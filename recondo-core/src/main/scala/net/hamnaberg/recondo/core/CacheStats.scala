package net.hamnaberg.recondo.core

import java.util.concurrent.atomic.AtomicLong

/**
 * @author <a href="mailto:erlend@escenic.com">Erlend Hamnaberg</a>
 * @version $Revision: $
 */

class CacheStats {
  val hits = new AtomicLong
  val misses = new AtomicLong

  private[core] def hit() {
    hits.incrementAndGet
  }

  private[core] def miss() {
    misses.incrementAndGet
  }

  def reset() {
    hits.set(0L)
    misses.set(0L)
  }


  override def toString = "Stats[hits: %s, misses: %s]".format(hits.intValue, misses.intValue)
}