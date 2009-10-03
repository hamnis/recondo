package net.hamnaberg.recondo


import org.junit.{Test, Assert}
import org.joda.time.{DateTimeZone, DateTime}

/**
 * @author <a href="mailto:erlend@hamnaberg.net">Erlend Hamnaberg</a>
 * @version $Revision: $
 */
class ResponseTest {

  @Test
  def testLastModified() {
    val time = new DateTime(DateTimeZone.UTC)    
    val headers = Headers() + Header.toHttpDate("Last-Modified", time)
    val response = new Response(Status.OK, headers, None)
    Assert.assertEquals(Some(time.withMillisOfSecond(0)), response.lastModified)
  }


  @Test
  def testParsedEtag() {
    val headers = Headers() + Header("ETag" -> new Tag("123").format)
    val response = new Response(Status.OK, headers, None)
    Assert.assertEquals(Some(new Tag("123")), response.ETag)
  }

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
    val response = new Response(Status.OK, Headers() + Header("Allow", "GET, PUT, POST"), None)
    Assert.assertFalse(response.allowedMethods.isEmpty)
    Assert.assertEquals(Set(Method.GET, Method.PUT, Method.POST), response.allowedMethods)
  }
}