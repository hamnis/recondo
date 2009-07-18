package net.hamnaberg.recondo

/**
 * @author <a href="mailto:erlend@escenic.com">Erlend Hamnaberg</a>
 * @version $Revision: $
 */

trait Credentials {
  val identifier : String;
  def method : AuthMethod;
}

object Credentials {
  def apply(username : String, password : String, method : AuthMethod) = {
    new UsernamePasswordCredentials(username, password, method)
  }

  def toHeader(credentials : Credentials, authorizationHeader : Header) {
    
  }
}

case class UsernamePasswordCredentials(val identifier : String, val password : String, val method : AuthMethod) extends Credentials


abstract case class AuthMethod(val name : String);

case object BASIC extends AuthMethod("Basic");
case object DIGEST extends AuthMethod("Digest");