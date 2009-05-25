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
    Assert.assertEquals(0, mimeType.parameters.size)
  }
  
  @Test
  def testSimpleEquals() {
    val mimeType = MIMEType("application/xml")
    Assert.assertEquals(mimeType, APPLICATION_XML)
    Assert.assertEquals(0, mimeType.parameters.size)
  }
  
  @Test
  def testNotSoSimple() {
    val mimeType = MIMEType("application/xml;type=session")
    Assert.assertEquals("application", mimeType.mainType)
    Assert.assertEquals("xml", mimeType.subType)
    Assert.assertEquals(1, mimeType.parameters.size)
  }
}