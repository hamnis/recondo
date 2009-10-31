package net.hamnaberg.recondo.examples

import org.joda.time.{DateTime, Seconds}
import java.util.concurrent.atomic.AtomicInteger
import net.hamnaberg.recondo.core.{MemoryStorage, Cache}
import net.hamnaberg.recondo.resolvers.HTTPClientResolver
import org.apache.http.impl.client.DefaultHttpClient
import net.hamnaberg.recondo.Request

/**
 * @author <a href="mailto:erlend@escenic.com">Erlend Hamnaberg</a>
 * @version $Revision: $
 */

object TooManyRequestsIssue {
  def main(args: Array[String]) {
    val count = new AtomicInteger(0)
    val storage = new MemoryStorage
    val resolver = new HTTPClientResolver(new DefaultHttpClient) {
      override def resolve(request: Request) = {
        count.incrementAndGet
        super.resolve(request)
      }
    }
    val cache = new Cache(storage, resolver)
    //val request = Request("http://www.google.com")
    val request = Request("http://localhost:8080/lm/cc,10/etag/foo")
    val start = new DateTime()
    var response = cache.execute(request)
    //todo: exposes problem with too many updates of cache...
    while(true) {
      val now = new DateTime
      if (Seconds.secondsBetween(start, now).getSeconds < 11) {
        cache.execute(request).consume()
        //new ResponsePrinter(request, cache.execute(request)).print()
      }
      else {
        println(count.intValue)
        println(storage.size)
        println(cache.stats)
        return;
      }
    }
    new ResponsePrinter(request, response).print()
  }
  
}