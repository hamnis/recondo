package net.hamnaberg.recondo


import java.util.Locale
import org.joda.time.format.{DateTimeFormat}
import org.joda.time.{DateTime, DateTimeZone}

/**
 * @author <a href="mailto:erlend@hamnaberg.net">Erlend Hamnaberg</a>
 * @version $Revision : #5 $ $Date: 2008/09/15 $
 */
case class Header(name: String, value: String) {
  lazy val directives: Map[String, String] = parseValue(value) // todo: change from option to empty string to denote empty value ? good idea?

  override def toString() = name + ": " + value

  private def parseValue(value: String): Map[String, String] = {
    val foo = value.split(",").toList map {
      x => {
        val trimmed = x.trim
        val directiveparts = trimmed.split("=", 2)
        if (directiveparts.length == 2) directiveparts(0) -> directiveparts(1) else directiveparts(0) -> ""
      }
    }
    Map() ++ foo
  }
}

object Header {
  private val PATTERN_RFC1123 = "EEE, dd MMM yyyy HH:mm:ss 'GMT'";

  def apply(tuple: Tuple2[String, String]) = {
    new Header(tuple._1, tuple._2)
  }

  def fromHttpDate(header: Header) = {
    val formatter = DateTimeFormat.forPattern(PATTERN_RFC1123).
            withZone(DateTimeZone.forID("UTC")).
            withLocale(Locale.US);
    formatter.parseDateTime(header.value);
  }

  def toHttpDate(name: String, time: DateTime) = {
    val formatter = DateTimeFormat.forPattern(PATTERN_RFC1123).
            withZone(DateTimeZone.forID("UTC")).
            withLocale(Locale.US);
    new Header(name, formatter.print(time))
  }
}

object HeaderConstants {
  val ACCEPT = "Accept";
  val ACCEPT_LANGUAGE = "Accept-Language";
  val ACCEPT_CHARSET = "Accept-Charset";
  val AGE = "Age";
  val ALLOW = "Allow";
  val CACHE_CONTROL = "Cache-Control"
  val CONTENT_TYPE = "Content-Type";
  val DATE = "Date"
  val ETAG = "ETag"
  val EXPIRES = "Expires"
  val IF_MATCH = "If-Match"
  val IF_MODIFIED_SINCE = "If-Modified-Since"
  val IF_NONE_MATCH = "If-None-Match"
  val IF_UNMODIFIED_SINCE = "If-Unmodified-Since"
  val LAST_MODIFIED = "Last-Modified"
  val LOCATION = "Location";
  val PRAGMA = "Pragma"
  val VARY = "Vary"
  val WARNING = "Warning";
}