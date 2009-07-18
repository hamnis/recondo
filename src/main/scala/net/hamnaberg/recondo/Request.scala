package net.hamnaberg.recondo

import java.net.URI


/**
 * @author <a href="mailto:erlend@hamnaberg.net">Erlend Hamnaberg</a>
 * @version $Revision: $
 */
class Request(val uri : URI, val method : Method.Value, val headers : Headers, val payload : Option[Payload], val credentials : Option[Credentials]) {

  def setCredentials(c : Option[Credentials]) : Request = {
    new Request(uri, method, headers, payload, c)
  }

  def addHeader(h : Header) : Request = {
      require(h != null)
      new Request(uri, method, headers + h, payload, credentials)
  }
}

object Request {
  def apply(requestURI : URI) {
    new Request(requestURI, Method.GET, Headers(), None, None)
  }
  def apply(requestURI : String) {
    new Request(URI.create(requestURI), Method.GET, Headers(), None, None)
  }
}
