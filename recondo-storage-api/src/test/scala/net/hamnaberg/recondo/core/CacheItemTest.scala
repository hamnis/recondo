package net.hamnaberg.recondo.core


import org.joda.time.{DateTime, DateTimeUtils}
import org.junit.Test
import org.junit.Assert._
import org.mockito.Mockito

/**
 * @author <a href="mailto:erlend@hamnaberg.net">Erlend Hamnaberg</a>
 * @version $Revision: $
 */
class CacheItemTest {
  @Test
  def testStaleEmptyResponse {
    val item = CacheItem(createResponse(Headers(), None))
    assertTrue("item was stale", item.isStale)
  }
  
  @Test
  def testStaleResponseWithMaxAgeFiveSeconds {
    val dateTime = new DateTime(2009, 7, 27, 22, 51, 12, 0)
    val now = dateTime.plusSeconds(4)
    val item = new CacheItem(createResponse(Headers() + Header(HeaderConstants.CACHE_CONTROL, "max-age=5"), None), dateTime)
    DateTimeUtils.setCurrentMillisFixed(now.getMillis)
    assertFalse("item was stale", item.isStale)
    DateTimeUtils.setCurrentMillisFixed(now.plusSeconds(1).getMillis)
    assertTrue("item was not stale", item.isStale)
    DateTimeUtils.setCurrentMillisFixed(now.plusSeconds(2).getMillis)
    assertTrue("item was not stale", item.isStale)
  }

  @Test
  def testStaleResponseWithMaxAgeFiveSecondsWithNonAvailablePayload {
    val dateTime = new DateTime(2009, 7, 27, 22, 51, 12, 0)
    val now = dateTime.plusSeconds(4)
    val payload = Mockito.mock(classOf[Payload])
    Mockito.when(payload.isAvailable).thenReturn(false)

    val item = new CacheItem(createResponse(Headers() + Header(HeaderConstants.CACHE_CONTROL, "max-age=5"), Some(payload)), dateTime)
    DateTimeUtils.setCurrentMillisFixed(now.getMillis)
    assertTrue("item was stale", item.isStale)
    DateTimeUtils.setCurrentMillisFixed(now.plusSeconds(1).getMillis)
    assertTrue("item was not stale", item.isStale)
    DateTimeUtils.setCurrentMillisFixed(now.plusSeconds(2).getMillis)
    assertTrue("item was not stale", item.isStale)
  }

  @Test
  def testStaleResponseWithExpiresFiveSeconds {
    val dateTime = new DateTime(2009, 7, 27, 22, 51, 12, 0)
    val now = dateTime.plusSeconds(4)
    val headers = List(Header.toHttpDate(HeaderConstants.EXPIRES, dateTime.plusSeconds(5)), Header.toHttpDate(HeaderConstants.DATE, dateTime.minusSeconds(5)))
    val item = new CacheItem(createResponse(Headers() ++ headers, None), dateTime)
    DateTimeUtils.setCurrentMillisFixed(now.getMillis)
    assertFalse("item was stale", item.isStale)
    DateTimeUtils.setCurrentMillisFixed(now.plusSeconds(1).getMillis)
    assertTrue("item was not stale", item.isStale)
    DateTimeUtils.setCurrentMillisFixed(now.plusSeconds(2).getMillis)
    assertTrue("item was not stale", item.isStale)
  }

  @Test
  def testStaleResponseWithMaxAgeAndExpiresFiveSeconds {
    val dateTime = new DateTime(2009, 7, 27, 22, 51, 12, 0)
    val now = dateTime.plusSeconds(4)
    val headers = List(Header(HeaderConstants.CACHE_CONTROL, "max-age=5"), Header.toHttpDate(HeaderConstants.EXPIRES, dateTime.plusSeconds(5)))
    val item = new CacheItem(createResponse(Headers() ++ headers, None), dateTime)
    DateTimeUtils.setCurrentMillisFixed(now.getMillis)
    assertFalse("item was stale", item.isStale)
    DateTimeUtils.setCurrentMillisFixed(now.plusSeconds(1).getMillis)
    assertTrue("item was not stale", item.isStale)
    DateTimeUtils.setCurrentMillisFixed(now.plusSeconds(2).getMillis)
    assertTrue("item was not stale", item.isStale)
  }

  @Test
  def testStaleResponseWithMaxAge4SecondsAndExpiresFiveSeconds {
    val dateTime = new DateTime(2009, 7, 27, 22, 51, 12, 0)
    val now = dateTime.plusSeconds(4)
    val headers = List(Header(HeaderConstants.CACHE_CONTROL, "max-age=4"), Header.toHttpDate(HeaderConstants.EXPIRES, dateTime.plusSeconds(5)))
    val item = new CacheItem(createResponse(Headers() ++ headers, None), dateTime)
    DateTimeUtils.setCurrentMillisFixed(now.getMillis)
    assertTrue("item was not stale", item.isStale)
    DateTimeUtils.setCurrentMillisFixed(now.plusSeconds(1).getMillis)
    assertTrue("item was not stale", item.isStale)
    DateTimeUtils.setCurrentMillisFixed(now.plusSeconds(2).getMillis)
    assertTrue("item was not stale", item.isStale)
  }

  @Test
  def testStaleResponseWithMaxAge6SecondsAndExpiresFiveSeconds {
    val dateTime = new DateTime(2009, 7, 27, 22, 51, 12, 0)
    val now = dateTime.plusSeconds(4)
    val headers = List(Header(HeaderConstants.CACHE_CONTROL, "max-age=6"), Header.toHttpDate(HeaderConstants.EXPIRES, dateTime.plusSeconds(5)))
    val item = new CacheItem(createResponse(Headers() ++ headers, None), dateTime)
    DateTimeUtils.setCurrentMillisFixed(now.getMillis)
    assertFalse("item was stale", item.isStale)
    DateTimeUtils.setCurrentMillisFixed(now.plusSeconds(1).getMillis)
    assertFalse("item was stale", item.isStale)
    DateTimeUtils.setCurrentMillisFixed(now.plusSeconds(2).getMillis)
    assertTrue("item was not stale", item.isStale)
  }

  def createResponse(headers: Headers, payload: Option[Payload]) = {
    new Response(Status.OK, headers, payload)
  }
}