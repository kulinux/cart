name := """cart-play"""
organization := "com.cart"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.12.8"

libraryDependencies += guice
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "4.0.2" % Test
libraryDependencies += "com.typesafe.play" %% "play-slick" % "3.0.0"
libraryDependencies += "com.h2database" % "h2" % "1.4.192"

libraryDependencies += "com.datastax.cassandra" % "cassandra-driver-core" % "3.7.1"



val elastic4sVersion = "6.3.3"
libraryDependencies ++= Seq(
  "com.sksamuel.elastic4s" %% "elastic4s-http" % elastic4sVersion
)



// Adds additional packages into Twirl
//TwirlKeys.templateImports += "com.cart.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "com.cart.binders._"
