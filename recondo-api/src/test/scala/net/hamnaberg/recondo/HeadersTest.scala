package net.hamnaberg.recondo


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
    Assert.assertEquals("Headers were not equal", h eq Headers() ++ h)
    Assert.assertEquals("Headers were not equal", int2Integer(h.hashCode) eq int2Integer((Headers() ++ h).hashCode))
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
}