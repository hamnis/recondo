package net.hamnaberg.recondo.auth

import net.hamnaberg.recondo._
import net.hamnaberg.recondo.Host._

/**
 * @author <a href="mailto:erlend@hamnaberg.net">Erlend Hamnaberg</a>
 * @version $Revision : $
 */
class DefaultAuthenticator extends AuthenticatorBase with Authenticator {
  protected def strategies = List(new BasicAuthStrategy)
  protected val interestingHeaders = List(HeaderConstants.WWW_AUTHENTICATE)

  def preparePreemptively(request: Request) = prepareWithOptionalResponse(request, None)

  def prepare(request: Request, response: Response) = prepareWithOptionalResponse(request, Some(response))

  protected def getAuthHeaders(strategy: Option[AuthStrategy], request:Request, scheme:AuthScheme) = {
    strategy.map(x => x.prepare(request, scheme)).flatMap(x=>x)
  }

}