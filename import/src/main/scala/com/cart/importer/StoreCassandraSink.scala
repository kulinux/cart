package com.cart.importer

import akka.Done
import akka.stream.alpakka.cassandra.scaladsl.CassandraSink
import akka.stream.scaladsl.Sink
import com.datastax.driver.core.querybuilder.QueryBuilder
import com.datastax.driver.core.schemabuilder.{Create, SchemaBuilder}
import com.datastax.driver.core.{Cluster, DataType, PreparedStatement}

import scala.concurrent.Future

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
