package net.hamnaberg.recondo

/**
 *
 * Validation tag equivalent to the HTTP entity tag. "A strong entity tag may be
 * shared by two entities of a resource only if they are equivalent by octet
 * equality.<br/> A weak entity tag may be shared by two entities of a resource
 * only if the entities are equivalent and could be substituted for each other
 * with no significant change in semantics."
 *
 * @see <a
 *      href="http://www.w3.org/Protocols/rfc2616/rfc2616-sec3.html#sec3.11">HTTP
 *      Entity Tags</a>
 * @see <a
 *      href="http://www.w3.org/Protocols/rfc2616/rfc2616-sec13.html#sec13.3.2">HTTP
 *      Entity Tag Cache Validators</a>
 *  
 * @author <a href="mailto:erlend@hamnaberg.net">Erlend Hamnaberg</a>
 * @version $Revision: $
 */
case class Tag(opaque : String, weak : Boolean) {
  require(opaque != null)

  def format : String = {
    val formatted = if ("*" == opaque) opaque else "\"" + opaque + "\"";
    if (weak) "W/" + formatted else formatted;
  }

  override def toString = "opaque: " + opaque + " weak: " + weak;
}


object Tag {
  def apply(value : String) = {
    require(value != null)
    require(value != "")
    val copy = if (value.startsWith("W/")) true -> value.substring(2) else false -> value;

    copy match {
      case Tuple2(false, "*") => Tag.ALL;
      case Tuple2(true, x) => new Tag(extract(x), true);
      case Tuple2(false, x) => new Tag(extract(x), false);
      case _ => throw new IllegalArgumentException("Not a valid ETag")
    }
  }

  private def extract(value : String) : String = {
    if (value.startsWith("\"") && value.endsWith("\"")) {
      value.substring(1, value.length -1)
    }
    else {
      throw new IllegalArgumentException("Not a valid ETag")
    }
  }
  case object ALL extends Tag("*", false)
}