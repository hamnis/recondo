package net.hamnaberg.recondo.core


import org.joda.time.{Seconds, DateTime}

/**
 * @author <a href="mailto:erlend@hamnaberg.net">Erlend Hamnaberg</a>
 * @version $Revision: $
 */

class CacheItem(val response: Response, val cacheTime: DateTime) {
  def isStale(): Boolean = {
    if (response.hasPayload && !response.isPayloadAvailable) {
      true
    }
    else {
      val h = response.headers
      val now = new DateTime()
      val maxAgeResponse = calculateMaxAge(h.firstHeader(HeaderConstants.CACHE_CONTROL), now.getMillis, cacheTime.getMillis)
      //val maxAgeRequest = calculateMaxAge(request.headers.firstHeader(HeaderConstants.CACHE_CONTROL), now, cacheTime.getMillis)
      //if (maxAgeResponse <= 0) return true
      val expiry = h.firstHeader(HeaderConstants.EXPIRES) match {
        case Some(x) => {
          val expiryDate = Header.fromHttpDate(x)
          Seconds.secondsBetween(now, expiryDate)
        }
        case None => Seconds.seconds(-1)
      }
      if (maxAgeResponse.getSeconds != -1 && expiry.getSeconds != -1) {
        return maxAgeResponse.getSeconds <= 0
      }
      else if (maxAgeResponse.getSeconds > 0 && expiry.getSeconds == -1) {
        return false
      }
      else if (maxAgeResponse.getSeconds == -1 && expiry.getSeconds > 0) {
        return false
      }
      true
    }
  }

  private[this] def calculateMaxAge(cacheControlHeader: Option[Header], now: Long, cacheTime: Long):Seconds = {
    cacheControlHeader match {
      case Some(x) if (x.value contains "max-age") => {
        val ma = CacheItem.stringToLong(x.directives("max-age") getOrElse("0"))
        val age = now - cacheTime
        val remaining = ((ma * 1000L) - age) / 1000L
        Seconds.seconds(remaining.toInt)
      }
      case _ => Seconds.seconds(-1)
    }
  }
}

object CacheItem {
  def apply(response: Response) = new CacheItem(response, new DateTime())

  def stringToLong(s: String): Long = {
      try {
        java.lang.Long.parseLong(s)
      }
      catch {
        case e:NumberFormatException => -1L 
      }
  }
}