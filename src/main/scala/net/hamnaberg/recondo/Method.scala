
package net.hamnaberg.recondo

/**
 * @author <a href="mailto:erlend@escenic.com">Erlend Hamnaberg</a>
 * @version $Revision: #5 $ $Date: 2008/09/15 $
 */

abstract sealed case class Method

case object HEAD extends Method;
case object GET extends Method;
case object TRACE extends Method;
case object POST extends Method;
case object PUT extends Method;
case object DELETE extends Method;
