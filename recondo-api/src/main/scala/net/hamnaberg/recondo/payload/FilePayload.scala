package net.hamnaberg.recondo.payload


import java.io.{FileInputStream, File}
import org.apache.commons.io.input.ClosedInputStream

/**
 * @author <a href="mailto:erlend@hamnaberg.net">Erlend Hamnaberg</a>
 * @version $Revision: $
 */
case class FilePayload(val file: File, val mimeType: MIMEType) extends Payload {

  def getMIMEType = mimeType

  def inputStream = if (isAvailable) new FileInputStream(file) else ClosedInputStream.CLOSED_INPUT_STREAM

  def isAvailable = file.exists && file.canRead
}