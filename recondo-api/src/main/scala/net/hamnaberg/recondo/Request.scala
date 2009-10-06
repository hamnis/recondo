package net.hamnaberg.recondo

import java.net.URI


/**
 * @author <a href="mailto:erlend@hamnaberg.net">Erlend Hamnaberg</a>
 * @version $Revision : $
 */
case class Request(uri: URI,
                   method: Method,
                   headers: Headers,
                   conditionals: Conditionals,
                   preferences: Preferences,
                   credentials: Option[Credentials],
                   payload: Option[Payload]
        ) extends PayloadContainer {

  def headers(headers: Headers): Request = {
    new Request(uri, method, headers, conditionals, preferences, credentials, payload)
  }

  private[recondo] def allHeaders() = {
    val h  = headers ++ conditionals.toHeaders ++ preferences.toHeaders
    if (hasPayload) {
      val heads = h - HeaderConstants.CONTENT_TYPE
      heads + payload.map{p => new Header(HeaderConstants.CONTENT_TYPE, p.MIMEType.toString)}.getOrElse(error("No payload!?!"))
    }
    else {
      h
    }
  }

  def conditionals(conditionals: Conditionals):Request = new Request(uri, method, headers, conditionals, preferences, credentials, payload)

  def preferences(preferences: Preferences):Request = new Request(uri, method, headers, conditionals, preferences, credentials, payload)

  def credentials(credentials: Credentials):Request = new Request(uri, method, headers, conditionals, preferences, Some(credentials), payload)

  def payload(payload: Payload):Request = new Request(uri, method, headers, conditionals, preferences, credentials, Some(payload))
}

object Request {
  def apply(requestURI: URI) = new Request(requestURI, Method.GET, Headers(), Conditionals(), Preferences(), None, None)

  def apply(requestURI: String) : Request = apply(URI.create(requestURI))
}
