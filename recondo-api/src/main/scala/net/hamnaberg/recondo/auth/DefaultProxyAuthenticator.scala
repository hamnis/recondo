package net.hamnaberg.recondo.auth

import net.hamnaberg.recondo.{Credentials, HeaderConstants, Response, Request}

/**
 * @author <a href="mailto:erlend@hamnaberg.net">Erlend Hamnaberg</a>
 * @version $Revision : $
 */
class DefaultProxyAuthenticator(p: Option[CredentialsProvider]) extends AuthenticatorBase with ProxyAuthenticator {
  protected def strategies = List(new BasicAuthStrategy)

  protected val interestingHeaders = List(HeaderConstants.PROXY_AUTHENTICATE)
  private val provider = p.getOrElse(new NoProvider)
  private var cache = Map[AuthScheme, Credentials]()

  protected def getAuthHeaders(strategy: Option[AuthStrategy], request: Request, scheme: AuthScheme) = {
    strategy.map(x => x.prepareUsingProxy(request, getCredentials(scheme), scheme)).flatMap(x => x)
  }

  def preparePreemptively(request: Request) = prepareWithOptionalResponse(request, None)

  def prepare(request: Request, response: Response) = prepareWithOptionalResponse(request, Some(response))


  private def getCredentials(scheme:AuthScheme) = {
    var cred : Option[Credentials] = None
    if (cache contains scheme) {
      cred = cache.get(scheme)
    }
    else {
      cred = provider.provide
      cred.foreach(cache += scheme -> _)
    }
    cred
  }
}