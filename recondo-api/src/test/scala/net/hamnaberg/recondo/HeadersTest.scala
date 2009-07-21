package net.hamnaberg.recondo


import org.joda.time.DateTime
import org.junit.{Assert, Test}

/**
 * @author <a href="mailto:erlend@hamnaberg.net">Erlend Hamnaberg</a>
 * @version $Revision : $
 */
class HeadersTest {

  @Test
  def testHeaders() {
    val h = Headers()
    Assert.assertTrue("Headers was not empty", h.isEmpty)
  }
  @Test
  def testEmptyEquals() {
    val h = Headers()
    Assert.assertEquals("Headers were not equal", h, Headers())
    Assert.assertEquals("Headers were not equal", h.hashCode, Headers().hashCode)
  }

  @Test
  def testSimpleEquals() {
    val h = Headers() ++ List(Header("Allow", "GET"), Header("Cache-Control", "private"), Header("Foo", "bar"))
    Assert.assertFalse("Headers were not equal", h eq Headers())
    Assert.assertFalse("Headers were not equal", int2Integer(h.hashCode) eq int2Integer(Headers().hashCode))
    Assert.assertEquals("Headers were not equal", h, Headers() ++ h)
    Assert.assertEquals("Headers were not equal", int2Integer(h.hashCode), int2Integer((Headers() ++ h).hashCode))
  }

  @Test
  def testAddHeader() {
    val h = Headers() + Header("Allow", "GET")
    Assert.assertFalse("Headers was not empty", h.isEmpty)
    Assert.assertEquals("Headers was not empty", 1, h.size)
  }

  @Test
  def testAddHeaders() {
    val h = Headers() ++ List(Header("Allow", "GET"))
    Assert.assertFalse("Headers was not empty", h.isEmpty)
    Assert.assertEquals("Headers was not empty", 1, h.size)
  }

  @Test
  def testAddMultipleHeaders() {
    val h : Headers = Headers() ++ List(Header("Allow", "GET"), Header("If-Match", new Tag("1234", false).format))    
    Assert.assertFalse("Headers was not empty", h.isEmpty)
    Assert.assertEquals("Headers was not empty", 2, h.size)
  }

  @Test
  def testAddAndRemoveHeaders() {
    val allow = Header("Allow", "GET")
    val h : Headers = Headers() ++ List(allow, Header("If-Match", new Tag("1234", false).format))
    Assert.assertFalse("Headers was not empty", h.isEmpty)
    Assert.assertEquals("Headers was not empty", 2, h.size)
    val h2 = h - allow
    Assert.assertFalse("Headers was not empty", h2.isEmpty)
    Assert.assertEquals("Headers had the wrong size", 1, h2.size)
  }
  
  @Test
  def testIsCacheable {
    val time = new DateTime(2009, 7, 21, 23, 0, 0, 0)
    Assert.assertFalse("Headers were cacheable", Headers().isCacheable)
    Assert.assertFalse("Headers were cacheable", (Headers() + Header("Cache-Control", "private")).isCacheable)
    Assert.assertFalse("Headers were cacheable", (Headers() + Header("Cache-Control", "max-age=50,no-store")).isCacheable)
    Assert.assertFalse("Headers were cacheable", (Headers() + Header("Cache-Control", "private,no-cache,no-store")).isCacheable)
    Assert.assertFalse("Headers were cacheable", (Headers() + Header("Pragma", "no-store")).isCacheable)
    Assert.assertFalse("Headers were cacheable", (Headers() + Header("Pragma", "no-cache")).isCacheable)
    Assert.assertFalse("Headers were cacheable", (Headers() + Header("Pragma", "no-cache,no-store")).isCacheable)
    Assert.assertFalse("Headers were cacheable", (Headers() + Header("ETag", new Tag("1244", false).format)).isCacheable)
    Assert.assertFalse("Headers were cacheable", (Headers() ++ List(Header("ETag", new Tag("1244", false).format), Header("Cache-Control", "no-store"))).isCacheable)
    Assert.assertFalse("Headers were cacheable", (Headers() ++ List(Header.toHttpDate("Last-Modified", time), Header.toHttpDate("Date", time.minusMinutes(4)))).isCacheable)
    Assert.assertFalse("Headers were cacheable", (Headers() ++ List(Header.toHttpDate("Expires", time), Header.toHttpDate("Date", time))).isCacheable)
    Assert.assertTrue("Headers were not cacheable", (Headers() ++ List(Header.toHttpDate("Expires", time.plusDays(1)), Header.toHttpDate("Date", time))).isCacheable)
    Assert.assertTrue("Headers were not cacheable", (Headers() ++ List(Header.toHttpDate("Last-Modified", time), Header.toHttpDate("Date", time))).isCacheable)
    Assert.assertTrue("Headers were not cacheable", (Headers() ++ List(Header.toHttpDate("Last-Modified", time), Header.toHttpDate("Date", time.plusMinutes(4)))).isCacheable)
    Assert.assertTrue("Headers were not cacheable", (Headers() ++ List(Header("ETag", new Tag("1244", false).format), Header("Cache-Control", "max-age=50"))).isCacheable)
    Assert.assertTrue("Headers were not cacheable", (Headers() + Header("Cache-Control", "max-age=50")).isCacheable)
  }
}