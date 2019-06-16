package model


case class InputSearch(text: String)

case class SearchResultItem (
            id: String,
            name: String)

case class SearchResultItemCommand(
           command: String,
           sri: Option[SearchResultItem])

case class SearchResult (
            searchText: String,
            searchResult: List[SearchResultItem] )

case class PrepareCart(items: Seq[ItemCart])

case class ItemCart(
  id: String,
  name: String,
  quantity: Int
)

case class AddItem( id: String, quantity: Int = 1)

case class RemoveItem( id: String)

case class Search(q: String)
