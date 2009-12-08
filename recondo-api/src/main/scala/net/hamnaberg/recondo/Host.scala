package net.hamnaberg.recondo

import java.net.URI

/**
 * @author <a href="mailto:erlend@hamnaberg.net">Erlend Hamnaberg</a>
 * @version $Revision : $
 */
class Host(val scheme:String, val hostname: String, val port: Int)

object Host {
  def apply(uri: URI) = {
    val scheme = uri.getScheme
    val port = scheme match {
      case null => error("No scheme defined, we cannot work with this")
      case "http" if (uri.getPort == -1) => 80 
      case "https" if (uri.getPort == -1) => 443
      case _ => uri.getPort
    }
    new Host(uri.getScheme, uri.getHost, port)
  }
}