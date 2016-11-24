package com.zhranklin

import akka.http.scaladsl._
import akka.util.Timeout
import com.typesafe.config.ConfigFactory

import scala.concurrent.duration._

object Boot extends App with MyRouteService {

  import ActorImplicits._

  implicit val timeout = Timeout(5.seconds)

  val conf = ConfigFactory.load().getConfig("settings.server")
  private val port = conf.getInt("http_port")
  val httpBindingFuture = Http().bindAndHandle(myRoute, "localhost", port)
  println(s"Server online at http://localhost:$port/")
}

