package model

object CassandraModel {

  case class Sku(code: String, attr: Map[String, String])

}
