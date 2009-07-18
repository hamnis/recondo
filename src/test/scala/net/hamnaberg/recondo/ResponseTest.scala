package net.hamnaberg.recondo


import org.junit.{Test, Assert}

/**
 * @author <a href="mailto:erlend@escenic.com">Erlend Hamnaberg</a>
 * @version $Revision: $
 */

class ResponseTest {
  @Test
  def testEmptyAllowHeaders() {
    val response = new Response(Status.OK, Headers(), None)
    Assert.assertTrue(response.allowedMethods.isEmpty)
  }

  @Test
  def testGETAllowHeaders() {
    val response = new Response(Status.OK, Headers() + Header("Allow", "GET"), None)
    Assert.assertFalse(response.allowedMethods.isEmpty)
    Assert.assertEquals(Set(Method.GET), response.allowedMethods)
  }

  @Test
  def testMultipleAllowHeaders() {
    val response = new Response(Status.OK, Headers() + Header("Allow", "GET,PUT,POST"), None)
    Assert.assertFalse(response.allowedMethods.isEmpty)
    Assert.assertEquals(Set(Method.GET, Method.PUT, Method.POST), response.allowedMethods)
  }
}