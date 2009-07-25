package net.hamnaberg.recondo

import java.net.URI


/**
 * @author <a href="mailto:erlend@hamnaberg.net">Erlend Hamnaberg</a>
 * @version $Revision : $
 */
class RequestBuilder private (val uri: URI, val method: Method, private[this] val headers: Headers, val conditionals: Conditionals, val credentials: Option[Credentials], val payload: Option[Payload]) extends PayloadContainer {

  def setHeaders(h: Headers) {
    new RequestBuilder(uri, method, headers, conditionals, credentials, payload)
  }

  def condtionals(c: Conditionals) = {
    new RequestBuilder(uri, method, headers, c, credentials, payload)
  }

  def credentials(c: Credentials) = {
    new RequestBuilder(uri, method, headers, conditionals, Some(c), payload)
  }

  def payload(p: Payload) : RequestBuilder = {
    new RequestBuilder(uri, method, headers, conditionals, credentials, Some(p))
  }

  private def calculateHeaders() = {
    var condititonalHeaders = conditionals.toHeaders
    var heads = headers;
    condititonalHeaders foreach {heads -= _.name}
    heads ++ condititonalHeaders
  }

  def toRequest() : Request = new Request(uri, method, calculateHeaders, credentials, payload)
}

object RequestBuilder {
  def apply(requestURI: URI) = new RequestBuilder(requestURI, Method.GET, Headers(), Conditionals(), None, None)

  def apply(requestURI: String) : RequestBuilder = apply(URI.create(requestURI))

  implicit def convertToRequest(b: RequestBuilder) : Request = b.toRequest
}
