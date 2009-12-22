package net.hamnaberg.recondo.auth

import org.apache.commons.codec.binary.Base64
import net.hamnaberg.recondo._

/**
 * @author <a href="mailto:erlend@hamnaberg.net">Erlend Hamnaberg</a>
 * @version $Revision : $
 */
trait AuthStrategy {
  def supports(scheme: AuthScheme): Boolean

  def prepare(request: Request, scheme: AuthScheme): Request
}

class BasicAuthStrategy extends AuthStrategy {
  def prepare(request: Request, scheme: AuthScheme) = {
    prepare(request, request.credentials, false)
  }

  def prepareUsingProxy(request: Request, credentials: Credentials, scheme: AuthScheme) = {
    prepare(request, Some(credentials), true)
  }

  private def prepare(request: Request, credentials: Option[Credentials], proxy: Boolean) = {
    credentials match {
      case Some(UsernamePasswordCredentials(u, p)) => {
        val headervalue = "Basic " + new String(Base64.encodeBase64("%s:%s".format(u, p).getBytes("UTF-8")))
        if (proxy) {
          request.headers(request.headers + Header("Proxy-Authorization" -> headervalue))
        }
        else {
          request.headers(request.headers + Header("Authorization" -> headervalue))
        }
      }
      case _ => request
    }
  }


  def supports(scheme: AuthScheme) = "basic".equalsIgnoreCase(scheme.method)
}