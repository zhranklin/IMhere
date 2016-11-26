package com.zhranklin.imhere

import java.time._

import com.zhranklin.imhere.Models._
import org.jsoup.Jsoup

import scala.collection.mutable

/**
 * Created by Zhranklin on 16/10/5.
 */
object Models {

  case class Place(id: String, name: String)

  case class Item(id: Int, owner: String, title: String, `type`: String, showType: String, content: String, date: LocalDateTime, place: String) {
    def abs = `type` match {
      case "html" ⇒
        Jsoup.parse(content).body().text().take(200)
      case "text" ⇒
        content.take(200)
    }
    def withId(id: Int) = Item(id, owner, title, `type`, showType, content, date, place)
  }

  case class User(username: String, name: String, gender: String, birthDate: LocalDate, stuId: String, description: String)

  case class UserPass(username: String, name: String, password: String) {
//    def asUser = User(username, name)
  }

}

object ModelDemo {
  val rawItems: mutable.MutableList[Item] = mutable.MutableList(("四川大学校车时刻表", "校车站",
    <table class="table">
      <thead>
        <tr>
          <th>华西—江安</th>
          <th>江安—华西</th>
          <th>望江—江安</th>
          <th>江安—望江</th>
        </tr>
      </thead>

      <tbody>
        <tr>
          <td>7:15</td>
          <td>江安点8:20</td>
          <td>7:10</td>
          <td>"文星花园、江安花园7:10到江安点7:20"</td>
        </tr>
        <tr>
          <td>7:40</td>
          <td>江安点8:40</td>
          <td>7:20</td>
          <td>"文星花园7：40 江安花园7：50"</td>
        </tr>
        <tr>
          <td>9:10</td>
          <td>江安点9:00</td>
          <td>7:30</td>
          <td>"文星花园、江安花园8:10 到江安点:8:20"</td>
        </tr>
        <tr>
          <td>10:00</td>
          <td>10:15行政楼</td>
          <td>7:40</td>
          <td>江安点8:40</td>
        </tr>
        <tr>
          <td>11:10</td>
          <td>12:10行政楼</td>
          <td>7:50</td>
          <td>江安点9:00</td>
        </tr>
        <tr>
          <td>12:10</td>
          <td>13:00行政楼</td>
          <td>8:10</td>
          <td>10:15行政楼</td>
        </tr>
        <tr>
          <td>13:00</td>
          <td>14:55行政楼</td>
          <td>8:20</td>
          <td>11:20行政楼</td>
        </tr>
        <tr>
          <td>13:55</td>
          <td>15:45行政楼</td>
          <td>9:00</td>
          <td>12:10行政楼</td>
        </tr>
        <tr>
          <td>14:50</td>
          <td>16:35行政楼</td>
          <td>10:10</td>
          <td>13:00行政楼</td>
        </tr>
        <tr>
          <td>15:40</td>
          <td>17:20行政楼</td>
          <td>10:50</td>
          <td>13:25行政楼</td>
        </tr>
        <tr>
          <td>16:50</td>
          <td>17:50行政楼</td>
          <td>11:10</td>
          <td>14:00行政楼</td>
        </tr>
        <tr>
          <td>17:20</td>
          <td>18:30一基楼</td>
          <td>12:10</td>
          <td>14:55行政楼</td>
        </tr>
        <tr>
          <td>17:50（至文星花园）</td>
          <td>18:50一基楼</td>
          <td>12:30</td>
          <td>15:45行政楼</td>
        </tr>
        <tr>
          <td>18:15</td>
          <td>19:40一基楼</td>
          <td>13:00</td>
          <td>16:35行政楼</td>
        </tr>
        <tr>
          <td>19:30</td>
          <td>21:20一基楼</td>
          <td>13:55</td>
          <td>17:10行政楼</td>
        </tr>
        <tr>
          <td></td>
          <td>（江安-华西-望江</td>
          <td>14:30</td>
          <td>17:30行政楼</td>
        </tr>
        <tr>
          <td></td>
          <td></td>
          <td>15:00</td>
          <td>17:50行政楼</td>
        </tr>
        <tr>
          <td></td>
          <td></td>
          <td>15:30</td>
          <td>18:30一基楼</td>
        </tr>
        <tr>
          <td></td>
          <td></td>
          <td>15:45</td>
          <td>18:50一基楼</td>
        </tr>
        <tr>
          <td></td>
          <td></td>
          <td>16:50</td>
          <td>19:00一基楼</td>
        </tr>
        <tr>
          <td></td>
          <td></td>
          <td>17:20</td>
          <td>20:00一基楼</td>
        </tr>
        <tr>
          <td></td>
          <td></td>
          <td>18:00（至文星花园）</td>
          <td>21:20一基楼</td>
        </tr>
        <tr>
          <td></td>
          <td></td>
          <td>18:15</td>
          <td>22:15一基楼</td>
        </tr>
        <tr>
          <td></td>
          <td></td>
          <td>18:50（至文星花园）</td>
          <td>22:30一基楼</td>
        </tr>
        <tr>
          <td></td>
          <td></td>
          <td>19:30（至文星花园）</td>
          <td></td>
        </tr>
        <tr>
          <td></td>
          <td></td>
          <td>20:30</td>
          <td></td>
        </tr>
        <tr>
          <td></td>
          <td></td>
          <td>21:10</td>
          <td></td>
        </tr>
        <tr>
          <td></td>
          <td></td>
          <td>21:40</td>
          <td></td>
        </tr>
        <tr>
          <td></td>
          <td></td>
          <td>22:30</td>
          <td></td>
        </tr>
      </tbody>
    </table>), ("课表", "教学楼",
    <table class="table">
      <thead>
        <tr>
          <th></th>
          <th>星期一</th>
          <th>星期二</th>
          <th>星期三</th>
          <th>星期四</th>
          <th>星期五</th>
          <th>星期六</th>
          <th>星期日</th>
        </tr>
      </thead>
      <tbody>
        <tr>
          <td>第一节</td>
          <td>大物</td>
          <td>离散</td>
          <td></td>
          <td>英语</td>
          <td></td>
          <td>毛概</td>
          <td></td>
        </tr>
        <tr>
          <td>第二节</td>
          <td>大物</td>
          <td>离散</td>
          <td></td>
          <td>英语</td>
          <td></td>
          <td>毛概</td>
          <td></td>
        </tr>
        <tr>
          <td>第三节</td>
          <td>数分</td>
          <td>离散</td>
          <td>数分</td>
          <td></td>
          <td>数分</td>
          <td>毛概</td>
          <td></td>
        </tr>
        <tr>
          <td>第四节</td>
          <td>数分</td>
          <td></td>
          <td>数分</td>
          <td>数分</td>
          <td></td>
          <td>毛概</td>
          <td></td>
        </tr>
        <tr>
          <td>第五节</td>
          <td>体育</td>
          <td>计网</td>
          <td>数构</td>
          <td>计组</td>
          <td>计组</td>
          <td></td>
          <td></td>
        </tr>
        <tr>
          <td>第六节</td>
          <td>体育</td>
          <td>计网</td>
          <td>数构</td>
          <td>计组</td>
          <td>计组</td>
          <td></td>
          <td></td>
        </tr>
        <tr>
          <td>第七节</td>
          <td></td>
          <td>计网</td>
          <td>数结</td>
          <td>计组</td>
          <td>计组</td>
          <td></td>
          <td></td>
        </tr>
        <tr>
          <td>第八节</td>
          <td></td>
          <td>计网</td>
          <td>数构</td>
          <td>离散</td>
          <td>计组</td>
          <td></td>
          <td></td>
        </tr>
        <tr>
          <td>第九节</td>
          <td></td>
          <td>计网</td>
          <td>数构</td>
          <td>离散</td>
          <td></td>
          <td></td>
          <td></td>
        </tr>
        <tr>
          <td>第十节</td>
          <td></td>
          <td></td>
          <td></td>
          <td></td>
          <td></td>
          <td></td>
          <td>行教</td>
        </tr>
        <tr>
          <td>第十一节</td>
          <td></td>
          <td></td>
          <td></td>
          <td></td>
          <td></td>
          <td></td>
          <td>行教</td>
        </tr>
      </tbody>
    </table>)
  ).map {
    case (title, place, html) ⇒ Item(0, "public", title, "html", "实用信息", html.toString(), LocalDateTime.of(LocalDate.of(2016, 11, 20), LocalTime.MIDNIGHT), place)
  }

  def items = {
    println("asdfasdfasd")
    rawItems.zipWithIndex.map(tp ⇒ tp._1.withId(tp._2))
  }

  val users = mutable.MutableList(
    User("sht", "束晗涛", "男", LocalDate of (1996, 7, 10), "2014141462195", null)
  )

  val places = mutable.MutableList(
    Place("001", "二基楼"),
    Place("002", "教学楼"),
    Place("003", "宿舍区"),
    Place("004", "商业街")
  )
}
