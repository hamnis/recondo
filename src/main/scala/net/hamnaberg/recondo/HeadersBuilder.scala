package net.hamnaberg.recondo

import collection.mutable

/**
 * @author <a href="mailto:erlend@escenic.com">Erlend Hamnaberg</a>
 * @version $Revision: #5 $ $Date: 2008/09/15 $
 */

class HeadersBuilder() {
  val headers = mutable.HashMap[String, List[Header]]()
  private[this] var commited = false

  def +=(header : Header) {
    addHeader(header)
  }

  def addHeader(header : Header) {
    headers get header.name match {
      case Some(value) => headers ++ header.name -> (header :: List(value))
      case None => headers ++ header.name -> List(header)
    }
  }

  /*def +=(header : Tuple2[String, String]) {
    addHeader(Header(header))
  }*/

  def toHeaders() : Headers ={
    new Headers(Map.empty ++ headers)
  }
}
