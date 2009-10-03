package net.hamnaberg.recondo

/**
 * @author <a href="mailto:erlend@hamnaberg.net">Erlend Hamnaberg</a>
 * @version $Revision : #5 $ $Date: 2008/09/15 $
 */

sealed abstract case class Method(val method : String);

object Method {
  def apply(method: String) = method match {
    case DELETE.method => DELETE
    case GET.method => GET
    case HEAD.method => HEAD
    case OPTIONS.method => OPTIONS
    case POST.method => POST
    case PUT.method => PUT
    case TRACE.method => TRACE
    case undefined => error("No method named " + undefined)
  }

  object DELETE extends Method("DELETE");
  object GET extends Method("GET");
  object HEAD extends Method("HEAD");
  object OPTIONS extends Method("OPTIONS");
  object POST extends Method("POST");
  object PUT extends Method("PUT");
  object TRACE extends Method("TRACE")
}
