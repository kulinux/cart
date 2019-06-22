package controllers

import db.DBModel
import db.DBModel.Skus
import javax.inject._
import model.{AddItem, ItemCart, PrepareCart, RemoveItem, Search, SearchResultItem}
import play.api.db.slick.DatabaseConfigProvider
import play.api.libs.json.{JsError, Json, Reads}
import play.api.mvc._
import slick.jdbc.HsqldbProfile.api._
import slick.jdbc.JdbcProfile
import sun.security.pkcs11.Secmod.DbMode

import scala.concurrent.ExecutionContext.Implicits._
import scala.concurrent.Future

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class PrepareCartController @Inject()( cc: ControllerComponents,
                                       dbConfigProvider: DatabaseConfigProvider,
                                     ) extends AbstractController(cc) {
  val con = dbConfigProvider.get[JdbcProfile].db

  db.DBModel.buildDatabase(con)

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

  def list = Action.async {
     mapToJson( con.run(DBModel.skus.result) )
  }

  def mapToJson(res: Future[Iterable[Skus#TableElementType]]) = {
    res.map(
      _.map(sku => ItemCart(sku.id, sku.name, 0) )
    ).map( ic => Ok(Json.toJson(ic)) )
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

  def search = Action.async(validateJson[Search]) { req =>
    mapToJson(
      con.run(
        DBModel.skus.filter(_.name.like(s"%${req.body.q}%")).result
      )
    )
  }
}
