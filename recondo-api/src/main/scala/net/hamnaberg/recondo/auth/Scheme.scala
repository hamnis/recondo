package net.hamnaberg.recondo.auth

import net.hamnaberg.recondo.{HeaderConstants, Header}
import java.util.Locale

/**
 * @author <a href="mailto:erlend@hamnaberg.net">Erlend Hamnaberg</a>
 * @version $Revision: $
 */
class Scheme(header:Header) {
  require(header.name.toLowerCase(Locale.ENGLISH) match {
    case "www-authenticate"|"proxy-authenticate" => true
    case _ => false
  })
  val method = parseMethod(header.value)
  val directives = new Header(header.name, header.value.substring(header.value.indexOf(" ") + 1)).directives
  val realm = directives.get("realm").flatMap(x => x).getOrElse(error("No realm, wrong header? " + header))

  private def parseMethod(value: String) = value.substring(0, value.indexOf(" "))
}