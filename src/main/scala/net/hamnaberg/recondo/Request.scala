package net.hamnaberg.recondo

import java.net.URI


/**
 * @author <a href="mailto:erlend@escenic.com">Erlend Hamnaberg</a>
 * @version $Revision: $
 */
class Request(val uri : URI, val Method : Method) {
  val headers = new HeadersBuilder();
  var payload : Option[Payload] = None;
  
}

object Request {
  def apply(requestURI : URI) {
    new Request(requestURI, GET)
  }
  def apply(requestURI : String) {
    new Request(URI.create(requestURI), GET)
  }
}
