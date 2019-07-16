package com.cart.importer


import java.nio.charset.StandardCharsets

import akka.{Done, NotUsed}
import akka.actor.ActorSystem
import akka.stream.alpakka.cassandra.scaladsl.CassandraSink
import akka.stream.{ActorMaterializer, ActorMaterializerSettings, IOResult, Supervision}
import akka.stream.alpakka.csv.scaladsl.{CsvFormatting, CsvParsing, CsvToMap}
import akka.stream.scaladsl.{FileIO, Flow, Framing, Sink, Source}
import akka.util.ByteString
import com.datastax.driver.core.querybuilder.QueryBuilder
import com.datastax.driver.core.schemabuilder.{Create, CreateKeyspace, SchemaBuilder}
import com.datastax.driver.core.{Cluster, DataType, PreparedStatement}

import scala.concurrent.{Await, Future}
import scala.concurrent.duration._

trait StoreCassandraSink {

  val KeyspaceName = "store"
  val TableName = "skus"

  implicit val session = Cluster.builder
    .addContactPoint("127.0.0.1")
    .withPort(9042)
    .build
    .connect()

  def inserCql(values: Seq[String])= {
    values.foldLeft(
      QueryBuilder.insertInto(KeyspaceName, TableName)
    )((query, entry) => query.value(columnName(entry), QueryBuilder.bindMarker(columnName(entry))))
  }

  def createKeyspace() = {
    s"""CREATE KEYSPACE IF NOT EXISTS $KeyspaceName
       |WITH REPLICATION = {
       |'class': 'SimpleStrategy',
       |'replication_factor': 1
       |}
     """.stripMargin
  }

  def columnName(str: String) = str.replaceAll("-", "_")

  def table(): Create = {
    val cql = SchemaBuilder.createTable(KeyspaceName, TableName)
        .addPartitionKey(Headers.code, DataType.varchar())

    Headers.Headers
      .filter( _ != Headers.code )
      .foreach( head => cql.addColumn(columnName(head), DataType.varchar()))


    cql.ifNotExists()
  }

  lazy val cql = inserCql(Headers.Headers)
  lazy val preparedStatement = session.prepare(cql)

  def cassandraSink(headers: Seq[String]): Sink[Map[String, String], Future[Done]] = {

    val statementBinder = (csvRow: Map[String, String], statement: PreparedStatement) => {
      val stm = statement.bind()
      for((k, v) <- csvRow) {
        stm.setString(columnName(k), v)
      }
      stm
    }
    CassandraSink[Map[String, String]](parallelism = 2, preparedStatement, statementBinder)
  }

  def create(): Unit = {
    session.execute(createKeyspace())
    session.execute(table())
  }

}


class Importer(file: Source[ByteString, Future[IOResult]])
  extends StoreCassandraSink {

  val Country = "france"

  val parseCsv :Flow[ByteString, Map[String, String], _]  = Flow[ByteString]
    .via(CsvParsing.lineScanner(delimiter = '\t'))
    .via(CsvToMap.withHeadersAsStrings(StandardCharsets.UTF_8, Headers.Headers:_*))
    .filter( _(Headers.countries).contains(Country) )


  def run()(implicit system: ActorSystem) : Unit = {

    create()

    val decider: Supervision.Decider = { e =>
      system.log.error(e, "Unhandled exception in stream")
      Supervision.Stop
    }
    val materializerSettings = ActorMaterializerSettings(system).withSupervisionStrategy(decider)
    implicit val materializer = ActorMaterializer(materializerSettings)(system)
    implicit val dispatcher = system.dispatcher

    val res = file
      .via(parseCsv)
      .log("Error parsing CSV")
      .to(cassandraSink(Headers.Headers))
      .run()



    val res2 = Await.ready(res, 30 seconds)

    println(s"Res2 $res2")


    //.runForeach(x => println(x(Headers.code) + " " + x(Headers.countries)))

  }




}
