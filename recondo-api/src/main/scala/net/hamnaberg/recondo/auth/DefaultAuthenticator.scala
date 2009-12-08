package net.hamnaberg.recondo.auth

import net.hamnaberg.recondo.{Response, Request}

/**
 * @author <a href="mailto:erlend@hamnaberg.net">Erlend Hamnaberg</a>
 * @version $Revision: $
 */
class DefaultAuthenticator extends Authenticator {  
  private[this] val strategies = createStrategies;

  protected def createStrategies : List[AuthStrategy] = List()

  def preparePreemptively(request: Request) = null

  def prepare(request: Request, response: Response) = null
}