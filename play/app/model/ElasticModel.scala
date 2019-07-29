package model

object ElasticModel {
  case class ESInputSearch(q: String)
  case class ESResult(res: Seq[String])
}
