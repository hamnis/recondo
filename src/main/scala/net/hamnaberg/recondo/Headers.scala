package net.hamnaberg.recondo

import collection.mutable.MultiMap
import scala.Option


/**
 * @author <a href="mailto:erlend@escenic.com">Erlend Hamnaberg</a>
 * @version $Revision: #5 $ $Date: 2008/09/15 $
 */

class Headers(headers : Header*) {

  def getFirstHeader(name : String) : Option[Header] = {
    headers.find(_._1 == name)
  }
  
  def getHeaders(name : String) : Seq[Header] = {
    headers.filter(_._1 == name)
  }

  /*def add(name : String, value : String) : Unit = {
    headers.add(new Header(name, value))
  }*/

  
}