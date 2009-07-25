package net.hamnaberg.recondo.core


import org.joda.time.DateTime

/**
 * @author <a href="mailto:erlend@escenic.com">Erlend Hamnaberg</a>
 * @version $Revision: $
 */

class CacheItem(val response: Response, val cacheTime: DateTime) {
  //TODO: Implement
  def isStale = {
    false
  }
}

object CacheItem {
  def apply(response: Response) = new CacheItem(response, new DateTime())
}