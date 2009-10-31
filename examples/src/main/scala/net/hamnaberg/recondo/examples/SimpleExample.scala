package net.hamnaberg.recondo.examples

import net.hamnaberg.recondo.core.{MemoryStorage, Cache}
import net.hamnaberg.recondo.resolvers._
import net.hamnaberg.recondo.{Method, Response, Request}
import org.apache.commons.io.IOUtils
import org.apache.http.impl.client.DefaultHttpClient


object SimpleExample {
  def main(args: Array[String]) {
    val storage = new MemoryStorage
    val resolver = new HTTPClientResolver(new DefaultHttpClient)
    val cache = new Cache(storage, resolver)
    //val request = Request("http://www.google.com")
    val request = Request("http://localhost:8080/lm/cc,10/etag/foo")
    val response = cache.execute(request)
    new ResponsePrinter(request, response).print()
  }
}

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