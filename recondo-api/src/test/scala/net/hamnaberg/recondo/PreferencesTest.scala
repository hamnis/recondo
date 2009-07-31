package net.hamnaberg.recondo

import org.junit.Assert._
import org.junit.Test

/**
 * @author <a href="mailto:erlend@hamnaberg.net">Erlend Hamnaberg</a>
 * @version $Revision: $
 */

class PreferencesTest {
  @Test
  def testEmptyPreferences() {
    assertTrue(Preferences() match {
      case Preferences(List(), List(), List()) => true
      case _ => false
    })
  }

  @Test
  def testAcceptMimeTypePreferences() {
    val p = Preferences().addAccept(MIMEType("application/xml")).addAccept(MIMEType("text/uri-list"))
    assertFalse("Empty Preferences was equal to non empty",p == Preferences())
    assertEquals(Headers() + Header(HeaderConstants.ACCEPT, "application/xml,text/uri-list"), p.toHeaders)
  }
}
