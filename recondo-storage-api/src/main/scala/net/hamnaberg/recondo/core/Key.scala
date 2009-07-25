package net.hamnaberg.recondo.core


import java.net.URI

/**
 * @author <a href="mailto:erlend@hamnaberg.net">Erlend Hamnaberg</a>
 * @version $Revision : $
 */
case class Key(uri: URI, vary: Vary)

object Key {
  def apply(request: Request, response: Response): Key = {
    val vary = response.headers.firstHeaderValue(HeaderConstants.VARY) match {
      case Some(v) => createVary(v, request)
      case None => Vary()
    }
    new Key(request.uri, vary)
  }
  
  private def createVary(value: String, request: Request) = {
    var map = Map[String, String]()
    val parts = value.split(",")
    for (part <- parts) {
      request.headers.firstHeaderValue(part) match {
        case Some(x) => map += part -> x
        case None => map += part -> ""
      }
    }
    Vary(map)
  }
}