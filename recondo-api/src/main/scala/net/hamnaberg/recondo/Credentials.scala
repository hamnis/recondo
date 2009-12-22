package net.hamnaberg.recondo

/**
 * @author <a href="mailto:erlend@hamnaberg.net">Erlend Hamnaberg</a>
 * @version $Revision: $
 */

trait Credentials {
  val identifier : String  
}

object Credentials {
  def apply(username : String, password : String) = {
    new UsernamePasswordCredentials(username, password)
  }
}

abstract case class AuthMethod(val name : String);

case class UsernamePasswordCredentials(identifier : String, password : String) extends Credentials


