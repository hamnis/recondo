
package net.hamnaberg.recondo

/**
 * @author <a href="mailto:erlend@escenic.com">Erlend Hamnaberg</a>
 * @version $Revision: #5 $ $Date: 2008/09/15 $
 */

abstract sealed case class Method(val value : String)

case object DELETE extends Method("DELETE");
case object GET extends Method("GET");
case object HEAD extends Method("HEAD");
case object POST extends Method("POST");
case object PUT extends Method("PUT");
case object TRACE extends Method("TRACE");
