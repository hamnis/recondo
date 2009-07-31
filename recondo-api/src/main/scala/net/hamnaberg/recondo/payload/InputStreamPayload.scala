package net.hamnaberg.recondo.payload


import java.io.InputStream

/**
 * @author <a href="mailto:erlend@hamnaberg.net">Erlend Hamnaberg</a>
 * @version $Revision: $
 */

class InputStreamPayload(s: InputStream, val mimeType: MIMEType) extends Payload {
  var avail = false
  val stream = new DelegatingInputStream(s) {
    override def read(b: Array[Byte]) = {
      if (avail) avail = false;
      super.read(b)
    }

    override def read(b: Array[Byte], off: Int, len: Int) = {
      if (avail) avail = false;
      super.read(b, off, len)
    }

    override def read = {
      if (avail) avail = false;
      super.read
    }
  };
  
  def getMIMEType() = mimeType

  def getInputStream() = stream

  def isAvailable = avail
}