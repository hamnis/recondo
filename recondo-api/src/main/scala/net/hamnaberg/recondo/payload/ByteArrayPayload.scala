package net.hamnaberg.recondo.payload


import java.io.{InputStream, ByteArrayInputStream}
import org.apache.commons.io.IOUtils

/**
 * @author <a href="mailto:erlend@escenic.com">Erlend Hamnaberg</a>
 * @version $Revision: $
 */

class ByteArrayPayload(stream: InputStream, m: MIMEType) extends Payload {
  val bytes = IOUtils.toByteArray(stream)
  val mimeType = m;

  def getInputStream() = new ByteArrayInputStream(bytes)

  def getMIMEType() = mimeType

  def isAvailable = true
}