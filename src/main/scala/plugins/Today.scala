package org.fetch.app
package plugins

import com.themillhousegroup.scoup._
import fansi._
import org.jsoup._
import org.jsoup.nodes.Document
import scala.concurrent._
import scala.concurrent.duration._
import java.text.SimpleDateFormat
import java.util.Calendar


class Today extends Plugin {
	override def name: String = "Today"

	override def parameters: List[String] = "-today" :: "--today" :: Nil

	override def description: String = "Today - Get what day it today"

	override def author: String = "Sonu Chaudhary"

	override def run(args: Array[String]): Boolean = {
		val date = new SimpleDateFormat("d-MMM").format(Calendar.getInstance().getTime).split("-")
		val day = date(0)
		val month = date(1).toLowerCase

		val res: Future[Document] = Scoup.parse(s"https://www.daysoftheyear.com/days/$month/$day")

		try {
			val result = Await.result(res, Duration.Inf)
			fetchDaysName(result)
			true
		}
		catch {
			case hse: HttpStatusException => println(s"Offline... (Status Code: ${hse.getStatusCode})")
			false
		}
	}

	def fetchDaysName(document: Document): Unit = {
		val result = document.select("h3 .js-link-target")
		println(Str("Today is: ").overlay(Color.Red) ++ Str(s"${result.get(0).text}").overlay(Color.LightGreen ++ Bold.On))
	}
}
