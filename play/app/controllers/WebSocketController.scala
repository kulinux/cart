package controllers


import play.api.libs.json._
import play.api.mvc._
import play.api.libs.streams.ActorFlow
import play.api.mvc.WebSocket.MessageFlowTransformer
import javax.inject.Inject
import akka.actor.ActorSystem
import akka.stream.Materializer
import model.{InputSearch, SearchResult, SearchResultItem, SearchResultItemCommand}
import services.WebSocketActor

//https://www.playframework.com/documentation/2.7.x/ScalaWebSockets


class WebSocketController @Inject()(cc: ControllerComponents)(implicit system: ActorSystem, mat: Materializer)
  extends AbstractController(cc) {

  implicit val searchResultItem = Json.format[SearchResultItem]
  implicit val searchResultItemCommand = Json.format[SearchResultItemCommand]
  implicit val inEventFormat  = Json.format[InputSearch]
  implicit val outEventFormat = Json.format[SearchResult]

  implicit val messageFlowTransformer =
    MessageFlowTransformer.jsonMessageFlowTransformer[InputSearch, SearchResultItemCommand]


  def socket = WebSocket.accept[InputSearch, SearchResultItemCommand] { request =>
    ActorFlow.actorRef { out =>
      WebSocketActor.props(out)
    }
  }

}
