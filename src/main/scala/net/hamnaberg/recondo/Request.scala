package net.hamnaberg.recondo

import java.net.URI


/**
 * @author <a href="mailto:erlend@escenic.com">Erlend Hamnaberg</a>
 * @version $Revision: #5 $ $Date: 2008/09/15 $
 */
class Request(requestURI : URI, Method : Method) {
  val headers = new Headers;
  
}

object Request {
  def apply(requestURI : URI) {
    new Request(requestURI, GET)
  }
  def apply(requestURI : String) {
    new Request(URI.create(requestURI), GET)
  }
}