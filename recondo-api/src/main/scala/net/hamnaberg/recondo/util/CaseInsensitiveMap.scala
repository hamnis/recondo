package net.hamnaberg.recondo.util

import collection.immutable.{Map, HashMap}

/**
 * @author <a href="mailto:erlend@hamnaberg.net">Erlend Hamnaberg</a>
 * @version $Revision: $
 */
class CaseInsensitiveMap[T] private(map: Map[CaseInsensitiveString, T]) extends Map[String, T] {
  val backingMap = map;

  def this() = this(new HashMap)

  def elements = backingMap.map{x => x._1.toString -> x._2}.elements

  def get(key: String) = backingMap get (new CaseInsensitiveString(key))

  def update[B1 >: T](key:String, value:B1) = new CaseInsensitiveMap(backingMap.update(new CaseInsensitiveString(key), value)) 

  def empty[C] = new CaseInsensitiveMap[C]()

  def size = backingMap.size
  
  def -(key:String) : Map[String, T] = new CaseInsensitiveMap(backingMap - new CaseInsensitiveString(key))
}