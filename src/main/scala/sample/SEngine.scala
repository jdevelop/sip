package sample

import scala.beans.BeanProperty

case class SEngine(@BeanProperty power: Int,
                   @BeanProperty maxRpm: Int,
                   @BeanProperty minRpm: Int)
