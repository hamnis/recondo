package net.hamnaberg.recondo.core

import org.scalatest.FunSuite
import org.scalatest.mock.MockitoSugar
import org.mockito.Mockito._
import org.mockito.Matchers._
import org.mockito.Matchers
import net.hamnaberg.recondo.resolver.ResponseResolver
import java.net.URI
import net.hamnaberg.recondo._
import org.joda.time.DateTime
import org.junit.Test

/**
 * 
 * @version $Revision: $
 */
class CacheSuite extends FunSuite with MockitoSugar {
  val uri = "http://unknown.com/resource/123"

  test("test execute GET request with no contents in cache storage") {
    val resolver = mock[ResponseResolver]
    when(resolver.resolve(isA(classOf[Request]))).
    thenReturn(Some(new Response(Status.OK, Headers() + Header.toHttpDate(HeaderConstants.DATE, new DateTime), None)))
    val storage = new CountingNullStorage
    val cache = new Cache(storage, resolver)
    val response = cache.execute(Request(uri))
    assert (response.headers contains HeaderConstants.DATE)
    assert (storage.size === 0)
  }

  test("test execute GET request which will be cached") {
    val resolver = mock[ResponseResolver]
    val resolved = new Response(
      Status.OK,
      Headers() ++ List(
        HeaderConstants.DATE -> new DateTime,
        Header(HeaderConstants.CACHE_CONTROL -> "private, max-age=10"),
        Header(HeaderConstants.ETAG -> new Tag("123").format)
        ),
      None
      )
    when(resolver.resolve(isA(classOf[Request]))).
    thenReturn(Some(resolved))
    val request = Request(uri)
    val storage = mock[Storage]
    val cache = new Cache(storage, resolver)
    when(storage.insert(request, resolved)).thenReturn(resolved)
    when(storage.size).thenReturn(1)
    val response = cache.execute(request)
    assert(response != null)
    assert(Helper.isCacheableResponse(response) === true)
    assert (storage.size === 1)    
  }
}

class CountingNullStorage extends Storage {
  var count = 0

  def clear() = count = 0

  protected def invalidate(key: Key) {}

  protected def insertImpl(key: Key, response: Response) = {
    count += 1
    response
  }

  def invalidate(uri: URI) {
    count -= 1
  }

  def update(request: Request, response: Response) = response

  def get(request: Request) = None

  def size = count
}