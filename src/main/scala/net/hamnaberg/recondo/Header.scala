
package net.hamnaberg.recondo



/**
 * @author <a href="mailto:erlend@hamnaberg.net">Erlend Hamnaberg</a>
 * @version $Revision: #5 $ $Date: 2008/09/15 $
 */
case class Header(name : String, value : String) {
  require(name != null)
  require(value != null)
  val directives : Map[String, String] = parseValue(value)
  
  override def toString() = name + ": " + value

  def parseValue(value : String) : Map[String, String] = {
    val foo = value.split(",").toList map {
      x => {
        val trimmed = x.trim
        val directiveparts = trimmed.split("=", 2)
        if (directiveparts.length == 2) directiveparts(0) -> directiveparts(1) else directiveparts(0) -> null
      }
    }
    Map() ++ foo
  }
}

object Header {
  def apply(tuple : Tuple2[String, String]) = {
    new Header(tuple._1, tuple._2)
  }
}