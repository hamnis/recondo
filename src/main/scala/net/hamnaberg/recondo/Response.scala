package net.hamnaberg.recondo

/**
 * @author <a href="mailto:erlend@escenic.com">Erlend Hamnaberg</a>
 * @version $Revision: #5 $ $Date: 2008/09/15 $
 */
class Response(val status : Status, val headers : Headers, val payload : Option[Payload]) {
   def getETag : Option[Tag] = {
     headers.firstHeaderValue("ETag") match {
       case Some(value) => Some(Tag(value))
       case None => None
     }     
   }
}



sealed abstract case class Status(code : Int, message : String)

case object CONTINUE extends Status(100, "Continue");
case object SWITCHING_PROTOCOLS extends Status(101, "Switching Protocols");
case object OK extends Status(200, "OK");
case object CREATED extends Status(201, "Created");
case object ACCEPTED extends Status(202, "Accepted");
case object NON_AUTHORITATIVE_INFORMATION extends Status(203, "Non-Authoritative Information");
case object NO_CONTENT extends Status(204, "No Content");
case object RESET_CONTENT extends Status(205, "Reset Content");
case object PARTIAL_CONTENT extends Status(206, "Partial Content");
case object MULTIPLE_CHOICES extends Status(300, "Multiple Choices");
case object MOVED_PERMANENTLY extends Status(301, "Moved Permanently");
case object FOUND extends Status(302, "Found");
case object SEE_OTHER extends Status(303, "See Other");
case object NOT_MODIFIED extends Status(304, "Not Modified");
case object USE_PROXY extends Status(305, "Use Proxy");
case object TEMPORARY_REDIRECT extends Status(307, "Temporary Redirect");
case object BAD_REQUEST extends Status(400, "Bad Request");
case object UNAUTHORIZED extends Status(401, "Unauthorized");
case object PAYMENT_REQUIRED extends Status(402, "Payment Required"); //Reserved for future use!
case object FORBIDDEN extends Status(403, "Forbidden");
case object NOT_FOUND extends Status(404, "Not Found");
case object METHOD_NOT_ALLOWED extends Status(405, "Method Not Allowed");
case object NOT_ACCEPTABLE extends Status(406, "Not Acceptable");
case object PROXY_AUTHENTICATION_REQUIRED extends Status(407, "Proxy Authentication Required");
case object REQUEST_TIMEOUT extends Status(408, "Request Timeout");
case object CONFLICT extends Status(409, "Conflict");
case object GONE extends Status(410, "Gone");
case object LENGTH_REQUIRED extends Status(411, "Length Required");
case object PRECONDITION_FAILED extends Status(412, "Precondition Failed");
case object REQUEST_ENTITY_TOO_LARGE extends Status(413, "Request Entity Too Large");
case object REQUEST_URI_TOO_LONG extends Status(414, "Request-URI Too Long");
case object UNSUPPORTED_MEDIA_TYPE extends Status(415, "Unsupported Media Type");
case object REQUESTED_RANGE_NOT_SATISFIABLE extends Status(416, "Requested Range Not Satisfiable");
case object EXPECTATION_FAILED extends Status(417, "Expectation Failed");
case object INTERNAL_SERVER_ERROR extends Status(500, "Internal Server Error");
case object NOT_IMPLEMENTED extends Status(501, "Not Implemented");
case object BAD_GATEWAY extends Status(502, "Bad Gateway");
case object SERVICE_UNAVAILABLE extends Status(503, "Service Unavailable");
case object GATEWAY_TIMEOUT extends Status(504, "Gateway Timeout");
case object HTTP_VERSION_NOT_SUPPORTED extends Status(505, "HTTP Version Not Supported");