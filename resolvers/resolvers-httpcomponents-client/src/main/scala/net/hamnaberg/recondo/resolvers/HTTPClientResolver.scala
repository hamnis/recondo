package net.hamnaberg.recondo.resolvers

import java.io.{OutputStream, IOException}
import net.hamnaberg.recondo.resolver.ResponseResolver
import net.hamnaberg.recondo.Method._
import net.hamnaberg.recondo.util._
import org.apache.http.client.HttpClient
import org.apache.http.client.methods._
import net.hamnaberg.recondo._
import org.apache.http.entity.{BasicHttpEntity, InputStreamEntity}
import org.apache.http.message.BasicHeader
import org.apache.http.{HttpEntityEnclosingRequest, HttpEntity, HttpResponse, Header => AHeader}
import payload.{InputStreamPayload, DelegatingInputStream}
class HTTPClientResolver(val client: HttpClient) extends ResponseResolver {
  def resolve(request: Request) = {
    client.execute(request)
  }

  implicit def convert(request: Request) : HttpUriRequest = {
    val method = request.method match {
      case GET => new HttpGet(request.uri)
      case PUT => new HttpPut(request.uri)
      case POST => new HttpPost(request.uri)
      case DELETE => new HttpDelete(request.uri)
      case TRACE => new HttpTrace(request.uri)
      case OPTIONS => new HttpOptions(request.uri)
      case HEAD => new HttpHead(request.uri)
    }    
    if (method.isInstanceOf[HttpEntityEnclosingRequest] && request.hasPayload) {
      val entityEnclosingRequest = method.asInstanceOf[HttpEntityEnclosingRequest]
      entityEnclosingRequest.setEntity(request.payload.get)
    }
    method
  }

  private implicit def convert(response: HttpResponse) : Option[Response] = {
    val headers: Headers = Headers() ++ response.getAllHeaders
    val mimeType = headers.firstHeaderValue("Content-Type") map(x => MIMEType(x)) getOrElse MIMEType.ALL 
    Some(new Response(Status(response.getStatusLine.getStatusCode), headers, toPayload(response.getEntity, mimeType)))
  }

  private implicit def toHttpEntity(payload: Payload) : HttpEntity = {
    new PayloadHttpEntity(payload);
  }

  private def toPayload(entity: HttpEntity, mimeType:MIMEType) : Option[Payload] = {
    try {
      entity match {
        case null => None
        case x if (x.getContent != null) => Some(new InputStreamPayload(new HttpEntityInputStream(x), mimeType))
      }
    }
    catch {
      case e @ (_ : IOException | _ : RuntimeException) => if (entity != null) entity.consumeContent; None
    }
  }

  private implicit def convert(headers: Array[AHeader]) : Iterable[Header] = {
    headers.map(x => Header(x.getName, x.getValue))
  }

  private class HttpEntityInputStream(val entity: HttpEntity) extends DelegatingInputStream(entity.getContent) {

    override def close() {
      entity.consumeContent();
    }
  }

  private class PayloadHttpEntity(payload: Payload) extends HttpEntity {
    val inputStream = payload.inputStream
    
    def isRepeatable = false

    def getContent = inputStream

    def getContentType = new BasicHeader(HeaderConstants.CONTENT_TYPE, payload.MIMEType.toString)

    def isChunked = false

    def consumeContent = inputStream.close

    def writeTo(outstream: OutputStream) = StreamUtils.copy(inputStream, outstream)

    def getContentLength = -1L

    def getContentEncoding = null

    def isStreaming = true
  }
}
