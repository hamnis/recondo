package net.hamnaberg.recondo.core

import java.io.IOException
import resolver.ResponseResolver
import Method._
import Helper._

/**
 * @author <a href="mailto:erlend@hamnaberg.net">Erlend Hamnaberg</a>
 * @version $Revision : $
 */
class Recondo(val storage: Storage, val resolver: ResponseResolver) {
  def request(r: Request): Response = request(r, false)

  def request(r: Request, force: Boolean): Response = {
    if (!isCacheableRequest(r)) {
      if (!isSafeRequest(r)) {
        storage.invalidate(r.uri)
      }
      executeRequest(r, None)
    }
    else {
      getFromCache(r, force)
    }
  }

  private[this] def getFromCache(request: Request, force: Boolean): Response = {
    if (force || request.conditionals.ifNonMatch.contains(Tag.ALL)) {
      unconditionalResolve(request)
    }
    else {
      val item = storage.get(request)
      item match {
        case Some(x) => if (x.isStale) resolve(handleStale(request, x.response), item) else rewriteResponse(request, x.response)
        case None => unconditionalResolve(request)
      }
    }
  }

  private[this] def handleStale(request: Request, response: Response): Request = {
    Helper.prepareConditionalRequest(request, response)
  }

  private[this] def rewriteResponse(request: Request, response: Response): Response = {
    if (request.method == GET) {
      response.ETag match {
        case Some(x) if (request.conditionals.ifNonMatch.contains(x)) => new Response(Status.NOT_MODIFIED, response.headers, None)
        case _ => response
      }
    }
    else {
      response
    }
  }

  private[this] def unconditionalResolve(r: Request): Response = resolve(r, None)

  private[this] def resolve(r: Request, item: Option[CacheItem]) = {
    val resolvedResponse = executeRequest(r, item)

    item match {
      case Some(x) if (resolvedResponse.status == Status.OK) => storage.invalidate(Key(r, x.response), x)
    }

    item match {
      case None if(isCacheableResponse(resolvedResponse)) => cache(r, resolvedResponse)
      case Some(x) if(r.method == HEAD) => updateHeadersFromResolved(resolvedResponse, x.response)
      case Some(x) if(resolvedResponse.status == Status.NOT_MODIFIED) => updateHeadersFromResolved(resolvedResponse, x.response)
      case _ => resolvedResponse
    }
  }

  private[this] def updateHeadersFromResolved(resolvedResponse: Response, cachedResponse: Response) = {
    resolvedResponse
  }

  private[this] def cache(request: Request, response: Response) = {
    val key = Key(request, response)
    storage.put(key, CacheItem(response))
  }

  private[this] def executeRequest(request: Request, item: Option[CacheItem]) = {
    try {
      resolver.resolve(request)
    }
    catch {
      case e:IOException => item match {
        case Some(x) => Helper.warn(x.response)
        case None => throw new RuntimeException(e)
      }
    }
  }
}

object Recondo

private[core] object Helper {
  private val safeMethods = Set(GET, HEAD, TRACE, OPTIONS)
  private val cacheableStatuses = List(Status.OK, Status.NON_AUTHORITATIVE_INFORMATION, Status.MULTIPLE_CHOICES, Status.MOVED_PERMANENTLY, Status.GONE)

  def isSafeRequest(r: Request): Boolean = safeMethods contains r.method

  def isCacheableResponse(response: Response) = {
    if (cacheableStatuses.contains(response.status)) {
      response.headers.isCacheable
    }
    else {
      false
    }
  }

  def isCacheableRequest(request: Request) = {
    def analyzeCacheControlHeader(h: Option[String]) = h match {
      case Some(v) => !((v contains "no-cache") || (v contains "no-store"))
      case None => false
    }

    request match {
      case Request(_, GET, h, _, _, _) => analyzeCacheControlHeader(h.firstHeaderValue(HeaderConstants.CACHE_CONTROL))
      case _ => false
    }
  }

  def warn(response: Response) = {
    response
  }

  def prepareConditionalRequest(request: Request, response: Response): Request = {
    if (response.isPayloadAvailable) {
      response.ETag match {
        case Some(v) => request.conditionals(request.conditionals.addIfNoneMatch(v))
        case None => request
      }
    }
    else {
      request.conditionals(Conditionals())
    }
  }
  
  def calculateHeaders(r: Request) = {
    var condititonalHeaders = r.conditionals.toHeaders
    var heads = r.headers;
    condititonalHeaders foreach {heads -= _.name}
    heads ++ condititonalHeaders
  }
}