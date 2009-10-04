package net.hamnaberg.recondo


import org.joda.time.DateTime
import HeaderConstants._
import net.liftweb.json.JsonAST.{JString, JArray, JField, JObject}

/**
 * TODO:
 * - comparison of strings are always done case insensitive.
 *
 */

/**
 * @author <a href="mailto:erlend@hamnaberg.net">Erlend Hamnaberg</a>
 * @version $Revision : #5 $ $Date: 2008/09/15 $
 */
class Headers(h: Map[String, List[String]]) extends Iterable[Header] with ToJSON {
  private[recondo] val headers = Map() ++ h

  def this() = this(Map());

  def first(name: String): Header = headers(name).map(e => new Header(name, e)).reverse.first

  def firstHeader(name: String): Option[Header] = getHeaders(name).reverse.firstOption

  def firstHeaderValue(name: String): Option[String] = {
    firstHeader(name).map(h => Some(h.value)).getOrElse(None)
  }

  def getHeaders(name: String): List[Header] = {
    headers.get(name).map(v => v.map(new Header(name, _))).getOrElse(Nil)    
  }

  override def isEmpty: Boolean = headers.isEmpty

  def size: Int = headers.size

  def contains(h: Header): Boolean = {
    getHeaders(h.name) contains h
  }

  def +(header: Header): Headers = {
    val heads = headers.get(header.name).getOrElse(Nil)
    if (!heads.contains(header.value)) {
      new Headers(headers + (header.name -> (header.value :: heads)))
    }
    else {
      this      
    }
  }

  def -(header: Header): Headers = {
    val x = headers.get(header.name).map(_ - header.value).getOrElse(Nil) 
    x match {
      case List() => new Headers(headers - header.name)
      case x => new Headers(headers + (header.name -> x))
    }
  }

  def -(headerName: String): Headers = {
    new Headers(headers - headerName)
  }
  def --(headerNames: Iterable[String]): Headers = {
    new Headers(headers -- headerNames)
  }

  def ++(heads: Iterable[Header]): Headers = heads.foldLeft(this){_ + _}

  def contains(name: String) = !getHeaders(name).isEmpty

  private[recondo] def asMap = headers;

  override def equals(obj: Any) = {
    if (obj.isInstanceOf[Headers]) {
      val h = obj.asInstanceOf[Headers]
      toList == h.toList
    }
    else {
      false
    }
  }

  override def hashCode = 31 * elements.toList.hashCode  

  def elements: Iterator[Header] = {
    val iterable = for{
      (x, y) <- headers
      z <- y
    } yield new Header(x, z)
    iterable.elements
  }

  private[recondo] def json = JObject(headers.toList.map{ case (name, values) => JField(name, JArray(values.map(JString)))})
  
  private[recondo] def isCacheable: Boolean = {
    if (contains(Header(VARY, "*"))) return false
    val interestingHeaderNames = Set(CACHE_CONTROL, EXPIRES, LAST_MODIFIED)
    val cacheableHeaders = new Headers(headers.filter(interestingHeaderNames contains _._1)).toList
    val dateHeaderValue = firstHeader(DATE).map(Header.fromHttpDate(_)).getOrElse(return false)

    for (h <- cacheableHeaders) {
      if (!analyzeCachability(h, dateHeaderValue)) {
        return false
      }
    }
    contains(LAST_MODIFIED) || contains(ETAG)
  }

  private def analyzeCachability(h: Header, dateHeaderValue: DateTime): Boolean = h match {
    case Header(CACHE_CONTROL, v) => {
      val expire = firstHeader(EXPIRES).map {analyzeCachability(_, dateHeaderValue)} getOrElse false
      (v.contains("max-age") || expire) && !(v.contains("no-store") || v.contains("no-cache"))
    }
    case Header(EXPIRES, _) => dateHeaderValue.isBefore(Header.fromHttpDate(h))
    case Header(LAST_MODIFIED, _) => {
      val lastModified = Header.fromHttpDate(h)
      dateHeaderValue.isAfter(lastModified) || dateHeaderValue.equals(lastModified)
    }
  }
}

object Headers {
  def apply(): Headers = {
    new Headers(Map.empty)
  }
}