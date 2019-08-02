package services

import com.sksamuel.elastic4s.http.search.SearchResponse
import com.sksamuel.elastic4s.http.{ElasticClient, ElasticProperties, Response}
import javax.inject.Singleton

import scala.concurrent.{Await, Future}
import scala.concurrent.duration._


@Singleton
class ElasticSearchService {

  import com.sksamuel.elastic4s.http.ElasticDsl._

  val client = ElasticClient(ElasticProperties("http://localhost:9200"))

  def query(q: String): Future[Response[SearchResponse]] = {
    client.execute {
      search("skus" ).query("a")
    }
  }
}

object ElasticSearchService extends App {
  val es = new ElasticSearchService()
  val f = es.query("a")

  val res = Await.result(f, 10 seconds)
  println(res)
}
