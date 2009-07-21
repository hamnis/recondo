package net.hamnaberg.recondo

import java.net.URI
import org.junit.{Assert, Test}
import org.mockito.Mockito._

/**
 * @author <a href="mailto:erlend@hamnaberg.net">Erlend Hamnaberg</a>
 * @version $Revision : $
 */
class RequestBuilderTest {

  @Test
  def testConstruct() {
    val request = RequestBuilder(URI.create("foo"))
    Assert.assertEquals(Method.GET, request.method)
    val request2 = RequestBuilder("foo")
    Assert.assertEquals(Method.GET, request.method)
    Assert.assertEquals(Headers(), request.headers)
    Assert.assertEquals(URI.create("foo"), request.uri)
  }

  @Test
  def testCredentials() {
    val request = RequestBuilder(URI.create("foo"))
    val credentials = mock(classOf[Credentials])
    val req2 = request.setCredentials(credentials)
    Assert.assertNotSame(request, req2)
    Assert.assertEquals(None, request.credentials)
    Assert.assertEquals(Some(credentials), req2.credentials)
  }

  @Test
  def testConditionals() {
    val request = RequestBuilder(URI.create("foo"))
    val conditionals = Conditionals()
    val req2 = request.setCondtionals(conditionals)
    Assert.assertNotSame(request, req2)
    Assert.assertEquals(request.conditionals, req2.conditionals)
  }

  @Test
  def testPayload() {
    val request = RequestBuilder(URI.create("foo"))
    val payload = mock(classOf[Payload])
    val req2 = request.setPayload(payload)
    Assert.assertNotSame(request, req2)
    Assert.assertEquals(None, request.payload)
    Assert.assertEquals(Some(payload), req2.payload)
  }
}