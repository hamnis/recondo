
package net.hamnaberg.recondo


import java.util.Locale
import org.joda.time.format.{DateTimeFormat}
import org.joda.time.{DateTime, DateTimeZone}

/**
 * @author <a href="mailto:erlend@hamnaberg.net">Erlend Hamnaberg</a>
 * @version $Revision: #5 $ $Date: 2008/09/15 $
 */
case class Header(name : String, value : String) {
  require(name != null)
  require(value != null)
  val directives : Map[String, Option[String]] = parseValue(value)
  
  override def toString() = name + ": " + value

  def parseValue(value : String) : Map[String, Option[String]] = {
    val foo = value.split(",").toList map {
      x => {
        val trimmed = x.trim
        val directiveparts = trimmed.split("=", 2)
        if (directiveparts.length == 2) directiveparts(0) -> Some(directiveparts(1)) else directiveparts(0) -> None
      }
    }
    Map() ++ foo
  }
}

object Header {
  private val PATTERN_RFC1123 = "EEE, dd MMM yyyy HH:mm:ss 'GMT'";
  
  def apply(tuple : Tuple2[String, String]) = {
    new Header(tuple._1, tuple._2)
  }

  def fromHttpDate(header : Header) = {
    val formatter = DateTimeFormat.forPattern(PATTERN_RFC1123).
            withZone(DateTimeZone.forID("UTC")).
            withLocale(Locale.US);
    formatter.parseDateTime(header.value);
  }

  def toHttpDate(name : String, time : DateTime) = {
    val formatter = DateTimeFormat.forPattern(PATTERN_RFC1123).
            withZone(DateTimeZone.forID("UTC")).
            withLocale(Locale.US);
    new Header(name, formatter.print(time))
  }
}