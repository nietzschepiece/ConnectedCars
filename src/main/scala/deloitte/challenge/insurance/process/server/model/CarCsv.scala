package deloitte.challenge.insurance.process.server.model

import deloitte.challenge.trip.generator.model.Position

/**
  * Connected Cars - Technical Challenge
  * Deloitte Technology Consulting
  *
  * Created by cci on 30.01.2018.
  */
case class CarCsv (IMEI: String, IMSI: String, var speed: Int, var startPoint: Position, var endPoint: Position)