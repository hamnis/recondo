
package net.hamnaberg.recondo


import java.util.Collections
import scala.collection.jcl.Conversions._
import javax.activation.{MimeTypeParseException, MimeType}

/**
 * @author <a href="mailto:erlend@hamnaberg.net">Erlend Hamnaberg</a>
 * @version $Revision: #5 $ $Date: 2008/09/15 $
 */

class MIMEType (val mainType : String, val subType : String, val parameters : List[Parameter]) {
  require(mainType != null)
  require(subType != null)
  require(parameters != null)
  
  def matches(mimeType : String) : Boolean ={
    matches(MIMEType(mimeType));
  }

  def matches(mimeType : MIMEType) : Boolean = {
    if (this == MIMEType.ALL)
      mainType.equals(mimeType.mainType) && (subType == mimeType.subType || subType == "*")
    else false
  }

  override def equals(obj: Any) : Boolean = obj match {
    case MIMEType(mainType, subType) =>
            this.mainType == mainType && this.subType == subType
    case _ => false
  }


  override def toString() : String = {
    mainType + "/" + subType
  }
}


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

  def unapply(mimeType : MIMEType) : Some[(String, String)] = {
    Some(mimeType.mainType, mimeType.subType)
  }

  object APPLICATION_XML extends MIMEType("application", "xml", Nil)
  object APPLICATION_XHTML extends MIMEType("application", "xhtml+xml", Nil)
  object TEXT_XML extends MIMEType("text", "xml", Nil)
  object TEXT_HTML extends MIMEType("text", "html", Nil)
  object ALL extends MIMEType("*", "*", Nil);
}