package net.hamnaberg.recondo

import java.io.InputStream

/**
 * @author <a href="mailto:erlend@escenic.com">Erlend Hamnaberg</a>
 * @version $Revision: #5 $ $Date: 2008/09/15 $
 */

trait Payload {
  def getInputStream() : InputStream;
  def getMIMEType() : MIMEType;
}