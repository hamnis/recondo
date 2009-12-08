package net.hamnaberg.recondo.auth

import net.hamnaberg.recondo.{Request, Response}

/**
 * @author <a href="mailto:erlend@hamnaberg.net">Erlend Hamnaberg</a>
 * @version $Revision : $
 */
trait AuthStrategy {
  def supports(scheme: Scheme): Boolean

  def prepare(request: Request, scheme: Scheme): Request
}