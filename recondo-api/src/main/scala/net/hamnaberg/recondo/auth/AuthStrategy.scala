package net.hamnaberg.recondo.auth

import net.hamnaberg.recondo._
import org.apache.commons.codec.binary.{Base64}

/**
 * @author <a href="mailto:erlend@hamnaberg.net">Erlend Hamnaberg</a>
 * @version $Revision : $
 */
trait AuthStrategy {
  def supports(scheme: AuthScheme): Boolean

  def prepare(request: Request, scheme: AuthScheme): Option[Header]
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
      case None => None
      case Some(UsernamePasswordCredentials(u, p)) => {
        val headerValue = "Basic " + Base64.encodeBase64String("%s:%s".format(u, p).getBytes("UTF-8"))
        if (proxy) {
          Some(Header(HeaderConstants.PROXY_AUTHORIZATION -> headerValue))
        }
        else {
          Some(Header(HeaderConstants.AUTH -> headerValue))
        }
      }
    }
  }


  def supports(scheme: AuthScheme) = "basic".equalsIgnoreCase(scheme.method)
}