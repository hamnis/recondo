package net.hamnaberg.recondo

/**
 * @author <a href="mailto:erlend@hamnaberg.net">Erlend Hamnaberg</a>
 * @version $Revision: $
 */
import net.liftweb.json.JsonAST
import net.liftweb.json.JsonDSL._
import net.liftweb.json.JsonAST.JValue

trait ToJSON {
  private[recondo] def json : JValue
  override def toString = pretty(JsonAST.render(json))
}

trait FromJSON[T] {
    private[recondo] def fromJson(json: JValue): T 
}