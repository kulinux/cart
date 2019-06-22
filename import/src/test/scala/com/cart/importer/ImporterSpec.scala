package com.cart.importer;

import java.nio.file.Paths

import akka.actor.ActorSystem
import akka.stream.scaladsl.FileIO
import org.scalatest.{FlatSpec, Matchers};

class ImporterSpec extends FlatSpec with Matchers {

  implicit val system = ActorSystem()

  "A File" should "be importer" in {
    val filePath = "/Users/pako/project/cart/import/src/test/resources/1_line.csv"
    val file = FileIO.fromPath(Paths.get(filePath))
    val importer = new Importer(file)
    importer.run()
  }

}
