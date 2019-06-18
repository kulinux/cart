package db

import slick.jdbc.HsqldbProfile.api._
import slick.lifted.Tag


object DBModel {

  case class Sku(id: String, name: String, description: String)

  class Skus(tag: Tag) extends Table[Sku](tag, "product") {
    def id = column[String]("id", O.PrimaryKey, O.AutoInc)
    def name = column[String]("name")
    def description = column[String]("description")
    def * = (id, name, description) <> (Sku.tupled, Sku.unapply)
  }
  val skus = TableQuery[Skus]

  val schema = skus.schema

  def getDB(): Unit = {
    Database.forConfig("h2mem1")
  }

  def buildDatabase(db: Database): Unit = {
    db.run(DBIO.seq(
      schema.create,
      skus ++= Seq(Sku("1", "DB Name 1", "DB Description 1"),
      Sku("2", "DB Name 2", "DB Description 2"),
      Sku("3", "DB Name 3", "DB Description 3"))
    ))
  }
}
