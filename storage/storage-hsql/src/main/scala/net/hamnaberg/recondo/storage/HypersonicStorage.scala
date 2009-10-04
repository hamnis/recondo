package net.hamnaberg.recondo.storage

import java.net.URI
import net.hamnaberg.recondo.core.{Key, Storage}
import net.hamnaberg.recondo.{Response, Request}
import java.io.File

/**
 * @author <a href="mailto:erlend@escenic.com">Erlend Hamnaberg</a>
 * @version $Revision: $
 */
class HypersonicStorage(path: File) extends Storage {
  
  def clear() = null

  protected def invalidate(key: Key) = null

  protected def insertImpl(key: Key, response: Response) = null

  def invalidate(uri: URI) = null

  def update(request: Request, response: Response) = null

  def get(request: Request) = null
}