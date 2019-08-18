package controllers

import com.sksamuel.elastic4s.http.RequestSuccess
import com.sksamuel.elastic4s.http.search.SearchResponse
import javax.inject._
import model.SkuModel
import model.SkuModel._
import play.api.libs.json.{JsError, Json, Reads}
import play.api.mvc._
import services.ElasticSearchService

import scala.concurrent.ExecutionContext.Implicits._

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class SkuController @Inject()(cc: ControllerComponents,
                              elastic: ElasticSearchService
                                     ) extends AbstractController(cc) {
  val okJson = Json.obj("status" -> "ok")


  implicit val jsonSearch = Json.format[Search]
  implicit val jsonResultItem = Json.format[SkuResultItem]
  implicit val jsonResult= Json.format[SkuResult]


  def validateJson[A: Reads] = parse.json.validate(
    _.validate[A].asEither.left.map(e => BadRequest(JsError.toJson(e)))
  )


  def mapHits(res: RequestSuccess[SearchResponse] ) =
    res.result.hits.hits.map(
      sh => SkuModel.SkuResultItem(
        sh.id,
        sh.sourceAsMap("product_name").asInstanceOf[String],
        sh.sourceAsMap("ingredients_text").asInstanceOf[String]
      )
    ).toList

  def index() = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.index())
  }


  def search = Action.async(validateJson[Search]) { req =>
    elastic.query(s"*${req.body.q}*")
        .map( er => er match  {
            case res: RequestSuccess[SearchResponse] => SkuResult(res.result.hits.total, mapHits(res))
            case other => {
              println(s"No sucess $other")
              SkuModel.SkuResult(0, List())
            }
        })
        .map(js => Ok(Json.toJson(js)))

  }
}
