package org.fetch.app
package plugins

import com.themillhousegroup.scoup._
import fansi._
import org.jsoup._
import org.jsoup.nodes.Document
import scala.concurrent._
import scala.concurrent.duration._
import com.themillhousegroup.scoup.ScoupImplicits._

class QuoteOfDay extends Plugin {

	override def name: String = "Quote"

	override def parameters: List[String] = "-quote" :: "--quote" :: Nil

	override def description: String = "Quote of the day"

	override def author: String = "Akash Harel"

	override def run(args: Array[String]): Boolean = {
		val res: Future[Document] = Scoup.parse(s"https://www.brainyquote.com/quote_of_the_day")

		try {
			val result = Await.result(res, Duration.Inf)
			quote(result)
			true
		} catch {
			case hse:
			HttpStatusException => println(s" offline... (Status Code: ${hse.getStatusCode})")
			false
		}
	}

	def quote(document: Document): Unit = {
		val result = document.select(".b-qt")

		var count = 0;
		for (i <- result) {
			count += 1;
			if (count == 2) {
				println(Str(s"${i.text()}").overlay(Color.LightMagenta++Bold.On))
			}
		}

	}
}