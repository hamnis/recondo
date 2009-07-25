package net.hamnaberg.recondo.core


import java.net.URI

/**
 * @author <a href="mailto:erlend@hamnaberg.net">Erlend Hamnaberg</a>
 * @version $Revision : $
 */
trait Storage {
  def get(request: Request): Option[CacheItem]

  def put(key: Key, item: CacheItem): Response

  def invalidate(uri: URI): Unit

  def invalidate(key: Key, item: CacheItem): Unit

  def clear(): Unit
}