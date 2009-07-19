package net.hamnaberg.recondo


import org.junit.{Assert, Test}

/**
 * @author <a href="mailto:erlend@hamnaberg.net">Erlend Hamnaberg</a>
 * @version $Revision: $
 */
class TagTest {

  @Test
  def testAllTag {
    val tag = new Tag("*", false)
    Assert.assertEquals(tag, Tag.ALL)
  }

  @Test
  def testAllFormatTag {
    val tag = new Tag("*", false)
    Assert.assertEquals(Tag.ALL.format, tag.format)
    Assert.assertEquals(Tag.ALL.format, "*")
  }

  @Test
  def testTag {
    val tag = new Tag("1234", false).format
    Assert.assertEquals("\"1234\"", tag)
  }

  @Test
  def testParseTag {
    val tag = Tag("\"1234\"")
    Assert.assertEquals(new Tag("1234", false), tag)
  }

  @Test
  def testParseWeakTag {
    val tag = Tag("W/\"1234\"")
    Assert.assertEquals(new Tag("1234", true), tag)
  }

  @Test
  def testWeakFormatTag {
    val tag = Tag("W/\"1234\"")
    Assert.assertEquals(new Tag("1234", true).format, tag.format)
    Assert.assertEquals("W/\"1234\"", tag.format)
  }

  @Test
  def testEmptyTag {
    Tag("\"\"")
  }
}