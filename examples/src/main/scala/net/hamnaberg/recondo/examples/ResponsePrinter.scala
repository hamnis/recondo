package net.hamnaberg.recondo.examples

import org.apache.commons.io.IOUtils
import net.hamnaberg.recondo.{Response, Request}

/**
 * @author <a href="mailto:erlend@hamnaberg.net">Erlend Hamnaberg</a>
 * @version $Revision: $
 */
class ResponsePrinter(val request: Request, val response: Response) {
  def print() {
    printRequestTop();
    printHeaders()
    printPayload()
  }

  private def printRequestTop() {
    println(request.method.method + " " + request.uri.getPath + " " + response.status)
    println
  }

  private def printHeaders() {
    response.headers.foreach(println(_))
    println
  }

  private def printPayload() {
    response.payload.foreach(p => println(IOUtils.toString(p.inputStream)))
  }
}