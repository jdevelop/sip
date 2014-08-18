package sample.restful

import org.apache.commons.io.IOUtils
import org.apache.http.client.config.RequestConfig
import org.apache.http.client.methods.{CloseableHttpResponse, HttpGet}
import org.apache.http.impl.client.{LaxRedirectStrategy, HttpClients}
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager
import org.joda.time.DateTime

import scalaz._, scalaz.Scalaz._
import argonaut._, Argonaut._

object Facebook {

  case class Paging(next: Option[String])

  case class Container[T](data: List[T], paging: Paging)

  case class Album(id: String, name: String, created_time: String)

  case class Photo(id: String, link: String, created_time: String)

  object Container {
    implicit def ContainerCodecJson[T: CodecJson]: CodecJson[Container[T]] = casecodec2(Container.apply[T], Container.unapply[T])("data", "paging")
  }

  object Paging {
    implicit def ContainerCodecJson: CodecJson[Paging] = casecodec1(Paging.apply, Paging.unapply)("next")
  }

  object Album {
    implicit def AlbumCodecJson: CodecJson[Album] = casecodec3(Album.apply, Album.unapply)("id", "name", "created_time")
  }

  object Photo {
    implicit def PhotoCodecJson: CodecJson[Photo] = casecodec3(Photo.apply, Photo.unapply)("id", "source", "created_time")
  }

  def retrieveImages(date: DateTime)(album: Album)(token: String): Stream[Photo] = {
    val timestamp = date.getMillis / 1000
    val url = s"https://graph.facebook.com/v2.1/${album.id}/photos?since=$timestamp&fields=source&access_token=$token"
    downloadAndParse[Photo](Some(url)).flatten
  }

  def downloadAndParse[T: CodecJson](optUrl: Option[String]): Stream[List[T]] = {
    optUrl match {
      case None ⇒ Stream.empty
      case Some(url) ⇒
        var response: CloseableHttpResponse = null
        val res = try {
          val get = new HttpGet(url)
          response = client.execute(get)
          val stream = response.getEntity.getContent
          val lines = io.Source.fromInputStream(stream, "UTF-8").getLines().mkString("")
          (lines, lines.decodeOption[Container[T]])
        } finally {
          IOUtils.closeQuietly(response)
          None
        }
        res match {
          case (lns, None) ⇒
            Stream.empty
          case (_, Some(c)) ⇒
            c.data #:: downloadAndParse(c.paging.next)
        }
    }
  }

  def retrieveAlbums(token: String): Stream[Album] = {
    downloadAndParse[Album](Some(s"https://graph.facebook.com/v2.1/me/albums?fields=id,name&access_token=$token")).flatten
  }

  def imagesStream(token: String, startDate: DateTime = new DateTime(0)) = {
    (for (albs <- retrieveAlbums _; res <- albs.traverseU((retrieveImages(startDate) _))) yield res)(token).flatten
  }

  val cm = new PoolingHttpClientConnectionManager()

  val requestConfig = RequestConfig.custom()
    .setSocketTimeout(20 * 1000)
    .setConnectTimeout(20 * 1000)
    .build()

  val client = HttpClients.custom()
    .setDefaultRequestConfig(requestConfig)
    .setConnectionManager(cm)
    .setRedirectStrategy(new LaxRedirectStrategy())
    .build()

}