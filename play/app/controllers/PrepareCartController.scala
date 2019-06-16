package controllers

import javax.inject._
import model.{AddItem, ItemCart, PrepareCart, RemoveItem, Search, SearchResultItem}
import play.api.libs.json.{JsError, Json, Reads}
import play.api.mvc._

import scala.concurrent.ExecutionContext.Implicits._

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class PrepareCartController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  def index() = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.index())
  }

  var cart = new PrepareCart(Seq(ItemCart("id", "name", 0)))
  val okJson = Json.obj("status" -> "ok")


  implicit val jsonCartItem = Json.format[ItemCart]
  implicit val jsonCart = Json.format[PrepareCart]
  implicit val jsonAddItem = Json.format[AddItem]
  implicit val jsonRemoveItem = Json.format[RemoveItem]
  implicit val jsonSearch = Json.format[Search]
  implicit val jsonSearchResult = Json.format[SearchResultItem]

  def list = Action {
    val json = Json.toJson(cart.items)
    Ok(json)
  }

  def validateJson[A: Reads] = parse.json.validate(
    _.validate[A].asEither.left.map(e => BadRequest(JsError.toJson(e)))
  )


  def add = Action(validateJson[AddItem]) { req =>
    val request = req.body
    cart = PrepareCart(cart.items :+ ItemCart(request.id, "name " + request.id, request.quantity))
    Ok(okJson)
  }

  def remove = Action(validateJson[RemoveItem]) { req =>
    val request = req.body
    cart = PrepareCart(cart.items.filter( _.id == request.id ) )
    Ok(okJson)
  }

  def search = Action(validateJson[Search]) { req =>
    val request = req.body.q
    Ok(Json.toJson( Seq(
      SearchResultItem("1", "name 1"),
      SearchResultItem("2", "name 2")
    ) ) )
  }
}
