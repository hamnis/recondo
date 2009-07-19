package net.hamnaberg.recondo.mutable

import java.net.URI


/**
 * @author <a href="mailto:erlend@hamnaberg.net">Erlend Hamnaberg</a>
 * @version $Revision : $
 */
class RequestBuilder(val uri: URI, val method: Method) extends PayloadContainer {
  private[this] var h: Headers = Headers()
  val conditionals: MutableConditionals = new MutableConditionals()
  var credentials: Option[Credentials] = None
  var payload: Option[Payload] = None

  def addHeader(h: Header) {
    require(h != null)
    this.h += h
  }

  private def headers() = {
    var condititonalHeaders = conditionals.toHeaders
    var heads = h;
    condititonalHeaders foreach {x => heads = heads - x.name}
    heads
  }
}

object RequestBuilder {
  def apply(requestURI: URI) = {
    new RequestBuilder(requestURI, Method.GET)
  }

  def apply(requestURI: String) = {
    new RequestBuilder(URI.create(requestURI), Method.GET)
  }

  implicit def requestWrapper(rb : RequestBuilder) : Request = {
    new Request(rb.uri, rb.method, rb.headers, rb.credentials, rb.payload)
  }
}
