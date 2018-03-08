package deloitte.challenge.trip.generator.util

import java.util.concurrent.Executors

import com.typesafe.config.ConfigFactory
import com.typesafe.scalalogging.LazyLogging
import deloitte.challenge.routing.generator.model.Step
import deloitte.challenge.routing.generator.util.RoutingMethods
import deloitte.challenge.trip.generator.http.client.DataSender
import deloitte.challenge.trip.generator.model.{Car, Position}

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}

/**
  * Connected Cars - Technical Challenge
  * Deloitte Technology Consulting
  *
  * Created by cci on 28.01.2018.
  */
object TripGeneratorTools extends LazyLogging {

  val batchTime = ConfigFactory.load().getDouble("batch.processing.realTime")
  implicit val ec = ExecutionContext.fromExecutorService(Executors.newWorkStealingPool(8))

  def start(car: Car) = {
    val f = Future {
      logger.info("Car with IMEI: " + car.IMEI + " started the trip")

      val stepsList = RoutingMethods.getGoogleRoutes(car.startPoint, car.endPoint)
      stepsList.foreach(step => {
        calculateStartPositions(car, step)
        var distance: Double = step.distance.value

        while (distance > 0) {
          DataSender.sendCarStatus(car)
          Thread.sleep(5000) // Sending data every 5 seconds

          car.speed = getRandomSpeed()
          val traveledDistance: Double = moveCar(car)
          val nextPosition: Position = if (traveledDistance < distance) calculateNewPosition(car, traveledDistance / distance) else car.endPoint

          distance = distance - traveledDistance
          car.startPoint.latitude = nextPosition.latitude
          car.startPoint.longitude = nextPosition.longitude
        }
      })
    }
    f.onComplete {
      case Success(_) => logger.info("Car with IMEI: " + car.IMEI + " finished the trip")
      case Failure(ex) => ex.getMessage
    }
  }

  private def calculateStartPositions(car: Car, step: Step): Unit = {
    car.startPoint.latitude = step.start_location.lat
    car.startPoint.longitude = step.start_location.lng
    car.endPoint.latitude = step.end_location.lat
    car.endPoint.longitude = step.end_location.lng
  }

  private def moveCar(car: Car): Double = {
    def kmToMeter(speed: Double): Double = speed * 1000

    val hour: Double = 60
    val timeOfTravel: Double = batchTime // Change to 0.083 for real-time data
    val traveledDistance: Double = car.speed / hour * timeOfTravel
    return kmToMeter(traveledDistance)
  }

  private def calculateNewPosition(car: Car, ratioDistance: Double): Position = {
    val latitude = (1 - ratioDistance) * car.startPoint.latitude + ratioDistance * car.endPoint.latitude
    val longitude = (1 - ratioDistance) * car.startPoint.longitude + ratioDistance * car.endPoint.longitude
    Position(latitude, longitude)
  }

  private def getRandomSpeed(): Int = {
    val start = 30
    val end = 60
    val random = new scala.util.Random
    start + random.nextInt((end - start) + 1)
  }

}
