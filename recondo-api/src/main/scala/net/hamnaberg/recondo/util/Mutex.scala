package net.hamnaberg.recondo.util

import java.util.concurrent.locks.ReentrantLock

/**
 * @author <a href="mailto:erlend@hamnaberg.net">Erlend Hamnaberg</a>
 * @author <a href="mailto:erik@mogensoft.net">Erik Mogensen</p>
 * @version $Revision : $
 */
class Mutex[T] {
  private var locks = Set[T]()
  private val lock = new ReentrantLock
  private val condition = lock.newCondition

  def acquire(obj: T) {
    lock.lock
    try {
      while (locks.contains(obj)) {
        try {
          condition.await
        }
        catch {
          case e: InterruptedException => Thread.currentThread.interrupt
        }
        locks += obj
      }
    }
    finally {
      lock.unlock
    }
  }

  def release(obj: T) {
    lock.lock
    try {
      if (locks.contains(obj)) {
        locks -= obj
        condition.signal
      }
    }
    finally {
      lock.unlock
    }
  }
}