package net.hamnaberg.recondo.auth

import net.hamnaberg.recondo.Credentials

/**
 * @author <a href="mailto:erlend@hamnaberg.net">Erlend Hamnaberg</a>
 * @version $Revision: $
 */
trait CredentialsProvider {
  def provide() : Option[Credentials]
}

class NoProvider extends CredentialsProvider {
  def provide() = None
}