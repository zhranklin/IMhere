package com.zhranklin.imhere

import com.zhranklin.RouteService

trait WebRoute extends RouteService {
  abstract override def myRoute = super.myRoute ~
  path("index") {
    complete(html.index.render())
  } ~ path ("items" / Segment / Segment) { (uuid, username) ⇒
    complete(html.item_list.render(ModelDemo.items))
  } ~ path ("item" / IntNumber) { id ⇒
    complete(html.item_detail.render(ModelDemo.items(id)))
  }

}
