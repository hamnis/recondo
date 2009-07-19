package net.hamnaberg.recondo.mutable

import java.net.URI
import org.junit.{Assert, Test}
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
    Assert.assertEquals(URI.create("foo"), request.uri)
  }
}