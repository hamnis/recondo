package net.hamnaberg.recondo

/**
 * @author <a href="mailto:erlend@escenic.com">Erlend Hamnaberg</a>
 * @version $Revision: $
 */

trait PayloadContainer {
  def payload : Option[Payload]
  def hasPayload : Boolean = {
    payload match {
      case Some(p) => true
      case _ => false
    }
  }  
}