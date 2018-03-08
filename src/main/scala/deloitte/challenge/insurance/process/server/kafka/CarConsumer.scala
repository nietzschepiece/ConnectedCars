package deloitte.challenge.insurance.process.server.kafka

import java.util
import org.apache.kafka.clients.consumer.KafkaConsumer
import scala.collection.JavaConverters._
/**
  * Connected Cars - Technical Challenge
  * Deloitte Technology Consulting
  *
  * Created by cci on 01.02.2018.
  */
object CarConsumer extends App {

  import java.util.Properties

  val TOPIC = "kafkatopic"

  val props = new Properties()
  props.put("bootstrap.servers", "localhost:9092")
  props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
  props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
  props.put("group.id", "something")

  val consumer = new KafkaConsumer[String, String](props)
  consumer.subscribe(util.Collections.singletonList(TOPIC))

  while (true) {
    val records = consumer.poll(100)
    for (record <- records.asScala) {
      println(record)
    }
  }
}