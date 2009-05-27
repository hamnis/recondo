package net.hamnaberg.recondo


import collection.mutable

/**
 * @author <a href="mailto:erlend@escenic.com">Erlend Hamnaberg</a>
 * @version $Revision: #5 $ $Date: 2008/09/15 $
 */
class Headers(h : Map[String, List[Header]]) extends Iterable[Header]{

  private[this] val headers = Map() ++ h.elements

  def getFirstHeader(name : String) : Option[Header] = {
    def firstHeader(x : Option[List[Header]]) = x match {
      case Some(value) => value.firstOption
      case None => None
    }
    firstHeader(getHeaders(name))
  }
  
  def getHeaders(name : String) : Option[List[Header]] = {
    headers get name   
  }

  def elements : Iterator[Header] = {
    val iterable = for {
      (x,y) <- headers
      z <- y
    }
    yield z
    
    iterable.elements
  }
}

object Headers {
  def apply() : Headers = {
    return new Headers(Map.empty)
  }
  def apply(headers : Map[String, List[Header]]) : Headers = {
    return new Headers(headers)
  }
 /* def apply(headers : Map[String, List[String]]) : Headers = {
    return new Headers(headers)
  }*/

  def mapStringListToHeaders(headers : Map[String, List[String]]) {
    val z = for {
      (name, value) <- headers
      val foo = value.map(a => Header(name, a))
    } yield foo
    println(z)
  }
}