package net.hamnaberg.recondo


import java.net.URI

/**
 * @author <a href="mailto:erlend@hamnaberg.net">Erlend Hamnaberg</a>
 * @version $Revision: $
 */
case class Request(uri : URI, method : Method, headers : Headers, credentials : Option[Credentials], payload : Option[Payload]) extends PayloadContainer

object Request {
  def apply(uri : URI) = {
    new Request(uri, Method.GET, Headers(), None, None)
  }
  def apply(uri : String) = {
    new Request(URI.create(uri), Method.GET, Headers(), None, None)
  }
}