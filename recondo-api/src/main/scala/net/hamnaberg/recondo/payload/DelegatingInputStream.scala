package net.hamnaberg.recondo.payload


import java.io.InputStream

/**
 * @author <a href="mailto:erlend@hamnaberg.net">Erlend Hamnaberg</a>
 * @version $Revision: $
 */

class DelegatingInputStream(val stream: InputStream) extends InputStream {
  def read = stream.read;

  override def close = stream.close();

  override def reset = stream.reset()

  override def read(b: Array[Byte], off: Int, len: Int) = stream.read(b, off, len)

  override def read(b: Array[Byte]) = stream.read(b)

  override def markSupported = stream.markSupported

  override def available = stream.available

  override def mark(readlimit: Int) = stream.mark(readlimit)

  override def skip(n: Long) = stream.skip(n)
}