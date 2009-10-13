package net.hamnaberg.recondo.core

import java.io.IOException
import Helper._
import net.hamnaberg.recondo._
import net.hamnaberg.recondo.Method._
import resolver.ResponseResolver
import java.net.ConnectException

/**
 * @author <a href="mailto:erlend@hamnaberg.net">Erlend Hamnaberg</a>
 * @version $Revision : $
 */
class Cache(val storage: Storage, val resolver: ResponseResolver) {
  def execute(r: Request): Response = execute(r, false)

  //TODO: 2.8 default params....
  def execute(r: Request, force: Boolean): Response = {
    val request = r.withAllHeaders
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
        case Some(x) => if (x.stale) resolve(handleStale(request, x.response), item) else rewriteResponse(request, x.response)
        case None => unconditionalResolve(request)
      }
    }
  }

  private[this] def handleStale(request: Request, response: Response): Request = {
    prepareConditionalRequest(request, response)
  }

  private[this] def rewriteResponse(request: Request, response: Response): Response = {
    response.ETag match {
      case Some(x) if (request.method == GET && request.conditionals.ifNonMatch.contains(x)) =>
        new Response(Status.NOT_MODIFIED, response.headers, None)
      case _ => response
    }
  }

  private[this] def unconditionalResolve(request: Request): Response = resolve(request, None)

  private[this] def resolve(request: Request, item: Option[CacheItem]) = {
    val resolvedResponse = executeRequest(request, item)

    item match {
      case Some(x) if (request.method == HEAD || resolvedResponse.status == Status.NOT_MODIFIED) => {
        updateCacheFromResolved(request, resolvedResponse, x.response)
      }
      case Some(x) => resolvedResponse
      case None if (isCacheableResponse(resolvedResponse)) => storage.insert(request, resolvedResponse)
      case None => resolvedResponse
    }
  }

  private[this] def updateCacheFromResolved(request: Request, resolvedResponse: Response, cachedResponse: Response): Response = {
    val updatedHeaders = resolvedResponse.headers -- Helper.unmodifiableHeaders
    val cachedHeaders = cachedResponse.headers.asMap
    val newHeaders = cachedHeaders ++ updatedHeaders.asMap
    val response = new Response(cachedResponse.status, new Headers(newHeaders), cachedResponse.payload)
    storage.update(request, response)
  }

  private[this] def executeRequest(request: Request, item: Option[CacheItem]) = {
    try {
      val resolved = resolver.resolve(request)
      resolved.getOrElse(error("No response from resolver"))
    }
    catch {
      case e: ConnectException => item.map {
        x => addWarnings(x.response, Warning.DISCONNECT_OPERATION_WARNING, Warning.STALE_WARNING)
      }.getOrElse(throw new RuntimeException(e))
      case e: IOException => item.map {
        x => addWarnings(x.response, Warning.REVALIDATE_FAILED_WARNING, Warning.STALE_WARNING)
      }.getOrElse(throw new RuntimeException(e))
    }
  }

  private[this] def addWarnings(response: Response, warn: Warning*) = {
    new Response(response.status, response.headers ++ warn.map(_.toHeader), response.payload)
  }

}

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

  def isCacheableResponse(response: Response) = cacheableStatuses.contains(response.status) && response.headers.isCacheable

  def isCacheableRequest(request: Request) = {
    def analyzeCacheControlHeader(h: Option[String]) = {
      h.map(v => ((v contains "no-cache") || (v contains "no-store"))).getOrElse(false)
    }

    request match {
      case Request(_, GET, h, _, _, _, _) => analyzeCacheControlHeader(h.firstHeaderValue(HeaderConstants.CACHE_CONTROL))
      case _ => false
    }
  }

  def prepareConditionalRequest(request: Request, response: Response): Request = {
    if (response.payloadAvailable) {
      response.ETag.map(v => request.conditionals(request.conditionals.addIfNoneMatch(v))).getOrElse(request)
    }
    else {
      request.conditionals(Conditionals())
    }
  }
}