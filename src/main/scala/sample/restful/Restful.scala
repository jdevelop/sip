package sample.restful

import akka.actor.{Props, Actor, ActorSystem}
import akka.io.IO
import org.joda.time.DateTime
import shapeless.HNil
import spray.can.Http
import spray.http.Uri.Path
import spray.routing.PathMatcher.{Unmatched, Matched}
import spray.routing._

import argonaut._, Argonaut._

object Restful extends App {

  private[this] val EPOCH = new DateTime(0L)

  object DateTimeMatcher extends PathMatcher1[DateTime] {
    def apply(path: Path) = path match {
      case Path.Segment(segment, tail) if segment.forall(_.isDigit) ⇒ Matched(tail, new DateTime(segment.toLong * 1000) :: HNil)
      case _ ⇒ Unmatched
    }
  }

  trait FacebookImageFetcher extends Directives {

    val showImages =
      (get & path("weeks" / Segment / DateTimeMatcher.?)) {
        case (fbToken, startTime) ⇒
          // load last traversal date
          val lastTraverslDate = startTime.getOrElse(EPOCH)
          val stream = Facebook.imagesStream(fbToken, lastTraverslDate).take(10)
          complete(stream.asJson.pretty(PrettyParams.spaces2))
      }
  }

  private implicit val system = ActorSystem("RESTful-example")

  private implicit val exceptonHandler = ExceptionHandler.default

  private implicit val routerSettings = RoutingSettings.default

  private class RestActor extends Actor with HttpService with FacebookImageFetcher {

    override val actorRefFactory = system

    private val svc = showImages

    override def receive = runRoute(svc)

  }

  private val restActor = system.actorOf(Props[RestActor])

  IO(Http) ! Http.Bind(restActor, port = 8080, interface = "localhost")

  println("Started!")

}