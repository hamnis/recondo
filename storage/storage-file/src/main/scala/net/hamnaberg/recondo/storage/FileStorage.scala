package net.hamnaberg.recondo.storage


import java.io.File
import java.net.URI
import collection.mutable
import net.hamnaberg.recondo.core.{CacheItem, Key, Storage}
import net.hamnaberg.recondo.{Request, Response}
import net.hamnaberg.recondo.payload.FilePayload
import util.concurrent.locks.ReentrantReadWriteLock
import org.joda.time.{DateTime, DateTimeUtils}

/**
 * @author <a href="mailto:erlend@hamnaberg.net">Erlend Hamnaberg</a>
 * @version $Revision: $
 */
class FileStorage(val baseDirectory:File) extends Storage {
  val map = mutable.HashMap[Key, CacheItem]()
  val lock = ReentrantReadWriteLock(false)
  val read = lock.read
  val write = lock.write
  val manager = new FileManager(baseDirectory)

  val serializationFile = new File(baseDirectory, "persistent.ser")
  var lastSerializationTime = 0L
  var inserts = 0L;


  def clear() = write {
      map.foreach(x => clearPayload(x._2.response))
      map.clear
      serializationFile.delete
    }

  def update(request: Request, response: Response) = {
    val key = Key(request, response)
    insertImpl(key, response)
  }

  def insertImpl(key: Key, item: Response) = write {
    val payload = item.payload match {
      case Some(x) if (x.isInstanceOf[FilePayload]) => Some(x)
      case Some(x) => Some(new FilePayload(manager.createDataFile(key, x.inputStream), x.MIMEType))
      case None => None
    }
    val cacheItem = StoreableCacheItem(new Response(item.status, item.headers, payload))
    map += key -> cacheItem
    inserts += 1L;
    if (inserts % FileStorage.threshold == 0) {
      if (DateTimeUtils.currentTimeMillis > lastSerializationTime + FileStorage.PERSISTENT_TIMEOUT) {
        lastSerializationTime = DateTimeUtils.currentTimeMillis
        saveToDisk()
      }
    }
    cacheItem.response
  }

  def get(request: Request) = read {
    map.find(x => x._1.uri == request.uri && x._1.vary.matches(request)).map(_._2)
  }

  def invalidate(uri: URI) {
    write {
      val iterable = map.filter(_._1.uri == uri).map(_._1)
      iterable.foreach(x => clearPayload((map(x)).response))
      map -- iterable
    }
  }

  def invalidate(key: Key) {
    write {
      map get key match {
        case Some(x) => {
          clearPayload(x.response)
          map -= key
        }
        case None =>
      }
    }
  }

  private def clearPayload(response: Response) {
    response.payload match {
      case Some(FilePayload(x, _)) => x.delete
      case None =>
    }
  }

  private def saveToDisk() {

  }
}

object FileStorage {
  val threshold = 100L;
  val PERSISTENT_TIMEOUT = 60000L;
}


class StoreableCacheItem(response: Response, cacheTime: DateTime) extends CacheItem(response, cacheTime) {
  import net.liftweb.json.JsonDSL._
  
  override def json = {
    val json = super.json
    if (response.hasPayload) {
      json ~ ("payload" -> response.payload.map{case FilePayload(f, t) => ("file" -> f.getAbsolutePath) ~ ("mime-type" -> t.toString)}.get)
    }
    else {
      json
    }
  }
}
private object StoreableCacheItem {
  def apply(response: Response) = {
    new StoreableCacheItem(response, new DateTime)
  }
}