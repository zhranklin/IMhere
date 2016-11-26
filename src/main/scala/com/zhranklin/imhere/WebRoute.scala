package com.zhranklin.imhere

import com.zhranklin.RouteService
import Models._
import java.time.LocalDateTime

import akka.http.scaladsl.model.StatusCodes

trait WebRoute extends RouteService {
  abstract override def myRoute = super.myRoute ~
  path("index") {
    complete(html.index.render())
  } ~ path ("items" / Segment / Segment) { (uuid, username) ⇒
    complete(html.item_list.render(ModelDemo.items))
  } ~ path("richeng") {
    complete(html.item_list.render(ModelDemo.items.filter(_.showType == "日程")))
  } ~ path ("item" / IntNumber) { id ⇒
    complete(html.item_detail.render(ModelDemo.items(id)))
  } ~ path ("pref" / Segment) { username ⇒
    complete(html.preference.render())
  } ~ path("profile" / Segment) { username ⇒
    complete(html.profile(ModelDemo.users.filter(_.username == username).head))
  } ~ path("place_detail" / Segment / Segment / Segment / Segment / Segment) { (uuid, name, mac, strength, distance) ⇒
    complete(html.place_detail.render(uuid, name, mac, strength, distance))
  } ~ path("new_richeng") {
    get {
      complete(html.new_richeng.render())
    } ~ post {
      formField('time, 'content, 'place) { (time, content, place) ⇒
        val newItem = Item(0, "public", s"日程$time", "text", "日程", s"$time#$content", LocalDateTime.now(), place)
        ModelDemo.rawItems += newItem
        println(ModelDemo.rawItems)
        println(ModelDemo.items)
        redirect("/items/a/b", StatusCodes.Found)
      }
    }
  }
}
