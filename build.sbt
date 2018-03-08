name := "DeloitteTechnicalChallenge"

version := "1.0"

scalaVersion := "2.12.1"


libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-http" % "10.0.11",
  "com.typesafe.akka" %% "akka-http-testkit" % "10.0.11" % Test,
  "com.typesafe.scala-logging" %% "scala-logging" % "3.7.2",
  "org.slf4j" % "slf4j-api" % "1.7.5",
  "org.slf4j" % "slf4j-simple" % "1.7.5",
  "net.liftweb" %% "lift-json" % "3.2.0-RC1",
  "com.google.code.gson" % "gson" % "2.8.2",
  "io.spray" %% "spray-json" % "1.3.4",
  "au.com.bytecode" % "opencsv" % "2.4",
  "com.univocity" % "univocity-parsers" % "2.5.9"
)

libraryDependencies += "org.apache.kafka" % "kafka-clients" % "1.0.0"
resolvers += Resolver.mavenLocal


