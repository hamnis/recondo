package net.hamnaberg.recondo.auth

import org.scalatest.FunSuite
import net.hamnaberg.recondo._

/**
 * @author <a href="mailto:erlend@escenic.com">Erlend Hamnaberg</a>
 * @version $Revision: $
 */

class DefaultAuthenticatorSuite extends FunSuite {
  test("test basic authentication without anything stored") {
    val authenticator = new DefaultAuthenticator()
    val headers = new Headers() + new Header(HeaderConstants.WWW_AUTHENTICATE, "Basic realm=123")
    val request = Request("http://test.com/123").credentials(new UsernamePasswordCredentials("123", "123"))
    val preparedrequest = authenticator.prepare(request, new Response(Status.OK, headers, None))
    assert(!preparedrequest.headers.isEmpty)
  }
}