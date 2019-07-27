package com.cart.importer

import akka.{Done, NotUsed}
import akka.stream.alpakka.elasticsearch.scaladsl.{ElasticsearchFlow, ElasticsearchSink}
import akka.stream.alpakka.elasticsearch.{WriteMessage, WriteResult}
import akka.stream.scaladsl.{Flow, Keep, Sink}
import org.apache.http.HttpHost
import org.elasticsearch.client.RestClient
import spray.json._

import scala.concurrent.Future


object HeaderJsonProtocol extends DefaultJsonProtocol {

  var count = 0
  implicit object SkuJsonFormat extends RootJsonFormat[Map[String, String]] {

    def write(c: Map[String, String]) = {
      val js: Map[String, JsValue] = c.map{ case (k, v) => (k -> JsString(v)) }
      val res = JsObject(js)
      println(s"Convert to json: $count")
      count = count + 1
      res
    }
      //JsArray(JsString(c.name), JsNumber(c.red), JsNumber(c.green), JsNumber(c.blue))

    def read(value: JsValue) = ???
  }
}

trait StoreElasticSink {
  implicit val restClient: RestClient = RestClient.builder(
    new HttpHost("localhost", 9200)
  ).build()

  val indexName = "skus"
  val format: JsonFormat[Map[String, String]] = HeaderJsonProtocol.SkuJsonFormat


  val newSink : Sink[WriteMessage[Map[String, String], NotUsed], Future[Done] ] =
    ElasticsearchSink.create[Map[String, String]](
    indexName,
    typeName = "_doc"
  ) (elasticsearchClient = restClient, sprayJsonWriter = HeaderJsonProtocol.SkuJsonFormat)

  val elasticSink : Sink[Map[String, String], Future[_]] =
    Flow[Map[String, String]]
      .map(m => WriteMessage.createIndexMessage(id = m("code"), source = m) )
      .toMat(newSink)(Keep.right)


}
