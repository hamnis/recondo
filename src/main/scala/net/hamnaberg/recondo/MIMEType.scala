
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
    error("Not implemented")
  }

  def matches(mimeType : MIMEType) : Boolean = {
    error("Not implemented")
  }
}

object MIMEType {
  def apply(mimeType : String) = {
    val mime = new MimeType(mimeType)    
    val params = Collections.list(mime.getParameters.getNames).
            asInstanceOf[java.util.List[String]].
            toList.map(name =>re
              new Parameter(name, mime.getParameters.get(name)))
    new MIMEType(mime.getPrimaryType, mime.getSubType, params)
  }
}