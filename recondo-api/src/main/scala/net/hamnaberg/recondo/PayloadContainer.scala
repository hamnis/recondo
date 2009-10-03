package net.hamnaberg.recondo

/**
 * @author <a href="mailto:erlend@hamnaberg.net">Erlend Hamnaberg</a>
 * @version $Revision: $
 */

trait PayloadContainer {

  def payload : Option[Payload]

  def hasPayload : Boolean = payload.map(x => true).getOrElse(false)

  def payloadAvailable: Boolean = payload.map(_.available).getOrElse(false)
}