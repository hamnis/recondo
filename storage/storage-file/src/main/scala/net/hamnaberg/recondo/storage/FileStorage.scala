package net.hamnaberg.recondo.storage


import core.{CacheItem, Key, Storage}
import java.io.File
import java.net.URI
import collection.mutable
import payload.FilePayload

/**
 * @author <a href="mailto:erlend@hamnaberg.net">Erlend Hamnaberg</a>
 * @version $Revision: $
 */
class FileStorage(val baseDirectory:File) extends Storage {
  val manager = new FileManager(baseDirectory)
  val map = new mutable.HashMap[Key, CacheItem]() with mutable.SynchronizedMap[Key, CacheItem]

  def clear() = {
    map.foreach(x => clearPayload(x._2.response))
    map.clear
  }

  private def clearPayload(response: Response) {
    response.payload match {
      case Some(FilePayload(x, _)) => x.delete
      case None => 
    }
  }

  def put(key: Key, item: Response) = {
    val payload = item.payload match {
      case Some(x) if (x.isInstanceOf[FilePayload]) => Some(x)
      case Some(x) => Some(new FilePayload(manager.createFile(key, x.getInputStream), x.getMIMEType))
      case None => None
    }
    val cacheItem = CacheItem(new Response(item.status, item.headers, payload))
    map += key -> cacheItem
    cacheItem.response
  }

  def get(request: Request) = map.find(x => x._1.uri == request.uri && x._1.vary.matches(request)).map(_._2)

  def invalidate(uri: URI) {
    val iterable = map.filter(_._1.uri == uri).map(_._1)
    iterable.foreach(x => clearPayload((map(x)).response))
    map -- iterable
  }

  def invalidate(key: Key) {
    map get key match {
      case Some(x) => {
        clearPayload(x.response)
        map -= key        
      }
      case None => 
    }
  }
}