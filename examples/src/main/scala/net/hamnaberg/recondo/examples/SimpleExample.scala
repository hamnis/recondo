package net.hamnaberg.recondo.examples

import net.hamnaberg.recondo.core.{MemoryStorage, Cache}
import net.hamnaberg.recondo.resolvers._
import net.hamnaberg.recondo.{Request}
import org.apache.http.impl.client.DefaultHttpClient
object SimpleExample {
  def main(args: Array[String]) {
    val cache = new Cache(new MemoryStorage, new HTTPClientResolver(new DefaultHttpClient))
    val request = Request("http://www.google.com/")
    var response = cache.execute(request)    
    new ResponsePrinter(request, response).print()
  }
}