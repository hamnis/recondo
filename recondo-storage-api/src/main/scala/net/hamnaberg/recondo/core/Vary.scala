package net.hamnaberg.recondo.core

import net.hamnaberg.recondo.{Header, Request}

/**
 * @author <a href="mailto:erlend@hamnaberg.net">Erlend Hamnaberg</a>
 * @version $Revision: $
 */
case class Vary(variations : Map[String, String]) {

  def this() = this(Map.empty)

  def matches(request: Request) : Boolean = {
    val list = variations.map(x => Header(x)).toList
    list.forall(x => request.headers.contains(x))
  }

  override def toString = if (variations.isEmpty) "" else variations.map(x => Header(x).toString).mkString("\r\n")

  def size = variations.size
  def isEmpty = variations.isEmpty
}

object Vary {
  def apply() = new Vary(Map.empty)  
}