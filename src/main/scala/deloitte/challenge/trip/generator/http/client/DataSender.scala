package deloitte.challenge.trip.generator.http.client

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{HttpMethods, HttpRequest}
import akka.stream.ActorMaterializer
import com.typesafe.scalalogging.LazyLogging
import deloitte.challenge.trip.generator.model.Car
import net.liftweb.json.Serialization.write
import net.liftweb.json._

import scala.util.{Failure, Success}


/**
  * Connected Cars - Technical Challenge
  * Deloitte Technology Consulting
  *
  * Created by cci on 25.01.2018.
  */
object DataSender extends LazyLogging{

  implicit val system = ActorSystem()
  implicit val materializer = ActorMaterializer()
  implicit val executionContext = system.dispatcher

  val http = Http(system)

  def sendCarStatus(car: Car) = {
    implicit val formats = DefaultFormats
    val jsonString = write(car)
    val request = HttpRequest(HttpMethods.POST, uri = "http://localhost:8080/data", entity = jsonString)
    val responseFuture = http.singleRequest(request)
    responseFuture.onComplete{
      case Success(_) =>
      case Failure(ex) => logger.warn("Request failed, Processing Server is not responding")
    }
  }

}
