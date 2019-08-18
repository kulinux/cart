package model

object SkuModel {

  case class Search(q: String)

  case class SkuResult(total: Long, items: List[SkuResultItem])

  case class SkuResultItem(id: String,
                        name: String,
                        desc: String)

}
