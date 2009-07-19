package net.hamnaberg.recondo.mutable

import java.net.URI


/**
 * @author <a href="mailto:erlend@hamnaberg.net">Erlend Hamnaberg</a>
 * @version $Revision : $
 */
class Request(val uri: URI, val method: Method) extends PayloadContainer {
  private[this] var h: Headers = Headers()
  val conditionals: MutableConditionals = new MutableConditionals()
  var credentials: Option[Credentials] = None
  var payload: Option[Payload] = None

  def addHeader(h: Header) {
    require(h != null)
    this.h += h
  }

  def headers() = {
    var condititonalHeaders = conditionals.toHeaders
    var heads = h;
    condititonalHeaders foreach {x => heads = heads - x.name}
    heads
  }
}

object Request {
  def apply(requestURI: URI) = {
    new Request(requestURI, Method.GET)
  }

  def apply(requestURI: String) = {
    new Request(URI.create(requestURI), Method.GET)
  }
}
