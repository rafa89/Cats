package com.underscore.chapter1

/**
  * Created by rafa on 03/06/2017.
  */

sealed trait Json

final case class JsObject(get: Map[String, Json]) extends Json
final case class JsString(get: String) extends Json
final case class JsDouble(get: Double) extends Json

trait JsonWriter[A] {
  def write(value: A): Json
}

final case class Person(name: String, email: String)

object JsonWriterInstances {
  implicit val stringJsonWriter = new JsonWriter[String] {
    override def write(value: String): Json =
      JsString(value)
  }

  implicit val personWriter = new JsonWriter[Person] {
    override def write(value: Person): Json =
      JsObject(Map(
        "name" -> JsString(value.name),
        "email" -> JsString(value.email)
      ))
  }
}


//interface objects
object Json {
  def toJson[A](value: A)(implicit w: JsonWriter[A]): Json =
    w.write(value)
}

object InterfaceObjectsUsage extends App {

  import JsonWriterInstances._

  val res = Json.toJson(Person("Dave", "as@emailcom"))
  println(res)
}

//interface syntax
object JsonSyntax {
  implicit class JsonWriterOps[A](value: A) {
    def toJson(implicit w: JsonWriter[A]): Json =
      w.write(value)
  }
}

object InterfaceSyntaxUsage extends App {
  import JsonWriterInstances._
  import JsonSyntax._
  Person("Dave", "as@emailcom").toJson
}