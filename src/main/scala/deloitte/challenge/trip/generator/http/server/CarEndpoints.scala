package deloitte.challenge.trip.generator.http.server

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{HttpResponse, StatusCodes}
import akka.http.scaladsl.server.Directives.{entity, _}
import akka.stream.ActorMaterializer
import com.typesafe.scalalogging.LazyLogging
import deloitte.challenge.trip.generator.model.Car
import deloitte.challenge.trip.generator.util.TripGeneratorTools
import net.liftweb.json.Serialization.write
import net.liftweb.json.{DefaultFormats, parse}

import scala.io.StdIn

/**
  * Connected Cars - Technical Challenge
  * Deloitte Technology Consulting
  *
  * Created by cci on 25.01.2018.
  */
class CarEndpoints extends LazyLogging {

  implicit val system = ActorSystem("my-system")
  implicit val materializer = ActorMaterializer()
  implicit val executionContext = system.dispatcher
  implicit val formats = DefaultFormats

  val addNewCarRoute =
    path("add" / "car") {
      post {
        entity(as[String]) { car =>
          val json = parse(car)
          val carObject = json.extract[Car] // Unmarshall JSON to Car object and add to list
          Car.listCars += carObject
          logger.info("Added new car with IMEI: " + carObject.IMEI)
          complete(StatusCodes.Created)
        }
      }
    }

  val getCarsRoute =
    path("get" / "cars") {
      get {
        entity(as[String]) { entity =>
          logger.info("Number of current cars: " + Car.getNumberOfCars())
          logger.info("List of cars:")
          Car.listCars.foreach(car => logger.info(car.toString))
          val jsonString = write(Car.listCars)
          complete(HttpResponse(entity = jsonString))
        }
      }
    }

  val startCarRoute =
    path("start" / "car") {
      get {
        parameter('imei) { imei =>
          val car = Car.listCars.flatMap(car => if (car.IMEI == imei) Some(car) else None)
            .lift(0)
            .getOrElse("Car with this IMEI was not found")

          if (car.isInstanceOf[Car])
            TripGeneratorTools.start(car.asInstanceOf[Car])
          else
            logger.info(car.toString)
          complete(StatusCodes.OK)
        }
      }
    }

  val routes = addNewCarRoute ~ getCarsRoute ~ startCarRoute

  val bindingFuture = Http().bindAndHandle(routes, "localhost", 8081)

  logger.info(s"\nCar Server online at http://localhost:8081/\nPress RETURN to stop...")
  StdIn.readLine()
  bindingFuture
    .flatMap(_.unbind())
    .onComplete(_ => system.terminate())

}

object CarEndpoints {
  def startService() = new CarEndpoints()
}

