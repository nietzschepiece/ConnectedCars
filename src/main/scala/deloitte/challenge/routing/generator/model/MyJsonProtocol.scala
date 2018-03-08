package deloitte.challenge.routing.generator.model

import spray.json._

/**
  * Connected Cars - Technical Challenge
  * Deloitte Technology Consulting
  *
  * Created by cci on 28.01.2018.
  */
object MyJsonProtocol extends DefaultJsonProtocol{

  lazy implicit val googleResponseFormat: JsonFormat[GoogleResponse] = jsonFormat1(GoogleResponse)
  lazy implicit val routeFormat: JsonFormat[Route] = jsonFormat1(Route)
  lazy implicit val legFormat: JsonFormat[Leg] = jsonFormat1(Leg)
  lazy implicit val stepFormat: JsonFormat[Step] = jsonFormat3(Step)
  lazy implicit val positionFormat: JsonFormat[Position] = jsonFormat2(Position)
  lazy implicit val distanceFormat: JsonFormat[Distance] = jsonFormat1(Distance)

}
