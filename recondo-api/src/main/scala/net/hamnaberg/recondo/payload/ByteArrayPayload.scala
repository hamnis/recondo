package net.hamnaberg.recondo.payload


import java.io.{InputStream, ByteArrayInputStream}
import org.apache.commons.io.IOUtils
import net.hamnaberg.recondo.{Payload, MIMEType}

/**
 * @author <a href="mailto:erlend@hamnaberg.net">Erlend Hamnaberg</a>
 * @version $Revision: $
 */

class ByteArrayPayload(stream: InputStream, m: MIMEType) extends Payload {
  val bytes = IOUtils.toByteArray(stream)
  val MIMEType = m;
  val available = true

  def inputStream = new ByteArrayInputStream(bytes)


}