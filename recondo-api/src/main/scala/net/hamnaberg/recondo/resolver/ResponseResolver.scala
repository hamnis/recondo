package net.hamnaberg.recondo.resolver



/**
 * @author <a href="mailto:erlend@hamnaberg.net">Erlend Hamnaberg</a>
 * @version $Revision: #5 $ $Date: 2008/09/15 $
 */
trait ResponseResolver {
  def resolve(request : Request) : Response;
}