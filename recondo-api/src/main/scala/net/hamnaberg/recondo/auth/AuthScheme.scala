package net.hamnaberg.recondo.auth

import java.util.Locale
import net.hamnaberg.recondo.{Header}
/**
 * @author <a href="mailto:erlend@hamnaberg.net">Erlend Hamnaberg</a>
 * @version $Revision: $
 */
class AuthScheme(val header:Header) {
  require(header.name.toLowerCase(Locale.ENGLISH) match {
    case "www-authenticate"|"proxy-authenticate" => true
    case _ => false
  })
  val method = parseMethod(header.value)
  val directives = new Header(header.name, header.value.substring(header.value.indexOf(" ") + 1)).directives
  val realm = directives.get("realm").flatMap(x => x).getOrElse(error("No realm, wrong header? " + header))

  private def parseMethod(value: String) = value.substring(0, value.indexOf(" "))


  override def toString = "Method: %s, realm: %s, headerValue: %s".format(method, realm, header.value)
}
