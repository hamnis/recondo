package net.hamnaberg.recondo.payload


import java.io.{FileInputStream, File}
import org.apache.commons.io.input.ClosedInputStream
import net.hamnaberg.recondo.{MIMEType, Payload}

/**
 * @author <a href="mailto:erlend@hamnaberg.net">Erlend Hamnaberg</a>
 * @version $Revision: $
 */
case class FilePayload(val file: File, mimeType: MIMEType) extends Payload {
  
  val MIMEType = mimeType;

  def inputStream = if (available) new FileInputStream(file) else ClosedInputStream.CLOSED_INPUT_STREAM

  def available = file.exists && file.canRead
}