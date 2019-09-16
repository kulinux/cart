package services

import org.scalatest.{FlatSpec, Matchers}

import cats._
import cats.implicits._

import scala.concurrent.Future

class SuggesterServiceSpec
  extends FlatSpec with Matchers {

  def ss() = {
    val ssMem = new SuggestionInMemory[Option]
    SuggesterService(ssMem)
  }

  "Barbacoa" should "suggest ingredients" in {
    val res = ss.suggest("barbacoa")
    println(res)
  }

}
