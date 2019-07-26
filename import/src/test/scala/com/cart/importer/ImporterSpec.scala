package com.cart.importer;

import java.nio.file.Paths

import akka.actor.ActorSystem
import akka.stream.IOResult
import akka.stream.scaladsl.{FileIO, Source}
import akka.util.ByteString
import org.scalatest.{FlatSpec, Matchers}

import scala.concurrent.Future;

class ImporterSpec extends FlatSpec with Matchers {

  implicit val system = ActorSystem()

  "A File" should "be importer in cassandra" in {
    //val filePath = "/Users/pako/project/cart/db/en.openfoodfacts.org.products.csv"
    val filePath = "/Users/pako/project/cart/import/src/test/resources/1_line.csv"
    val file = FileIO.fromPath(Paths.get(filePath))
    val importer = new Importer(file)
    importer.create()
    importer.run()
  }


  "A File" should "be importer in elastic search" in {
    val filePath = "/Users/pako/project/cart/import/src/test/resources/1_line.csv"
    val file = FileIO.fromPath(Paths.get(filePath))
    val importer = new Importer(file)
    importer.create()
    importer.run()
  }
}

class ImporterElastic (file: Source[ByteString, Future[IOResult]])
  extends ImporterFile(file)
  with StoreElasticSink {

  val sink = elasticSink

}
