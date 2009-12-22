package net.hamnaberg.recondo.auth

import net.hamnaberg.recondo.{Host}
import collection.immutable.HashMap

/**
 * @author <a href="mailto:erlend@hamnaberg.net">Erlend Hamnaberg</a>
 * @version $Revision : $
 */
private[auth] class AuthRegistry {
  var cache : Map[Host, AuthScheme] = new HashMap[Host, AuthScheme]()

  def register(host: Host, scheme: AuthScheme) {
    cache += host -> scheme
  }

  def get(host:Host) = cache get host

  def matches(host: Host) = cache contains host

  def clear() {
    cache = cache.empty
  }
}