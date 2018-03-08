package deloitte.challenge.insurance.process.server

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer
import com.typesafe.scalalogging.LazyLogging
import deloitte.challenge.insurance.process.server.export.CSVDataWriter
import deloitte.challenge.insurance.process.server.kafka.CarProducer
import deloitte.challenge.trip.generator.model.Car
import net.liftweb.json._

import scala.collection.mutable.ListBuffer
import scala.io.StdIn

/**
  * Connected Cars - Technical Challenge
  * Deloitte Technology Consulting
  *
  * Created by cci on 28.01.2018.
  */
object ProcessServer extends App with LazyLogging {

  implicit val system = ActorSystem("my-system")
  implicit val materializer = ActorMaterializer()
  implicit val executionContext = system.dispatcher
  implicit val formats = DefaultFormats

  val listOfCars: ListBuffer[Car] = ListBuffer()

  val route =
    path("data") {
      post {
        entity(as[String]) { car =>
          CarProducer.sendToKafka(car)
          logger.info(car)
          val carObject = parseEntityToCar(car)
          listOfCars += carObject
          CSVDataWriter.writeToCsv(listOfCars.toList)
          complete(StatusCodes.OK)
        }
      }
    }

  val parseEntityToCar: String => Car = (car: String) => {
    val json = parse(car)
    json.extract[Car]
  }

  val bindingFuture = Http().bindAndHandle(route, "localhost", 8080)

  logger.info("Insurance Server online at http://localhost:8080/\nPress RETURN to stop...")
  StdIn.readLine()
  bindingFuture
    .flatMap(_.unbind())
    .onComplete(_ => system.terminate())

}