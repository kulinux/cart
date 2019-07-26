package com.cart.importer

import akka.stream.alpakka.elasticsearch.scaladsl.ElasticsearchFlow
import spray.json._
import DefaultJsonProtocol._
import akka.{Done, NotUsed}
import akka.stream.alpakka.elasticsearch.{WriteMessage, WriteResult}
import akka.stream.scaladsl.{Flow, Sink}
import org.apache.http.HttpHost
import org.elasticsearch.client.RestClient

import scala.concurrent.Future


object HeaderJsonProtocol extends DefaultJsonProtocol {
  implicit object SkuJsonFormat extends RootJsonFormat[Map[String, String]] {

    def write(c: Map[String, String]) = {
      c.map{ case (k, v) => JsString(v) }
      ???

    }
      //JsArray(JsString(c.name), JsNumber(c.red), JsNumber(c.green), JsNumber(c.blue))

    def read(value: JsValue) = ???
  }
}

trait StoreElasticSink {
  implicit val restClient: RestClient = RestClient.builder(
    new HttpHost("localhost", 9201)
  ).build()

  val indexName = "skus"
  val format: JsonFormat[Map[String, String]] = HeaderJsonProtocol.SkuJsonFormat


  val flowCassandra: Flow[WriteMessage[Map[String, String], NotUsed], WriteResult[Map[String, String], NotUsed], NotUsed] =
    ElasticsearchFlow.create[Map[String, String]](
    indexName,
    "_doc"
  ) (elasticsearchClient = restClient, sprayJsonWriter = HeaderJsonProtocol.SkuJsonFormat)

  val flow: Flow[Map[String, String], WriteResult[Map[String, String], NotUsed], NotUsed] =
    Flow[Map[String, String]]
      .map(m => WriteMessage.createIndexMessage(id = "00001", source = m) )
      .via(flowCassandra)

  val elasticSink : Sink[Map[String, String], _] = flow.to(Sink.ignore)

}
