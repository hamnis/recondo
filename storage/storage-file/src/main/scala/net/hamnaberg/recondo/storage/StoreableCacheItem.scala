package net.hamnaberg.recondo.storage

import net.hamnaberg.recondo.core.CacheItem
import net.hamnaberg.recondo._
import java.io.{File, ObjectInputStream, IOException, ObjectOutputStream}
import net.liftweb.json.JsonAST.{JField, JInt, JString, JValue}
import net.liftweb.json.JsonParser
import net.hamnaberg.recondo.payload.FilePayload
import org.joda.time.format.DateTimeFormat
import org.joda.time.DateTime

/**
 * @author <a href="mailto:erlend@hamnaberg.net">Erlend Hamnaberg</a>
 * @version $Revision: $
 */
@serializable
class StoreableCacheItem(response: Response, cacheTime: DateTime) extends CacheItem(response, cacheTime) with ToJSON {
  import net.liftweb.json.JsonDSL._

  def json = ("item" ->
              ("cache-time" -> DateTimeFormat.fullDateTime().print(cacheTime)) ~
              ("status" -> response.status.code) ~
              ("payload" -> response.payload.map{case FilePayload(f, t) => ("file" -> f.getAbsolutePath) ~ ("mime-type" -> t.toString)}) ~
              ("headers" -> response.headers.json))

  @throws(classOf[IOException])
  private def writeObject(out: ObjectOutputStream) {
      out.writeObject(toString)
  }

  @throws(classOf[IOException])
  @throws(classOf[ClassNotFoundException])
  private def readObject(in: ObjectInputStream) {
    val string = in.readObject.asInstanceOf[String]
    StoreableCacheItem.fromJson(JsonParser.parse(string))
  }
}

private object StoreableCacheItem extends FromJSON[StoreableCacheItem] {
  def apply(response: Response) = {
    new StoreableCacheItem(response, new DateTime)
  }

  private[recondo] def fromJson(json: JValue) = {
    val extractedDate = (json \ "item" \ "cache-time") match {
      case JString(value) => value
      case _ => error("Expected json string")
    }
    val extractedStatus = (json \ "item" \ "status") match {
      case JInt(value) => value.intValue
      case _ => error("Expected json integer")
    }
    val extractedPayload = (json \ "item" \ "payload").children match {
      case List(JField("file", JString(value)), JField("mime-type", JString(mimeType))) => Some(FilePayload(new File(value), MIMEType(mimeType)))
      case _ => None
    }
    val headers = Headers.fromJson(json \ "headers")
    val cacheTime = DateTimeFormat.fullDateTime.parseDateTime(extractedDate)
    new StoreableCacheItem(new Response(Status(extractedStatus), headers, extractedPayload), cacheTime)
  }
}