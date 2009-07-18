package net.hamnaberg.recondo


/**
 * @author <a href="mailto:erlend@hamnaberg.net">Erlend Hamnaberg</a>
 * @version $Revision: #5 $ $Date: 2008/09/15 $
 */
class Headers(h : Map[String, List[Header]]) extends Iterable[Header]{

  private[this] val headers = Map() ++ h.elements

  def firstHeader(name : String) : Option[Header] = {
    getHeaders(name) match {
      case Some(value) => value.reverse.firstOption
      case None => None
    }  
  }

  def firstHeaderValue(name : String) : Option[String] = {
    firstHeader(name) match {
        case Some(header) => Some(header.value);
        case None => None
    }
  }

  def getHeaders(name : String) : Option[List[Header]] = {
    headers get name   
  }

  def +(header : Header) : Headers = {
      val h = add(getHeaders(header.name), header)
      h match {
          case List() => this
          case List(_*) => {
                  val heads = headers + (header.name -> h)
                  new Headers(heads)
          }
      }
  }

  private def add(list : Option[List[Header]], header : Header) : List[Header] = {
      list match {
          case Some(value) => if (value contains header) Nil else header :: value
          case None => List(header)
      }
  }

  def elements : Iterator[Header] = {
    val iterable = for {
      (x,y) <- headers
      z <- y
    } yield z    
    iterable.elements
  }
}

object Headers {
  def apply() : Headers = {
    return new Headers(Map.empty)
  }
}