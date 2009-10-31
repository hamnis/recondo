package net.hamnaberg.recondo

import java.util.Locale
import java.text.Collator

/**
 * @author <a href="mailto:erlend@hamnaberg.net">Erlend Hamnaberg</a>
 * @version $Revision: $
 */
case class CaseInsensitiveString(original: String) {

  override def equals(obj: Any) : Boolean = {
    if (obj == null) return false
    if (!obj.isInstanceOf[CaseInsensitiveString]) return false
    val n = obj.asInstanceOf[CaseInsensitiveString]
    if (!original.equalsIgnoreCase(n.original)) {
      return false
    }
    return true
  }

  override def hashCode = 31 * original.toLowerCase(Locale.ENGLISH).hashCode
}