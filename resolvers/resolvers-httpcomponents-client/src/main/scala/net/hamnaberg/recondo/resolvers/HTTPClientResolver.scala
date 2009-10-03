package net.hamnaberg.recondo.resolvers

import net.hamnaberg.recondo.resolver.ResponseResolver
import net.hamnaberg.recondo.Method._
import org.apache.http.client.HttpClient
import org.apache.http.client.methods._
import net.hamnaberg.recondo._
import org.apache.http.{HttpEntity, HttpResponse, Header => AHeader}
import payload.{InputStreamPayload, DelegatingInputStream}
import java.io.IOException

class HTTPClientResolver(val client: HttpClient) extends ResponseResolver {
  def resolve(request: Request) = {
    client.execute(request)
  }

  implicit def convert(request: Request) : HttpUriRequest = {
    request.method match {
      case GET => new HttpGet(request.uri)
      case PUT => new HttpPut(request.uri)
      case POST => new HttpPost(request.uri)
      case DELETE => new HttpDelete(request.uri)
      case TRACE => new HttpTrace(request.uri)
      case OPTIONS => new HttpOptions(request.uri)
      case HEAD => new HttpHead(request.uri)
    }
  }

  private implicit def convert(response: HttpResponse) : Option[Response] = {
    val headers: Headers = Headers() ++ response.getAllHeaders
    val mimeType = headers.firstHeaderValue("Content-Type") map(x => MIMEType(x)) getOrElse MIMEType.ALL 
    Some(new Response(Status(response.getStatusLine.getStatusCode), headers, toPayload(response.getEntity, mimeType)))
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
}
