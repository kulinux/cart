package services

import java.util.concurrent.{Executor, Executors}

import com.datastax.driver.core.querybuilder.{QueryBuilder, Select}
import com.datastax.driver.core.{Cluster, ResultSet, ResultSetFuture, Statement}
import services.CassandraService.CassandraSku

import scala.concurrent.{Await, ExecutionContext, Future, Promise}
import com.google.common.util.concurrent.{FutureCallback, Futures, ListenableFuture}
import javax.inject.Inject

import scala.language.implicitConversions
import play.api.libs.json._

import scala.concurrent.duration._


@javax.inject.Singleton
class CassandraService @Inject()(implicit executionContext: ExecutionContext) {


  implicit val session = new Cluster
  .Builder()
    .addContactPoints("localhost")
    .withPort(9042)
    .build()
    .connect()

  def getById(id: String): Future[CassandraSku] = {
    val select = QueryBuilder.select()
        .json()
        .from("store", "skus")
        .where( QueryBuilder.eq("code", id))
    val res = session.executeAsync( select )

    listenableFutureToFuture(res)
      .map(buildCassandraSku)
  }

  def buildCassandraSku(rs: ResultSet): CassandraSku = {
    val jsonStr = rs.one()
      .getString("[json]")
    val json : JsObject = Json.parse(jsonStr)
      .asInstanceOf[JsObject]

    val code = (json \ "code").get.as[String]
    val attr : Map[String, String] =
      json.fieldSet.map{case (key, value) => (key, value.as[String])}
          .toMap


    CassandraSku(code, attr)
  }

  implicit def listenableFutureToFuture[T](
                                            listenableFuture: ListenableFuture[T]
                                          ): Future[T] = {

    val executor = new MyExecutor()

    val promise = Promise[T]()
    Futures.addCallback(listenableFuture, new FutureCallback[T] {
      def onFailure(error: Throwable): Unit = {
        promise.failure(error)
        ()
      }
      def onSuccess(result: T): Unit = {
        promise.success(result)
        ()
      }
    }, executor)
    promise.future
  }
}

class MyExecutor(implicit ec: ExecutionContext) extends Executor {
  override def execute(command: Runnable): Unit = ec.execute(command)
}

object CassandraService {
  case class CassandraSku(id: String, attr: Map[String, String])
}

object FastTestCassandra extends App {
  import ExecutionContext.Implicits.global
  val cassandraService = new CassandraService()

  val fut = cassandraService.getById("8410095506431")
    .map(println)

  val res = Await.result(fut, 10 seconds)
  println(res)

}
