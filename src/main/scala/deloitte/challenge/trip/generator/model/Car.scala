package deloitte.challenge.trip.generator.model

import scala.collection.mutable.ListBuffer

/**
  * Connected Cars - Technical Challenge
  * Deloitte Technology Consulting
  *
  * Created by cci on 25.01.2018.
  */
case class Car(IMEI: String, IMSI: String, var speed: Int, var startPoint: Position, var endPoint: Position) {
  implicit class IntUtils(i: Int) {
    def ++() = i + 1

    def --() = i - 1
  }

  Car.numberOfCars = Car.numberOfCars ++
}

object Car {
  val listCars: ListBuffer[Car] = ListBuffer()
  var numberOfCars = 0

  def getNumberOfCars() = numberOfCars
}
