package com.cart.importer


import java.nio.charset.StandardCharsets
import java.nio.file.Paths

import akka.NotUsed
import akka.actor.ActorSystem
import akka.stream.{ActorMaterializer, IOResult}
import akka.stream.alpakka.csv.scaladsl.{CsvFormatting, CsvParsing, CsvToMap}
import akka.stream.scaladsl.{FileIO, Flow, Framing, Source}
import akka.util.ByteString

import scala.concurrent.Future


class Importer(file: Source[ByteString, Future[IOResult]]) {


  def run()(implicit system: ActorSystem) : Unit = {

    implicit val mat = ActorMaterializer()

    val parseCsv :Flow[ByteString, Map[String, String], _]  = Flow[ByteString]
      .via(CsvParsing.lineScanner(delimiter = '\t'))
      .via(CsvToMap.withHeadersAsStrings(StandardCharsets.UTF_8, Headers.Headers:_*))

    file
      .via(parseCsv)
      .runForeach(x => println(x(Headers.code) + " " + x(Headers.countries)))

  }




}
