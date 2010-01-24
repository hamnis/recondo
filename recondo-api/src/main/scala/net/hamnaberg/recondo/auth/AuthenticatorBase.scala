package net.hamnaberg.recondo.auth

import net.hamnaberg.recondo.{Response, Header, Request, HeaderConstants}

/**
 * @author <a href="mailto:erlend@hamnaberg.net">Erlend Hamnaberg</a>
 * @version $Revision: $
 */
private[auth] abstract class AuthenticatorBase {
  private[this] val registry = new AuthRegistry

  protected def interestingHeaders: List[String]
  
  /**
   * The list is expected to be sorted with the most secure implementation first.
   * We will try to find a matching authentication strategy based on this list.
   */
  protected def strategies: List[AuthStrategy]

  protected def prepareWithOptionalResponse(request: Request, response: Option[Response]) = {
    val fromRegistry = registry.get(request.uri).map(as => findSupportingStrategyAndPrepare(request, as))
    lazy val fromResponse = getHeadersFromResponse(request, response)
    maybeAddHeaders(request, fromRegistry.getOrElse(fromResponse))
  }

  private def findSupportingStrategyAndPrepare(request: Request, scheme: AuthScheme) = {
    val supportedStrategy = strategies.find(_.supports(scheme))
    val authHeaders = getAuthHeaders(supportedStrategy, request, scheme)
    if (!registry.matches(request.uri)) {
      registry.register(request.uri, scheme)
    }
    authHeaders.toList
  }

  protected def getAuthHeaders(strategy: Option[AuthStrategy], request:Request, scheme:AuthScheme) : Option[Header]

  private def maybeAddHeaders(request: Request, headers: Iterable[Header]) = headers match {
    case List() => request
    case x => request.headers(request.headers ++ x)
  }

  private def getHeadersFromResponse(request: Request, response: Option[Response]) = {
    val schemes = response.map(r => r.headers.filter(x => interestingHeaders contains x.name).map(h => new AuthScheme(h))).getOrElse(List())
    schemes.flatMap(s => findSupportingStrategyAndPrepare(request, s))
  }
}