package net.hamnaberg.recondo

/**
 * @author <a href="mailto:erlend@hamnaberg.net">Erlend Hamnaberg</a>
 * @version $Revision : #5 $ $Date: 2008/09/15 $
 */

sealed case class Method(val method : String);

object Method {
  object DELETE extends Method("DELETE");
  object GET extends Method("GET");
  object HEAD extends Method("HEAD");
  object POST extends Method("POST");
  object PUT extends Method("PUT");
  object TRACE extends Method("TRACE")
}
