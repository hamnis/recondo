package net.hamnaberg.recondo


import org.junit.{Assert, Test}

/**
 * @author <a href="mailto:erlend@escenic.com">Erlend Hamnaberg</a>
 * @version $Revision: #5 $ $Date: 2008/09/15 $
 */

class MIMETypeTest {

  @Test
  def testSimple() {
    val mimeType = MIMEType("application/xml")
    Assert.assertEquals("application", mimeType.mainType)
    Assert.assertEquals("xml", mimeType.subType)
  }
}