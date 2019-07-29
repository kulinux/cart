name := "cart-import"

version := "1.0"

scalaVersion := "2.12.6"

lazy val akkaVersion = "2.5.23"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % akkaVersion,
  "com.typesafe.akka" %% "akka-stream" % akkaVersion,
  "com.lightbend.akka" %% "akka-stream-alpakka-csv" % "0.18",
  "com.lightbend.akka" %% "akka-stream-alpakka-cassandra" % "1.1.0",
  "com.lightbend.akka" %% "akka-stream-alpakka-elasticsearch" % "1.1.0",
  "com.typesafe.akka" %% "akka-testkit" % akkaVersion,
  "org.scalatest" %% "scalatest" % "3.0.5" % "test"
)
