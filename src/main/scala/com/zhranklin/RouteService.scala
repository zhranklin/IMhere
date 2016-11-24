package com.zhranklin

import akka.actor.ActorSystem
import akka.http.scaladsl.server._
import akka.stream.ActorMaterializer
import com.zhranklin.imhere.WebRoute

trait RouteService extends MyHttpService {
  def myRoute: Route
}

trait BaseRoute extends RouteService {
  def myRoute = getFromResourceDirectory("")
}

object ActorImplicits {

  implicit val system = ActorSystem("on-spray-can")
  implicit val materializer = ActorMaterializer()
  implicit val executionContext = system.dispatcher

}

trait MyRouteService extends BaseRoute with WebRoute
