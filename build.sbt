name := "alpakka-sns"

version := "0.1"

scalaVersion := "2.12.6"

libraryDependencies ++= Seq(
  // Provides Akka Streams connector for SNS.
  // Transitively brings dependencies for `akka-actor` and `akka-stream`.
  "com.lightbend.akka" %% "akka-stream-alpakka-sns" % "0.20",

  // Command line options parsing.
  "com.github.scopt" %% "scopt" % "3.7.0",

  // For convenient logging.
  "com.typesafe.scala-logging" %% "scala-logging" % "3.9.0",
  "ch.qos.logback" % "logback-classic" % "1.2.3"
)

Compile/mainClass := Some("sns.Main")