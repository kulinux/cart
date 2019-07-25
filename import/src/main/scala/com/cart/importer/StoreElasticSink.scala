package com.cart.importer

import akka.stream.alpakka.elasticsearch.scaladsl.ElasticsearchFlow
import spray.json._
import DefaultJsonProtocol._
import akka.NotUsed
import akka.stream.alpakka.elasticsearch.{WriteMessage, WriteResult}
import akka.stream.scaladsl.Flow
import org.apache.http.HttpHost
import org.elasticsearch.client.RestClient


object HeaderJsonProtocol extends DefaultJsonProtocol {
  implicit object ColorJsonFormat extends RootJsonFormat[Map[String, String]] {

    def write(c: Map[String, String]) = {
      c.map{ case (k, v) => JsString(v) }
      ???

    }
      //JsArray(JsString(c.name), JsNumber(c.red), JsNumber(c.green), JsNumber(c.blue))

    def read(value: JsValue) = ???
  }
}

trait StoreElasticSink {
  implicit val client: RestClient = RestClient.builder(
    new HttpHost("localhost", 9201)
  ).build()

  val indexName = "skus"
  implicit val format: JsonFormat[Book] = jsonFormat1(Book)

  case class Book(id: String)

  val flow: Flow[WriteMessage[Book, NotUsed], WriteResult[Book, NotUsed], NotUsed] =
    ElasticsearchFlow.create[Book](
    indexName,
    "_doc"
  )

}
