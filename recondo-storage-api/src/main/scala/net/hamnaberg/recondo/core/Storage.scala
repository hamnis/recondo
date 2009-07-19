package net.hamnaberg.recondo.core


import java.net.URI

/**
 * @author <a href="mailto:erlend@hamnaberg.net">Erlend Hamnaberg</a>
 * @version $Revision : $
 */
trait Storage {
  def get(request: Request): CacheItem

  def put(key: Key, item: CacheItem)

  def invalidate(uri: URI)

  def invalidate(key: Key, item: CacheItem)

  def clear()
}