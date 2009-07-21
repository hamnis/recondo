package net.hamnaberg.recondo.core


import resolver.ResponseResolver

/**
 * @author <a href="mailto:erlend@hamnaberg.net">Erlend Hamnaberg</a>
 * @version $Revision: $
 */
class Recondo(storage: Storage, resolver: ResponseResolver) {
  def request(r: Request)
}

object Recondo {

}