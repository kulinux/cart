package services

import akka.actor._
import model.{InputSearch, SearchResultItem, SearchResultItemCommand}

object WebSocketActor {
  def props(out: ActorRef) = Props(new WebSocketActor(out))
}

class WebSocketActor(out: ActorRef) extends Actor {
  def receive = {
    case msg: InputSearch => {
      out ! SearchResultItemCommand("CLEAR", SearchResultItem("", "") )
      out ! SearchResultItemCommand("ADD", SearchResultItem("id1", "From Play"))
      out ! SearchResultItemCommand("ADD", SearchResultItem("id2", "From Play 1"))
      out ! SearchResultItemCommand("ADD", SearchResultItem("id3", "From Play 2"))
    }
    case other => println(s"Unknown msg: $other")
  }
}
