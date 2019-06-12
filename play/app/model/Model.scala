package model


case class InputSearch(text: String)

case class SearchResultItem (
            id: String,
            name: String)

case class SearchResultItemCommand(
           command: String,
           sri: SearchResultItem)

case class SearchResult (
            searchText: String,
            searchResult: List[SearchResultItem] )

