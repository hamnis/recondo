package net.hamnaberg.recondo


import java.io.{InputStream}

/**
 * @author <a href="mailto:erlend@hamnaberg.net">Erlend Hamnaberg</a>
 * @version $Revision: #5 $ $Date: 2008/09/15 $
 */
trait Payload {
  def withInputStream[A]()(f: InputStream => A) {
    val is = inputStream()
    try {
      f(is)
    }
    finally {
      is.close()
    };
  }
  
  def inputStream() : InputStream
  def MIMEType : MIMEType
  def isAvailable : Boolean
}