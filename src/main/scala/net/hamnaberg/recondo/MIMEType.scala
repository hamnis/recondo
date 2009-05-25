
package net.hamnaberg.recondo


import java.util.Collections
import scala.collection.jcl.Conversions._
import javax.activation.{MimeTypeParseException, MimeType}

/**
 * @author <a href="mailto:erlend@escenic.com">Erlend Hamnaberg</a>
 * @version $Revision: #5 $ $Date: 2008/09/15 $
 */

class MIMEType (val mainType : String, val subType : String, val parameters : List[Parameter]) {

  def matches(mimeType : String) : Boolean ={
    matches(MIMEType(mimeType));
  }

  def matches(mimeType : MIMEType) : Boolean = {
    if (this == ALL)
      mainType.equals(mimeType.mainType) && (subType == mimeType.subType || subType == "*")
  }

  override def equals(obj: Any) : Boolean = {
    if (obj.isInstanceOf[MIMEType]) {
      val mimeType = obj.asInstanceOf[MIMEType]
      return mimeType.mainType == mainType && mimeType.subType == subType
    }
    return false
  }


  override def toString() : String = {
    mainType + "/" + subType
  }
}

object APPLICATION_XML extends MIMEType("application", "xml", Nil)
object APPLICATION_XHTML extends MIMEType("application", "xhtml+xml", Nil)
object TEXT_XML extends MIMEType("text", "xml", Nil)
object TEXT_HTML extends MIMEType("text", "html", Nil)
object ALL extends MIMEType("*", "*", Nil);

object MIMEType {
  def apply(mimeType : String) = {
    val mime = new MimeType(mimeType)
    val params = Collections.list(mime.getParameters.getNames).
            asInstanceOf[java.util.List[String]].
            toList.map(name =>
              new Parameter(name, mime.getParameters.get(name)))
    new MIMEType(mime.getPrimaryType, mime.getSubType, params)
  }

    def apply(mainType : String, subType : String) = {
      new MIMEType(mainType, subType, Nil)
    }
}