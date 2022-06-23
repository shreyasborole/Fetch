package org.fetch.app
package plugins

import com.hypertino.binders.value.{Obj, Value}
import com.hypertino.parser.HEval
import com.hypertino.parser.ast.Identifier
import com.hypertino.parser.eval.ValueContext
import fansi._
import org.fetch.app.Plugin

import scala.math._

class Calc extends Plugin{

	override def name: String = "Calculator"

	override def parameters: List[String] = "-calc" :: "-calculator" :: Nil

	override def description: String = "Calculator - a expression evaluator"

	override def author: String = "Shreyas Borole"

	override def run(args: Array[String]): Boolean = {
		val context: ValueContext = new ValueContext(Obj.empty) {
			override def function: PartialFunction[(Identifier, Seq[Value]), Value] = {
				case (Identifier(Seq("pow")), seq) => pow(seq.head.toDouble, seq.tail.head.toDouble)
				case (Identifier(Seq("max")), seq) => max(seq.head.toDouble, seq.tail.head.toDouble)
				case (Identifier(Seq("min")), seq) => min(seq.head.toDouble, seq.tail.head.toDouble)
				case (Identifier(Seq("sqrt")), seq) => sqrt(seq.head.toDouble)
				case (Identifier(Seq("cbrt")), seq) => cbrt(seq.head.toDouble)
				case (Identifier(Seq("ceil")), seq) => ceil(seq.head.toDouble)
				case (Identifier(Seq("floor")), seq) => floor(seq.head.toDouble)
				case (Identifier(Seq("log")), seq) => log(seq.head.toDouble)
				case (Identifier(Seq("log10")), seq) => log10(seq.head.toDouble)
				case (Identifier(Seq("acos")), seq) => acos(seq.head.toDouble)
				case (Identifier(Seq("asin")), seq) => asin(seq.head.toDouble)
				case (Identifier(Seq("atan")), seq) => atan(seq.head.toDouble)
				case (Identifier(Seq("cos")), seq) => cos(seq.head.toDouble)
				case (Identifier(Seq("sin")), seq) => sin(seq.head.toDouble)
				case (Identifier(Seq("tan")), seq) => tan(seq.head.toDouble)
				case (Identifier(Seq("toDegrees")), seq) => toDegrees(seq.head.toDouble)
				case (Identifier(Seq("toRadians")), seq) => toRadians(seq.head.toDouble)
				case (Identifier(Seq("random")), seq) => random
				case (Identifier(Seq("abs")), seq) => abs(seq.head.toDouble)
			}
		}
		try {
			val result = HEval(args.mkString, context).toDouble
			println(Str("> ").overlay(Color.LightGreen ++ Bold.On) ++ Str(result.toString).overlay(Color.LightCyan))
			true
		}
		catch {
			case e: Exception => {
				println("Invalid expression: " + e.getMessage)
				false
			}
		}
	}
}
