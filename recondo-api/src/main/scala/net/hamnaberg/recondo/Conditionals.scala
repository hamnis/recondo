package net.hamnaberg.recondo


import org.joda.time.DateTime

/**
 * Represents the different conditional types that an HTTP request may have.
 * This are basically 4 things:
 * <ul>
 *   <li>If-Match</li>
 *   <li>If-None-Match</li>
 *   <li>If-Unmodified-Since</li>
 *   <li>If-Modified-Since</li>
 * </ul>
 *
 * Combinations of these conditionals are possible with the following exceptions<br/>
 *
 * <table>
 *   <thead>
 *    <th>Conditional</th><th>Can be combined with</th><th>Unspecified</th>
 *   </thead>
 *   <tbody>
 *   <tr>
 *     <th>If-Match</th><td>If-Unmodified-Since</td><td>If-None-Match, If-Modified-Since</td>
 *   </tr>
 *   <tr>
 *     <th>If-None-Match</th><td>If-Modified-Since</td><td>If-Match, If-Unmodified-Since</td>
 *   </tr>
 *   <tr>
 *     <th>If-Unmodified-Since</th><td>If-Match</td><td>If-None-Match, If-Modified-Since</td>
 *   </tr>
 *   <tr>
 *     <th>If-Modified-Since</th><td>If-None-Match</td><td>If-Match, If-Unmodified-Since</td>
 *   </tr>
 *   </tbody>
 * </table>
 *
 * @author <a href="mailto:erlend@hamnaberg.net">Erlend Hamnaberg</a>
 */
case class Conditionals(ifMatch: List[Tag], ifNonMatch: List[Tag], ifModifiedSince: Option[DateTime], ifUnModifiedSince: Option[DateTime]) {
  def addIfMatch(tag: Tag) = tag match {
    case Tag.ALL => Conditionals(List(Tag.ALL), ifUnModifiedSince, false)
    case x => if (!ifMatch.contains(x)) Conditionals(x :: ifMatch, ifUnModifiedSince, false) else this
  }

  def addIfNoneMatch(tag: Tag) = tag match {
    case Tag.ALL => Conditionals(List(Tag.ALL), ifUnModifiedSince, true)
    case x => if (!ifNonMatch.contains(x)) Conditionals(x :: ifNonMatch, ifModifiedSince, true) else {this}
  }

  def setModifiedSince(dateTime: Option[DateTime]) = {
    Conditionals(ifNonMatch, dateTime, true)
  }

  def setUnModifiedSince(dateTime: Option[DateTime]) = {
    Conditionals(ifMatch, dateTime, false)
  }

  def toHeaders() = {
    Headers()
  }
}

object Conditionals {
  def apply() = {
    new Conditionals(Nil, Nil, None, None)
  }

  def apply(tags: List[Tag], dateTimeOption: Option[DateTime], notMatch: Boolean) = {
    if (notMatch) {
      new Conditionals(Nil, tags, dateTimeOption, None)
    }
    else {
      new Conditionals(tags, Nil, None, dateTimeOption)
    }
  }
}