package net.hamnaberg.recondo

import java.net.URI
import org.junit.{Assert, Test}
import org.mockito.Mockito._

/**
 * @author <a href="mailto:erlend@hamnaberg.net">Erlend Hamnaberg</a>
 * @version $Revision : $
 */
class RequestTest {

  @Test
  def testConstruct() {
    val request = Request(URI.create("foo"))
    Assert.assertEquals(Method.GET, request.method)
    val request2 = Request("foo")
    Assert.assertEquals(Method.GET, request.method)
    Assert.assertEquals(Headers(), request.headers)
    Assert.assertEquals(URI.create("foo"), request.uri)
  }

  @Test
  def testCredentials() {
    val request = Request(URI.create("foo"))
    val credentials = mock(classOf[Credentials])
    val req2 = request.credentials(credentials)
    Assert.assertNotSame(request, req2)
    Assert.assertEquals(None, request.credentials)
    Assert.assertEquals(Some(credentials), req2.credentials)
  }

  @Test
  def testConditionals() {
    val request = Request(URI.create("foo"))
    val conditionals = Conditionals()
    val req2 = request.conditionals(conditionals)
    Assert.assertNotSame(request, req2)
    Assert.assertEquals(request.conditionals, req2.conditionals)
  }

  @Test
  def testPayload() {
    val request = Request(URI.create("foo"))
    val payload = mock(classOf[Payload])
    val req2 = request.payload(payload)
    Assert.assertNotSame(request, req2)
    Assert.assertEquals(None, request.payload)
    Assert.assertEquals(Some(payload), req2.payload)
  }
}