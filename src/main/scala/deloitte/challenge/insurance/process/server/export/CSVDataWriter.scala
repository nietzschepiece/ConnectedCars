package deloitte.challenge.insurance.process.server.export

import java.io.{BufferedWriter, FileWriter}

import au.com.bytecode.opencsv.CSVWriter
import deloitte.challenge.trip.generator.model.Car

import scala.collection.JavaConverters._
import scala.collection.mutable.ListBuffer

/**
  * Connected Cars - Technical Challenge
  * Deloitte Technology Consulting
  *
  * Created by cci on 30.01.2018.
  */
object CSVDataWriter {
  def writeToCsv(carList: List[Car]) = {
    def convertCarToArray(car: Car): Array[String] = {
      Array(car.IMEI, car.IMSI, car.speed.toString, car.startPoint.toString, car.endPoint.toString)
    }
    val outputFile = new BufferedWriter(new FileWriter("/Users/cci/Desktop/fisier.csv"))
    val csvWriter = new CSVWriter(outputFile)

    val csvSchema = Array("IMEI", "IMSI", "Speed", "Start Position", "End Position")
    val listOfRecords = ListBuffer[Array[String]]()
    listOfRecords += csvSchema

    carList.foreach(car => {
      val carArray = convertCarToArray(car)
      listOfRecords += carArray
    })

    val listOfRecordsJava = listOfRecords.asJava
    csvWriter.writeAll(listOfRecordsJava)
    outputFile.close()
  }

}

