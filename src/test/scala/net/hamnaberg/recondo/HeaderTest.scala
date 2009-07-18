package net.hamnaberg.recondo


import org.junit.{Assert, Test}

/**
 * @author <a href="mailto:erlend@escenic.com">Erlend Hamnaberg</a>
 * @version $Revision : $
 */

class HeaderTest {
  @Test
  def testSimpleHeader {
    val header = new Header("foo", "foo")
    Assert.assertEquals(header, Header(Tuple2("foo", "foo")));
    Assert.assertEquals(header, Header("foo" -> "foo"));
  }

  @Test
  def testSimpleHeaderDirectives {
    val header = new Header("foo", "foo")
    Assert.assertEquals(header, Header(Tuple2("foo", "foo")));
    Assert.assertEquals(header, Header("foo" -> "foo"));
    Assert.assertEquals(header.directives, Map("foo" -> None))
  }

  @Test
  def testComplexHeaderDirectives {
    val headerValue = "private,max-age=12, s-max-age=213, snabel"
    val header = new Header("Cache-Control", headerValue)
    Assert.assertEquals(header, Header(Tuple2("Cache-Control", headerValue)));
    Assert.assertEquals(header, Header("Cache-Control" -> headerValue));
    Assert.assertEquals(header.directives, Map("private" -> None, "max-age" -> Some("12"), "s-max-age" -> Some("213"), "snabel" -> None))
  }
}