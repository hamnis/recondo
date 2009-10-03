package net.hamnaberg.recondo


import org.joda.time.DateTime

/**
 * @author <a href="mailto:erlend@hamnaberg.net">Erlend Hamnaberg</a>
 * @version $Revision : #5 $ $Date: 2008/09/15 $
 */
class Response(val status: Status, val headers: Headers, val payload: Option[Payload]) extends PayloadContainer {
  lazy val ETag: Option[Tag] = {
    val header = headers firstHeaderValue ("ETag")
    header.map(x => Some(Tag(x))) getOrElse None
  }
  
  lazy val lastModified : Option[DateTime] = {
    val header = headers firstHeader ("Last-Modified")
    header.map(x => Some(Header.fromHttpDate(x))) getOrElse None
  }

  lazy val allowedMethods : Set[Method] = {
    val header = headers firstHeader("Allow");
    header.map(Set() ++ _.directives.keySet.map(Method(_))).getOrElse(Set())
  }
}

sealed abstract case class Status(code: Int, message: String)

object Status {
  def apply(code: Int) = code match {
    case CONTINUE.code => CONTINUE
    case SWITCHING_PROTOCOLS.code => SWITCHING_PROTOCOLS
    case OK.code => OK
    case CREATED.code => CREATED
    case ACCEPTED.code => ACCEPTED
    case NON_AUTHORITATIVE_INFORMATION.code => NON_AUTHORITATIVE_INFORMATION
    case NO_CONTENT.code => NO_CONTENT
    case RESET_CONTENT.code => RESET_CONTENT
    case PARTIAL_CONTENT.code => PARTIAL_CONTENT
    case MULTIPLE_CHOICES.code => MULTIPLE_CHOICES
    case MOVED_PERMANENTLY.code => MOVED_PERMANENTLY
    case FOUND.code => FOUND
    case SEE_OTHER.code => SEE_OTHER
    case NOT_MODIFIED.code => NOT_MODIFIED
    case USE_PROXY.code => USE_PROXY
    case TEMPORARY_REDIRECT.code => TEMPORARY_REDIRECT
    case BAD_REQUEST.code => BAD_REQUEST
    case UNAUTHORIZED.code => UNAUTHORIZED
    case PAYMENT_REQUIRED.code => PAYMENT_REQUIRED
    case FORBIDDEN.code => FORBIDDEN
    case NOT_FOUND.code => NOT_FOUND
    case METHOD_NOT_ALLOWED.code => METHOD_NOT_ALLOWED
    case NOT_ACCEPTABLE.code => NOT_ACCEPTABLE
    case PROXY_AUTHENTICATION_REQUIRED.code => PROXY_AUTHENTICATION_REQUIRED
    case REQUEST_TIMEOUT.code => REQUEST_TIMEOUT
    case CONFLICT.code => CONFLICT
    case GONE.code => GONE
    case LENGTH_REQUIRED.code => LENGTH_REQUIRED
    case PRECONDITION_FAILED.code => PRECONDITION_FAILED
    case REQUEST_ENTITY_TOO_LARGE.code => REQUEST_ENTITY_TOO_LARGE
    case REQUEST_URI_TOO_LONG.code => REQUEST_URI_TOO_LONG
    case UNSUPPORTED_MEDIA_TYPE.code => UNSUPPORTED_MEDIA_TYPE
    case REQUESTED_RANGE_NOT_SATISFIABLE.code => REQUESTED_RANGE_NOT_SATISFIABLE
    case EXPECTATION_FAILED.code => EXPECTATION_FAILED
    case INTERNAL_SERVER_ERROR.code => INTERNAL_SERVER_ERROR
    case NOT_IMPLEMENTED.code => NOT_IMPLEMENTED
    case BAD_GATEWAY.code => BAD_GATEWAY
    case SERVICE_UNAVAILABLE.code => SERVICE_UNAVAILABLE
    case GATEWAY_TIMEOUT.code => GATEWAY_TIMEOUT
    case HTTP_VERSION_NOT_SUPPORTED.code => HTTP_VERSION_NOT_SUPPORTED
    case undefined => error("Unknown status" + undefined)
  }
  case object CONTINUE extends Status(100, "Continue")
  case object SWITCHING_PROTOCOLS extends Status(101, "Switching Protocols")
  case object OK extends Status(200, "OK")
  case object CREATED extends Status(201, "Created")
  case object ACCEPTED extends Status(202, "Accepted")
  case object NON_AUTHORITATIVE_INFORMATION extends Status(203, "Non-Authoritative Information")
  case object NO_CONTENT extends Status(204, "No Content")
  case object RESET_CONTENT extends Status(205, "Reset Content")
  case object PARTIAL_CONTENT extends Status(206, "Partial Content")
  case object MULTIPLE_CHOICES extends Status(300, "Multiple Choices")
  case object MOVED_PERMANENTLY extends Status(301, "Moved Permanently")
  case object FOUND extends Status(302, "Found")
  case object SEE_OTHER extends Status(303, "See Other")
  case object NOT_MODIFIED extends Status(304, "Not Modified")
  case object USE_PROXY extends Status(305, "Use Proxy")
  case object TEMPORARY_REDIRECT extends Status(307, "Temporary Redirect")
  case object BAD_REQUEST extends Status(400, "Bad Request")
  case object UNAUTHORIZED extends Status(401, "Unauthorized")
  case object PAYMENT_REQUIRED extends Status(402, "Payment Required") //Reserved for future use!
  case object FORBIDDEN extends Status(403, "Forbidden")
  case object NOT_FOUND extends Status(404, "Not Found")
  case object METHOD_NOT_ALLOWED extends Status(405, "Method Not Allowed")
  case object NOT_ACCEPTABLE extends Status(406, "Not Acceptable")
  case object PROXY_AUTHENTICATION_REQUIRED extends Status(407, "Proxy Authentication Required")
  case object REQUEST_TIMEOUT extends Status(408, "Request Timeout")
  case object CONFLICT extends Status(409, "Conflict")
  case object GONE extends Status(410, "Gone")
  case object LENGTH_REQUIRED extends Status(411, "Length Required")
  case object PRECONDITION_FAILED extends Status(412, "Precondition Failed")
  case object REQUEST_ENTITY_TOO_LARGE extends Status(413, "Request Entity Too Large")
  case object REQUEST_URI_TOO_LONG extends Status(414, "Request-URI Too Long")
  case object UNSUPPORTED_MEDIA_TYPE extends Status(415, "Unsupported Media Type")
  case object REQUESTED_RANGE_NOT_SATISFIABLE extends Status(416, "Requested Range Not Sat  isfiable")
  case object EXPECTATION_FAILED extends Status(417, "Expectation Failed")
  case object INTERNAL_SERVER_ERROR extends Status(500, "Internal Server Error")
  case object NOT_IMPLEMENTED extends Status(501, "Not Implemented")
  case object BAD_GATEWAY extends Status(502, "Bad Gateway")
  case object SERVICE_UNAVAILABLE extends Status(503, "Service Unavailable")
  case object GATEWAY_TIMEOUT extends Status(504, "Gateway Timeout")
  case object HTTP_VERSION_NOT_SUPPORTED extends Status(505, "HTTP Version Not Supported")
}

