
package net.hamnaberg.recondo



/**
 * @author <a href="mailto:erlend@escenic.com">Erlend Hamnaberg</a>
 * @version $Revision: #5 $ $Date: 2008/09/15 $
 */
class Header(name : String, value : String, val directives : Map[String, String]) extends Tuple2(name, value) {
  def name() : String = {
    _1
  }

  def value() : String = {
    _2
  }


  override def toString() = name + ": " + value
}

object Header {
  def apply(name : String, value : String) {
    new Header(name, value, Map.empty)
  }
  def apply(tuple : Tuple2[String, String]) {
    new Header(tuple._1, tuple._2, Map.empty)
  }
}