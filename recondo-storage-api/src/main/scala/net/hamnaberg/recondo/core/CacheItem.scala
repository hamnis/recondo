package net.hamnaberg.recondo.core


import org.joda.time.{Seconds, DateTime}
import net.hamnaberg.recondo._

/**
 * @author <a href="mailto:erlend@hamnaberg.net">Erlend Hamnaberg</a>
 * @version $Revision: $
 */
class CacheItem(val response: Response, val cacheTime: DateTime) {
  lazy val ttl = CacheItem.calculateTTL(response.headers, 0) 

  def stale = (response.hasPayload && !response.payloadAvailable) || (ttl - age <= 0)

  private def age = Seconds.secondsBetween(cacheTime, new DateTime).getSeconds
}

object CacheItem {
  def apply(response: Response) = new CacheItem(response, new DateTime)

  def calculateTTL(headers: Headers, default: Int): Int = {
    if (headers.contains(HeaderConstants.CACHE_CONTROL)) {
      val header = headers.first(HeaderConstants.CACHE_CONTROL)
      if (header.directives contains "max-age") {
        (header.directives("max-age") getOrElse "0").toInt
      }
      else {
        0
      }
    }
    else if (headers contains(HeaderConstants.EXPIRES)) {
      val now = new DateTime()
      val expires = Header.fromHttpDate(headers.first(HeaderConstants.EXPIRES)).getOrElse(now)
      val date = Header.fromHttpDate(headers.first(HeaderConstants.DATE)).getOrElse(now)
      Seconds.secondsBetween(date, expires).getSeconds
    }
    else {
      default
    }
  }
}