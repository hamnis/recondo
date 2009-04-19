package net.hamnaberg.recondo

/**
 * @author <a href="mailto:erlend@escenic.com">Erlend Hamnaberg</a>
 * @version $Revision: #5 $ $Date: 2008/09/15 $
 */

class Response(status : Status, payload : Payload, headers : Headers) {

}

sealed abstract class Status(code : Int, message : String)

case object OK extends Status(200, "OK");