package net.hamnaberg.recondo

/**
 * @author <a href="mailto:erlend@hamnaberg.net">Erlend Hamnaberg</a>
 * @version $Revision : #5 $ $Date: 2008/09/15 $
 */

object Method extends Enumeration {
  val DELETE = Value("DELETE");
  val GET = Value("GET");
  val HEAD = Value("HEAD");
  val POST = Value("POST");
  val PUT = Value("PUT");
  val TRACE = Value("TRACE")
}
