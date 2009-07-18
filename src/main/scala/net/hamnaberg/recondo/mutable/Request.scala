package net.hamnaberg.recondo.mutable

import java.net.URI


/**
 * @author <a href="mailto:erlend@hamnaberg.net">Erlend Hamnaberg</a>
 * @version $Revision : $
 */
class Request(val uri: URI, val method: Method) extends PayloadContainer {
  var credentials: Option[Credentials] = None
  var payload: Option[Payload] = None
  var headers: Headers = Headers()
  var conditionals : Conditionals = Conditionals()

  def setCredentials(c: Option[Credentials]) {
    require(c != null, "Credentials may not be null")
    credentials = c;
  }

  def addHeader(h: Header) {
    require(h != null)
    headers + h
  }
}

object Request {
  def apply(requestURI: URI) {
    new Request(requestURI, Method.GET)
  }

  def apply(requestURI: String) {
    new Request(URI.create(requestURI), Method.GET)
  }
}
