
package net.hamnaberg.recondo



/**
 * @author <a href="mailto:erlend@escenic.com">Erlend Hamnaberg</a>
 * @version $Revision: #5 $ $Date: 2008/09/15 $
 */
case class Header(name : String, value : String) {
  override def toString() = name + ": " + value
}

object Header {
  def apply(tuple : Tuple2[String, String]) {
    new Header(tuple._1, tuple._2)
  }
}