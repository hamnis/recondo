package net.hamnaberg.recondo

/**
 * @author <a href="mailto:erlend@hamnaberg.net">Erlend Hamnaberg</a>
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
  def isPayloadAvailable: Boolean = {
    payload match {
      case Some(p) => p.isAvailable
      case _ => false
    }
  }
}