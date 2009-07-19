package net.hamnaberg.recondo.mutable


import org.joda.time.DateTime

class MutableConditionals {
  private[this] var c = Conditionals();

  def addIfMatch(tag: Tag) {
    c = c.addIfMatch(tag)
  }

  def addIfNoneMatch(tag: Tag) {
    c = c.addIfNoneMatch(tag)
  }

  def setModifiedSince(dateTime: Option[DateTime]) {
    c = c.setModifiedSince(dateTime)
  }

  def setUnModifiedSince(dateTime: Option[DateTime]) {
    c = c.setUnModifiedSince(dateTime)
  }

  private[recondo] def toHeaders() = {
    c.toHeaders
  }
}