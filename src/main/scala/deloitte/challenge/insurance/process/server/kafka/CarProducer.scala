package deloitte.challenge.insurance.process.server.kafka

import java.util.Properties
import org.apache.kafka.clients.producer._

/**
  * Connected Cars - Technical Challenge
  * Deloitte Technology Consulting
  *
  * Created by cci on 01.02.2018.
  */
object CarProducer {

  val props = new Properties()
  props.put("bootstrap.servers", "localhost:9092")
  props.put("acks", "1")
  props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
  props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")

  val producer = new KafkaProducer[String, String](props)
  val topic = "kafkatopic"

  def sendToKafka(car: String) = {
    val record = new ProducerRecord(topic, "Key:", car)
    producer.send(record)
  }

  // producer.close()
}
