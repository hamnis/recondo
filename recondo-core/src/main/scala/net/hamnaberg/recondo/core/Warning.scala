package net.hamnaberg.recondo.core

import net.hamnaberg.recondo._

private[core] case class Warning(code: Int, description: String) {
  def toHeader = new Header(HeaderConstants.WARNING, "%s %s %s".format(code, "Recondo", description))
}

private[core] object Warning {
  /**
   * MUST be included whenever the returned response is stale
   */
  val STALE_WARNING = new Warning(110, "Response is stale")

  /**
   * MUST be included if a cache returns a stale response because an attempt to revalidate the response failed, due to an inability to reach the server.
   */
  val REVALIDATE_FAILED_WARNING = new Warning(111, "Revalidation failed")

  /**
   * SHOULD be included if the cache is intentionally disconnected from the rest of the network for a period of time.
   */
  val DISCONNECT_OPERATION_WARNING = new Warning(112, "Disconnected operation")


  /**
   * MUST be included if the cache heuristically chose a freshness lifetime greater than 24 hours and the response's age is greater than 24 hours.
   */
  val HEURISTIC_EXPIRATION_WARNING = new Warning(110, "Heuristic expiration");

  /**
   * The warning text MAY include arbitrary information to be presented to a human user, or logged.
   * A system receiving this warning MUST NOT take any automated action, besides presenting the warning to the user.
   */
  val MISC_WARNING = new Warning(199, "Miscellaneous warning");

  /**
   * MUST be added by an intermediate cache or proxy if it applies any transformation changing the content-coding
   * (as specified in the Content-Encoding header) or
   * media-type (as specified in the Content-Type header) of the response,
   * or the entity-body of the response, unless this Warning code already appears in the response
   */
  val TRANSFORMATION_APPLIED_WARNING = new Warning(214, "Transformation applied");


  /**
   * The warning text MAY include arbitrary information to be presented to a human user, or logged.
   * A system receiving this warning MUST NOT take any automated action
   */
  val MISC_PERSISTENT_WARNING = new Warning(299, "Miscellaneous persistent warning");

}