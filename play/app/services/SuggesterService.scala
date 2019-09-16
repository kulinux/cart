package services

import cats.Applicative

import cats._
import cats.implicits._

import scala.collection.mutable


case class Suggestion(id: String, q: Int)
case class SuggestionList(id: String, sugs: List[Suggestion])

object SuggestionAlgebra {
  implicit val slMonad = new Monoid[SuggestionList] {
    override def empty: SuggestionList = SuggestionList("0", List.empty)

    override def combine(x: SuggestionList, y: SuggestionList): SuggestionList =
      SuggestionList( x.id + y.id, x.sugs ++ y.sugs)
  }
}


trait SuggestionAlgebra[F[_]] {
  def store(sl: SuggestionList): F[SuggestionList]
  def staticSugs(): F[List[SuggestionList]]
  def userSugs(): F[List[SuggestionList]]
}

class SuggestionInMemory[F[_]: Applicative]
  extends SuggestionAlgebra[F] {

  val static = Map(
    "barbacoa" -> SuggestionList("barbacoa", List(
      Suggestion("Ternasco", 2),
      Suggestion("Chorizo", 1),
      Suggestion("Longaniza", 1)
    )),
    "botellon" -> SuggestionList("botellon", List(
      Suggestion("Sangria", 2),
      Suggestion("Hielo", 1)
    ))
  )

  val user = mutable.Map[String, SuggestionList]()

  override def store(sl: SuggestionList): F[SuggestionList] = {
    user + (sl.id -> sl)
    sl.pure[F]
  }
  override def staticSugs(): F[List[SuggestionList]] =
    static.values.toList.pure[F]

  override def userSugs(): F[List[SuggestionList]] =
    user.values.toList.pure[F]
}

class SuggesterService[F[_]](ss: SuggestionAlgebra[F]) {

  def suggest(q: String)(implicit M: Monad[F]) : F[List[SuggestionList]] = {
    val f: (List[SuggestionList]) => List[SuggestionList] = _.filter( _.id == q )
    M.map(ss.staticSugs())(f)
  }
}

object SuggesterService {
  def apply[F[_]](ss: SuggestionAlgebra[F]): SuggesterService[F] =
    new SuggesterService(ss)
}
