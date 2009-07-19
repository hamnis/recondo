package net.hamnaberg.recondo.mutable

import java.net.URI
import org.junit.{Assert, Test}
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
    Assert.assertEquals(URI.create("foo"), request.uri)
  }
}