package net.hamnaberg.recondo


/**
 * @author <a href="mailto:erlend@escenic.com">Erlend Hamnaberg</a>
 * @version $Revision: $
 */
case class ServerCacheControl(header: Header) extends CacheControl {
  require(header.name == HeaderConstants.CACHE_CONTROL)

  val maxStale = -1;
  val isPrivate = header.value.contains("private");
  val maxAge = header.directives.get("max-age") map (x => x.get toInt) getOrElse(-1)
  val sMaxAge = header.directives.get("s-maxage") map (x => x.get toInt) getOrElse(-1)
  val cacheable = !(header.value.contains("no-cache") || header.value.contains("no-store"))
}

trait CacheControl {
  def isPrivate : Boolean
  def maxAge : Int
  def sMaxAge : Int
  def maxStale : Int
  def cacheable : Boolean
}