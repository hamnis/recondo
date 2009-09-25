package net.hamnaberg.recondo.core


import java.net.URI
import net.hamnaberg.recondo.{Response, Request}

/**
 * @author <a href="mailto:erlend@hamnaberg.net">Erlend Hamnaberg</a>
 * @version $Revision : $
 */
trait Storage {
  def get(request: Request): Option[CacheItem]

  def insert(request: Request, response: Response): Response = {
    val key = Key(request, response)
    invalidate(key)
    insertImpl(key, response)
  }

  def update(request: Request, response: Response): Response

  def invalidate(uri: URI): Unit

  protected def insertImpl(key: Key, response: Response): Response

  protected def invalidate(key: Key): Unit

  def clear(): Unit
}