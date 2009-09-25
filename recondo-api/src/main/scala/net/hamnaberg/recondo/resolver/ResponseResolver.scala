package net.hamnaberg.recondo.resolver


import java.io.IOException
import net.hamnaberg.recondo.{Response, Request}

/**
 * @author <a href="mailto:erlend@hamnaberg.net">Erlend Hamnaberg</a>
 * @version $Revision: #5 $ $Date: 2008/09/15 $
 */
trait ResponseResolver {
  @throws(classOf[IOException])
  def resolve(request : Request) : Option[Response]
}