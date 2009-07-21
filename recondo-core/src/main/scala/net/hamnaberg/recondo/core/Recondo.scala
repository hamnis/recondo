package net.hamnaberg.recondo.core


import resolver.ResponseResolver
/**
 * @author <a href="mailto:erlend@hamnaberg.net">Erlend Hamnaberg</a>
 * @version $Revision: $
 */
class Recondo(storage: Storage, resolver: ResponseResolver) {
  def request(r: Request) : Response = {
    null
  }

}

object Recondo {

  private def isCacheableRequest(request: Request) = request match {
    case Request(_, GET, _, _, _) => true
    case _ => false
  }

  private[core] class Helper {
    private val safeMethods = Set(GET, HEAD, TRACE, OPTIONS)
    private val cacheableStatuses = List(Status.OK, Status.NON_AUTHORITATIVE_INFORMATION, Status.MULTIPLE_CHOICES, Status.MOVED_PERMANENTLY, Status.GONE)

    def isSafeRequest(r: Request) : Boolean = safeMethods contains r.method

    def isCacheableResponse(response: Response) = {
      if (cacheableStatuses.contains(response.status)) {
        response.headers.isCacheable
      }
      else {
        false
      }
    }

  }
}