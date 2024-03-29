package com.cart.importer


import java.nio.charset.StandardCharsets

import akka.actor.ActorSystem
import akka.stream.alpakka.csv.scaladsl.{CsvParsing, CsvToMap}
import akka.stream.scaladsl.{Flow, Keep, Sink, Source}
import akka.stream.{ActorMaterializer, ActorMaterializerSettings, IOResult, Supervision}
import akka.util.ByteString

import scala.concurrent.duration._
import scala.concurrent.{Await, Future}

class ImporterCassandra(file: Source[ByteString, Future[IOResult]])
  extends ImporterFile(file)
  with StoreCassandraSink {

  create()
  val sink = cassandraSink(Headers.Headers)
}

class ImporterElastic(file: Source[ByteString, Future[IOResult]])
  extends ImporterFile(file)
    with StoreElasticSink  {

  val sink = elasticSink
}


abstract class ImporterFile(file: Source[ByteString, Future[IOResult]]) {

  val Country = "Spain"

  val parseCsv :Flow[ByteString, Map[String, String], _]  = Flow[ByteString]
    .map( str => str.filterNot(_ == '\\' ).filterNot( _ == '"' ) )
    .via(CsvParsing.lineScanner(delimiter = '\t', maximumLineLength = 1000000))
    .via(CsvToMap.withHeadersAsStrings(StandardCharsets.UTF_8, Headers.Headers:_*))
    .filter( _(Headers.countries).contains(Country) )

  val sink: Sink[Map[String, String], Future[_]]

  def run()(implicit system: ActorSystem): Unit = {


    val decider: Supervision.Decider = { e =>
      system.log.error(e, "Unhandled exception in stream")
      Supervision.Stop
    }
    val materializerSettings = ActorMaterializerSettings(system).withSupervisionStrategy(decider)
    implicit val materializer = ActorMaterializer(materializerSettings)(system)
    implicit val dispatcher = system.dispatcher

    val res : (Future[IOResult], Future[_])= file
      .via(parseCsv)
      .log("Error CSV Parse")
      .toMat(sink)(Keep.both)
      .run()

    val res1 = Await.ready(res._1, 3000 seconds)
    val res2 = Await.ready(res._2, 3000 seconds)

    println(s"Res2 $res1, $res2")

  }

}
