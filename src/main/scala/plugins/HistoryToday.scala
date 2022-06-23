package org.fetch.app
package plugins

import com.themillhousegroup.scoup._
import fansi._
import org.jsoup._
import org.jsoup.nodes.Document
import scala.concurrent._
import scala.concurrent.duration._
import com.themillhousegroup.scoup.ScoupImplicits._

class HistoryToday extends Plugin {
	override def name: String = "History Today"

	override def parameters: List[String] = "-history":: "--history-today" :: Nil

	override def description: String = "History Today: On this day"

	override def author: String = "Ayushi Varkate"

	override def run(args: Array[String]): Boolean = {
		val res: Future[Document] = Scoup.parse(s"https://en.wikipedia.org/wiki/Wikipedia:On_this_day/Today")

		try {
			val result = Await.result(res, Duration.Inf)
			fetchDaysName(result)
			true
		} catch {
			case hse: HttpStatusException => println(s"Offline... (Status Code: ${hse.getStatusCode})")
				false
		}
	}

	def fetchDaysName(document: Document): Unit = {
		val day = document.select("p:nth-of-type(2)")
		println(Str(s"${day.text}").overlay(Color.LightMagenta ++ Bold.On))
		val list = document.select(".mw-parser-output > ul > li")
		for (item <- list) {
			val string = item.text.split("–")
			println(
				Str(s"${string(0)}").overlay(Color.LightGreen ++ Bold.On)
				++
				Str(s"–${string(1)}").overlay(Color.LightGray ++ Bold.On)
			)
		}
	}
}