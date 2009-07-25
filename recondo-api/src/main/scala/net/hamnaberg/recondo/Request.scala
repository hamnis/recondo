package net.hamnaberg.recondo

import java.net.URI


/**
 * @author <a href="mailto:erlend@hamnaberg.net">Erlend Hamnaberg</a>
 * @version $Revision : $
 */
case class Request private[recondo](uri: URI,
                                    method: Method,
                                    headers: Headers,
                                    conditionals: Conditionals,
                                    credentials: Option[Credentials],
                                    payload: Option[Payload]
        ) extends PayloadContainer {

  def headers(h: Headers): Request = {
    new Request(uri, method, headers, conditionals, credentials, payload)
  }

  def conditionals(c: Conditionals): Request = {
    new Request(uri, method, headers, c, credentials, payload)
  }

  def credentials(c: Credentials):Request = {
    new Request(uri, method, headers, conditionals, Some(c), payload)
  }

  def payload(p: Payload) : Request = {
    new Request(uri, method, headers, conditionals, credentials, Some(p))
  }  
}

object Request {
  def apply(requestURI: URI) = new Request(requestURI, Method.GET, Headers(), Conditionals(), None, None)

  def apply(requestURI: String) : Request = apply(URI.create(requestURI))
}
