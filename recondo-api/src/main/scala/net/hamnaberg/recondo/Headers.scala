package net.hamnaberg.recondo


import org.joda.time.DateTime
import HeaderConstants._

/**
 * TODO:
 * - make the headers a Map of String, String.
 * -- extends a map like I do in httpcache4j.
 * - comparison of strings are always done case insensitive.
 *
 */


/**
 * @author <a href="mailto:erlend@hamnaberg.net">Erlend Hamnaberg</a>
 * @version $Revision : #5 $ $Date: 2008/09/15 $
 */
class Headers(h: Map[String, List[Header]]) extends Iterable[Header] {
  private[this] val headers = Map() ++ h

  def this() = this(Map());

  def first(name: String): Header = headers(name).reverse.first

  def firstHeader(name: String): Option[Header] = getHeaders(name).reverse.firstOption

  def firstHeaderValue(name: String): Option[String] = {
    firstHeader(name) match {
      case Some(header) => Some(header.value);
      case None => None
    }
  }

  def getHeaders(name: String): List[Header] = {
    val h = headers get name
    h match {
      case Some(x) => x
      case None => Nil
    }
  }

  override def isEmpty: Boolean = headers.isEmpty

  def size: Int = headers.size

  def contains(h: Header): Boolean = {
    getHeaders(h.name) contains h
  }

  def +(header: Header): Headers = {
    val h = add(getHeaders(header.name), header)
    h match {
      case List() => this
      case x => {
        val heads = headers + (header.name -> x)
        new Headers(heads)
      }
    }
  }

  def -(header: Header): Headers = {
    val x = getHeaders(header.name) - header;
    x match {
      case List() => new Headers(headers - header.name)
      case x => new Headers(headers + (header.name -> x))
    }
  }

  def -(header: String): Headers = {
    new Headers(headers - header)
  }
  def --(header: Iterable[String]): Headers = {
    new Headers(headers -- header)
  }

  def ++(heads: Iterable[Header]): Headers = {
    var headers = this.headers
    heads foreach {
      header => {
        val res = add(getHeaders(header.name), header)
        res match {
          case Nil =>
          case x => headers += (header.name -> x)
        }
      }
    }
    new Headers(headers)
  }

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

  override def toString = {
    val builder = new StringBuilder
    for (h <- this) {
      builder.append(h.name).append(": ").append(h.value).append("\r\n")
    }
    builder.toString
  }

  def elements: Iterator[Header] = {
    val iterable = for{
      (x, y) <- headers
      z <- y
    } yield z
    iterable.elements
  }
  
  private def add(list: List[Header], header: Header): List[Header] = {
    list match {
      case List() => List(header)
      case x => if (x contains header) Nil else header :: x
    }
  }

  private[recondo] def isCacheable: Boolean = {
    if (contains(Header(VARY, "*"))) return false
    val interestingHeaderNames = Set(CACHE_CONTROL, PRAGMA, EXPIRES, LAST_MODIFIED)
    val cacheableHeaders = new Headers(headers.filter(interestingHeaderNames contains _._1)).toList
    val dateHeaderValue = firstHeader(DATE) match {
      case Some(h) => Header.fromHttpDate(h)
      case None => return false;
    }

    for (h <- cacheableHeaders) {
      if (!analyzeCachability(h, dateHeaderValue)) {
        return false
      }
    }
    contains(LAST_MODIFIED) || contains(ETAG) || contains(EXPIRES)
  }

  private def analyzeCachability(h: Header, dateHeaderValue: DateTime): Boolean = h match {
    case Header(CACHE_CONTROL, v) => {
      val expire = firstHeader(EXPIRES).map {analyzeCachability(_, dateHeaderValue)} getOrElse false
      (v.contains("max-age") || expire) && !(v.contains("no-store") || v.contains("no-cache"))
    }
    case Header(PRAGMA, v) => !(v.contains("no-cache") || v.contains("no-store"))
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