import Dependencies._

ThisBuild / scalaVersion     := "2.13.0"
ThisBuild / version          := "0.1.0-SNAPSHOT"
ThisBuild / organization     := "com.cart.poc"
ThisBuild / organizationName := "cats"

lazy val root = (project in file("."))
  .settings(
    name := "cats",
    libraryDependencies ++= Seq(
      scalaTest % Test,
      "org.typelevel" %% "cats-core" % "2.0.0-RC1"
    )
  )

// See https://www.scala-sbt.org/1.x/docs/Using-Sonatype.html for instructions on how to publish to Sonatype.
