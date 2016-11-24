package com.zhranklin

import akka.http.scaladsl.server.Directives
trait MyHttpService extends PlayTwirlSupport with Directives

import de.heikoseeberger.akkahttpjson4s.Json4sSupport
trait BasicJsonSupport extends Json4sSupport {
  import org.json4s.DefaultFormats
  implicit val serialization = org.json4s.jackson.Serialization
  implicit val formats = DefaultFormats
}

object BasicJsonSupport extends BasicJsonSupport

trait PlayTwirlSupport {
  import akka.http.scaladsl.marshalling.{Marshaller, ToEntityMarshaller}
  import akka.http.scaladsl.model.MediaType
  import akka.http.scaladsl.model.MediaTypes._
  import play.twirl.api.{Html, Txt, Xml}

  implicit val twirlHtmlMarshaller = twirlMarshaller[Html](`text/html`)
  implicit val twirlTxtMarshaller = twirlMarshaller[Txt](`text/plain`)
  implicit val twirlXmlMarshaller = twirlMarshaller[Xml](`text/xml`)

  protected def twirlMarshaller[A <: AnyRef : Manifest](contentType: MediaType): ToEntityMarshaller[A] =
    Marshaller.StringMarshaller.wrap(contentType)(_.toString)

}
