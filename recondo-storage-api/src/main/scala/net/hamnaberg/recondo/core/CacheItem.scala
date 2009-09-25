package net.hamnaberg.recondo.core


import org.joda.time.{Seconds, DateTime}
import net.hamnaberg.recondo.{Headers, HeaderConstants, Response, Header}

/**
 * @author <a href="mailto:erlend@hamnaberg.net">Erlend Hamnaberg</a>
 * @version $Revision: $
 */

class CacheItem(val response: Response, val cacheTime: DateTime) {
  lazy val ttl = CacheItem.calculateTTL(response.headers, 0) 
  def isStale = {
    if (response.hasPayload && !response.payloadAvailable) {
      true
    }
    else {
      ttl - Seconds.secondsBetween(cacheTime, new DateTime).getSeconds <= 0
    }
  }
}

object CacheItem {
  def apply(response: Response) = new CacheItem(response, new DateTime())

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
      val expires = Header.fromHttpDate(headers.first(HeaderConstants.EXPIRES))
      val date = Header.fromHttpDate(headers.first(HeaderConstants.DATE))
      Seconds.secondsBetween(date, expires).getSeconds
    }
    else {
      default
    }
  }
}