package controllers

import play.api.libs.json._
import play.api.mvc._
import play.api.libs.streams.ActorFlow
import javax.inject.Inject
import akka.actor.ActorSystem
import akka.stream.Materializer
import services.WebSocketActor

//https://www.playframework.com/documentation/2.7.x/ScalaWebSockets

class WebSocketController @Inject()(cc: ControllerComponents)(implicit system: ActorSystem, mat: Materializer)
  extends AbstractController(cc) {

  println("Application::init")

  def socket = WebSocket.accept[String, String] { request =>
    ActorFlow.actorRef { out =>
      WebSocketActor.props(out)
    }
  }
}
