package net.hamnaberg.recondo.core

import java.util.concurrent.atomic.AtomicLong
import javax.management.MXBean

/**
 * @author <a href="mailto:erlend@hamnaberg.net">Erlend Hamnaberg</a>
 * @version $Revision: $
 */
@MXBean
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

  def getHits() = hits.get

  def getMisses() = misses.get

  def getHitRatio = getMisses / (getMisses + getHits).asInstanceOf[Double]

  override def toString = "Stats[hits: %s, misses: %s]".format(hits.intValue, misses.intValue)
}