package net.hamnaberg.recondo.util

import java.util.Locale

/**
 * @author <a href="mailto:erlend@hamnaberg.net">Erlend Hamnaberg</a>
 * @version $Revision: $
 */
private[util] class CaseInsensitiveString(val original: String) {

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