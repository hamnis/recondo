package net.hamnaberg.recondo


import org.junit.{Assert, Test}

/**
 * @author <a href="mailto:erlend@hamnaberg.net">Erlend Hamnaberg</a>
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
  def testEquals {
    val header = new Header("foo", "foo")
    val header2 = new Header("fOo", "foo")
    val header3 = new Header("FOO", "foo")
    val header4 = new Header("fOO", "foo")
    Assert.assertEquals(header, header2);
    Assert.assertEquals(header, header3);
    Assert.assertEquals(header, header4);
    Assert.assertEquals(header2, header3);
    Assert.assertEquals(header2, header4);
    Assert.assertEquals(header3, header4);
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