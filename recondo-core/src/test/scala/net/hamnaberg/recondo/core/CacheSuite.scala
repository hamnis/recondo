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

/**
 * 
 * @version $Revision: $
 */
class CacheSuite extends FunSuite with MockitoSugar {
  val uri = "http://unknown.com/resource/123"
  val request = Request(uri)


  test("test none cacheable GET request with no contents in cache storage") {
    val resolver = mock[ResponseResolver]
    when(resolver.resolve(isA(classOf[Request]))).
    thenReturn(Some(new Response(Status.OK, Headers() + (HeaderConstants.DATE -> new DateTime), None)))
    val storage = new CountingNullStorage
    val cache = new Cache(storage, resolver)
    val response = cache.execute(request)
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
    val storage = new CountingNullStorage
    val cache = new Cache(storage, resolver)
    val response = cache.execute(request)
    assert(Helper.isCacheableResponse(response) === true)
    verify(resolver, times(1)).resolve(isA(classOf[Request]))
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