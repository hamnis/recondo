package net.hamnaberg.recondo


import java.nio.charset.Charset
import java.util.Locale

/**
 * @author <a href="mailto:erlend@escenic.com">Erlend Hamnaberg</a>
 * @version $Revision : $
 */

case class Preferences(accept: List[Preference[MIMEType]], acceptLang: List[Preference[Locale]], acceptCharset: List[Preference[Charset]]) {
  def addAccept(mimeType: MIMEType): Preferences = addAccept(mimeType, 1.0)

  def addAccept(mimeType: MIMEType, quality: Double): Preferences = Preferences(Preference(mimeType, quality) :: accept, acceptLang, acceptCharset)

  def addAcceptLanguage(locale: Locale): Preferences = addAcceptLanguage(locale, 1.0)

  def addAcceptLanguage(locale: Locale, quality: Double): Preferences = Preferences(accept, Preference(locale, quality) :: acceptLang, acceptCharset)

  def addAcceptCharset(charset: Charset): Preferences = addAcceptCharset(charset, 1.0)

  def addAcceptCharset(charset: Charset, quality: Double): Preferences = Preferences(accept, acceptLang, Preference(charset, quality) :: acceptCharset)

  private[recondo] def toHeaders() = {
    var headers = Headers()
    if (!accept.isEmpty) headers += Header(HeaderConstants.ACCEPT, accept.reverse.mkString(","))
    if (!acceptLang.isEmpty) headers += Header(HeaderConstants.ACCEPT_LANGUAGE, acceptLang.reverse.mkString(","))
    if (!acceptCharset.isEmpty) headers += Header(HeaderConstants.ACCEPT_CHARSET, acceptCharset.reverse.mkString(","))
    headers
  }
}

abstract case class Preference[T](pref: T, quality: Double) {
  assert(quality >= 0.0 && quality <= 1.0)
}

object Preference {
  def apply(pref: MIMEType, quality: Double) = new MIMETypePreference(pref, quality)

  def apply(pref: Locale, quality: Double) = new LocalePreference(pref, quality)

  def apply(pref: Charset, quality: Double) = new CharsetPreference(pref, quality)

  class MIMETypePreference(pref: MIMEType, quality: Double) extends Preference(pref, quality) {
    override def toString = if (quality == 1.0) pref.toString else pref.toString + "q=" + quality
  }

  class LocalePreference(pref: Locale, quality: Double) extends Preference(pref, quality) {
    override def toString = if (quality == 1.0) pref.getLanguage else pref.getLanguage + "q=" + quality
  }

  class CharsetPreference(pref: Charset, quality: Double) extends Preference(pref, quality) {
    override def toString = if (quality == 1.0) pref.toString else pref.toString + "q=" + quality
  }
}

object Preferences {
  def apply() = new Preferences(Nil, Nil, Nil)

  implicit def convertToHeaders(pref: Preferences): Headers = pref.toHeaders
}
