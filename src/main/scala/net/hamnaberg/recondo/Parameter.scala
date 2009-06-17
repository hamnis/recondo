
package net.hamnaberg.recondo

/**
 * @author <a href="mailto:erlend@escenic.com">Erlend Hamnaberg</a>
 * @version $Revision: #5 $ $Date: 2008/09/15 $
 */

case class Parameter(name : String, value : String) {
  override def toString() = name + " " + value
}

object Parameter {
  def apply(tuple : Tuple2[String, String]) {
    new Parameter(tuple._1, tuple._2)
  }
}