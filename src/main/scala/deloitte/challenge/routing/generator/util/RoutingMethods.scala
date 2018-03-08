package deloitte.challenge.routing.generator.util

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{HttpMethods, HttpRequest, HttpResponse, StatusCodes}
import akka.http.scaladsl.unmarshalling.Unmarshal
import akka.stream.ActorMaterializer
import deloitte.challenge.routing.generator.model.MyJsonProtocol._
import deloitte.challenge.routing.generator.model._
import deloitte.challenge.trip.generator.model.Position
import net.liftweb.json.DefaultFormats
import spray.json._

import scala.concurrent.Future
import scala.util.{Failure, Success}

/**
  * Connected Cars - Technical Challenge
  * Deloitte Technology Consulting
  *
  * Created by cci on 28.01.2018.
  */

object RoutingMethods {

  implicit val system = ActorSystem()
  implicit val materializer = ActorMaterializer()
  implicit val executionContext = system.dispatcher
  implicit val formats = DefaultFormats

  val http = Http(system)

  def getGoogleRoutes(start: Position, end: Position ): List[Step] = {

    val googleUri = (s"${RoutingUtils.googleURI}origin=${start.latitude},${start.longitude}&destination=${end.latitude},${end.longitude}&key=${RoutingUtils.apiKey}")
    val request = HttpRequest(HttpMethods.GET, uri = googleUri)
    val responseFuture: Future[HttpResponse] = http.singleRequest(request)

    var stepList: List[Step] = Nil

    val result = responseFuture.map {
      case HttpResponse(StatusCodes.OK, headers, entity, _) => {
        val responseAsString = Unmarshal(entity).to[String]
        responseAsString.onComplete {
          case Success(json) => {
            val googleResp = json.parseJson.convertTo[GoogleResponse]
            stepList = googleResp.routes.head.legs.head.steps
            //stepList.foreach(step => println(step))
          }
          case Failure(ex) => ex.getMessage
        }
      }
      case x => s"Unexpected status code ${x.status}"
    }
    Thread.sleep(3000)
    stepList
  }
}
