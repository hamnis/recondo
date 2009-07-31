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
    val request = mergeHeaders(r)
    if (!isCacheableRequest(request)) {
      if (!isSafeRequest(request)) {
        storage.invalidate(r.uri)
      }
      executeRequest(request, None)
    }
    else {
      getFromCache(request, force)
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
    prepareConditionalRequest(request, response)
  }

  private[this] def rewriteResponse(request: Request, response: Response): Response = {
    response.ETag match {
      case Some(x) if (request.method == GET && request.conditionals.ifNonMatch.contains(x)) => new Response(Status.NOT_MODIFIED, response.headers, None)
      case _ => response
    }
  }

  private[this] def unconditionalResolve(r: Request): Response = resolve(r, None)

  private[this] def resolve(request: Request, item: Option[CacheItem]) = {
    val resolvedResponse = executeRequest(request, item)
    
    item match {
      case Some(x) => {
        if (resolvedResponse.status == Status.OK) {
          storage.invalidate(Key(request, x.response))
        }
        if (request.method == HEAD || resolvedResponse.status == Status.NOT_MODIFIED) {
          updateCacheFromResolved(request, resolvedResponse, x.response)
        }
        else {
          resolvedResponse
        }
      }
      case None if(isCacheableResponse(resolvedResponse)) => cache(request, resolvedResponse)
      case None => resolvedResponse
    }
  }

  private[this] def updateCacheFromResolved(request: Request, resolvedResponse: Response, cachedResponse: Response): Response = {
    val updatedHeaders = resolvedResponse.headers -- Helper.unmodifiableHeaders
    val cachedHeaders = cachedResponse.headers.asMap
    val newHeaders = cachedHeaders ++ updatedHeaders.asMap
    val response = new Response(cachedResponse.status, new Headers(newHeaders), cachedResponse.payload)
    storage.put(Key(request, response), response)
  }

  private[this] def cache(request: Request, response: Response): Response = {
    val key = Key(request, response)
    storage.put(key, CacheItem(response))
  }

  private[this] def executeRequest(request: Request, item: Option[CacheItem]) = {
      val resolved = resolver.resolve(request)
      item match {
        case Some(x) if (resolved.isLeft) => Helper.warn(x.response)
        case None if (resolved.isLeft) => throw new RuntimeException(resolved.left.get)
        case _ => resolved.right.get
      }
  }
}

object Recondo

private[core] object Helper {
  private val safeMethods = Set(GET, HEAD, TRACE, OPTIONS)
  private val cacheableStatuses = Set(Status.OK, Status.NON_AUTHORITATIVE_INFORMATION, Status.MULTIPLE_CHOICES, Status.MOVED_PERMANENTLY, Status.GONE)
  val unmodifiableHeaders = Set(
    "Age",
    "Connection",
    "Keep-Alive",
    "Proxy-Authenticate",
    "Proxy-Authorization",
    "TE",
    "Trailers",
    "Transfer-Encoding",
    "Upgrade"
    )

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
  
  def mergeHeaders(r: Request) = {
    var condititonalHeaders = r.conditionals.toHeaders
    var heads = r.headers;
    condititonalHeaders foreach {heads -= _.name}
    heads ++ condititonalHeaders
    r.headers(heads)
  }
}