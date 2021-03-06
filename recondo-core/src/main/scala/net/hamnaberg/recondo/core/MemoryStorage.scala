package net.hamnaberg.recondo.core


import java.io.{ByteArrayInputStream, InputStream}
import java.net.URI
import collection.mutable.SynchronizedMap
import collection.mutable.HashMap
import net.hamnaberg.recondo.{Response, Request}
import net.hamnaberg.recondo.payload.ByteArrayPayload

/**
 * @author <a href="mailto:erlend@hamnaberg.net">Erlend Hamnaberg</a>
 * @version $Revision: $
 */
//TODO: Should probably use a different set of locking here. synchronization may be very slow 
class MemoryStorage extends Storage {
  val map = new HashMap[Key, CacheItem]() with SynchronizedMap[Key, CacheItem]
  
  def clear() = map.clear

  protected def invalidate(key: Key) = map -= key

  def invalidate(uri: URI) {
    val iterable = map.filter(_._1.uri == uri).map(_._1)
    map --= iterable
  }

  def update(request: Request, response: Response) ={
    val key = Key(request, response)
    insertImpl(key, response)
  }

  def insertImpl(key: Key, response: Response) = {
    val payload = response.payload match {
      case Some(p) if (p.isInstanceOf[ByteArrayPayload]) => Some(p)
      case Some(p) => Some(new ByteArrayPayload(p.inputStream, p.MIMEType))
      case None => None
    }
    val res = CacheItem(new Response(response.status, response.headers, payload))
    map += key -> res
    res.response
  }

  def get(request: Request) = map.find(x => x._1.uri == request.uri && x._1.vary.matches(request)).map(_._2)


  def size = map.size
}