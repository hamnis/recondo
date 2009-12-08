package net.hamnaberg.recondo.auth

import net.hamnaberg.recondo.{Request, Response}

/**
 * @author <a href="mailto:erlend@hamnaberg.net">Erlend Hamnaberg</a>
 * @version $Revision: $
 */
trait Authenticator {
  def prepare(request: Request, response: Response): Request

  def preparePreemptively(request: Request): Request
}