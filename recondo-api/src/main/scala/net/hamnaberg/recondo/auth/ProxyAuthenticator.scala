package net.hamnaberg.recondo.auth

import net.hamnaberg.recondo.{Response, Request}

/**
 * @author <a href="mailto:erlend@hamnaberg.net">Erlend Hamnaberg</a>
 * @version $Revision : $
 */
trait ProxyAuthenticator {
  def prepare(request: Request, response: Response): Request

  def preparePreemptively(request: Request): Request
}