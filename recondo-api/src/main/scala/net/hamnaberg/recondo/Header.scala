package net.hamnaberg.recondo


import java.util.Locale
import org.joda.time.format.{DateTimeFormat}
import org.joda.time.{DateTime, DateTimeZone}

/**
 * @author <a href="mailto:erlend@hamnaberg.net">Erlend Hamnaberg</a>
 * @version $Revision : #5 $ $Date: 2008/09/15 $
 */
final case class Header(name: String, value: String) {
  lazy val directives: Map[String, Option[String]] = parseValue(value)

  override def toString() = name + ": " + value

  private def parseValue(value: String): Map[String, Option[String]] = {
    val foo = value.split(",").toList map {
      x => {
        val trimmed = x.trim
        val directiveparts = trimmed.split("=", 2)
        if (directiveparts.length == 2) directiveparts(0) -> Some(directiveparts(1)) else directiveparts(0) -> None
      }
    }
    Map() ++ foo
  }


  override def hashCode = {
    var hc = 31 + name.toLowerCase(Locale.ENGLISH).hashCode;
    hc += 31 * value.hashCode
    hc
  }


  override def equals(obj: Any) = {
    if (obj == null) {
      false;
    }
    if (obj.isInstanceOf[Header]) {
      false;
    }
    val h = obj.asInstanceOf[Header]
    if (!name.equalsIgnoreCase(h.name)) {
      false;
    }
    if (!value.equals(h.name)) {
      false
    }
    true
  }
}

object Header {
  private val PATTERN_RFC1123 = "EEE, dd MMM yyyy HH:mm:ss 'GMT'";

  def apply(tuple: Tuple2[String, String]) = {
    new Header(tuple._1, tuple._2)
  }

  implicit def dateHeader(header: Tuple2[String, DateTime]) = {
    toHttpDate(header._1, header._2)
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