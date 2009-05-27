
package net.hamnaberg.recondo

/**
 * @author <a href="mailto:erlend@escenic.com">Erlend Hamnaberg</a>
 * @version $Revision: #5 $ $Date: 2008/09/15 $
 */

class Parameter(name : String, value : String) extends Tuple2(name, value) {

  def name() {
    _1
  }

  def value() {
    _2
  }


  override def toString() = name + " " + value
}

object Parameter {
  def apply(name : String, value : String) {
    new Parameter(name, value)
  }
  def apply(tuple : Tuple2[String, String]) {
    new Parameter(tuple._1, tuple._2)
  }
}