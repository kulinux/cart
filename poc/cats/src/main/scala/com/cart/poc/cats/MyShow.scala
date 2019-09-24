package com.cart.poc.cats

import cats._
import cats.syntax._
import cats.implicits._


case class Person(name: String, sn: String)

object Person {
    implicit val pShow = Show.apply[Person](p => s"${p.name}, ${p.sn}")
}

object MyApp extends App {
    val p = Person("Paco", "Benitez")
    println(p.show)
}