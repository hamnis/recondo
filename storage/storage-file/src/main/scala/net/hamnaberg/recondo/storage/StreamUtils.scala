package net.hamnaberg.recondo.storage


import java.io.{IOException, Closeable, InputStream, OutputStream}
import org.apache.commons.io.IOUtils

/**
 * @author <a href="mailto:erlend@hamnaberg.net">Erlend Hamnaberg</a>
 * @version $Revision: $
 */
object StreamUtils {

  def close(c:Closeable) {
    try {
      c.close
    }
    catch {
      case e:IOException =>
    }
  }

  def using[T <: Closeable](c: T)(f: T => Unit) = {
    try {
      f(c)
    }
    finally {
      close(c)
    }
  }

  def copy(is: InputStream, os: OutputStream) {
    IOUtils.copy(is, os);
  }
}