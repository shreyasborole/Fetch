package org.fetch.app
package plugins

import com.themillhousegroup.scoup._
import fansi._
import org.jsoup._
import org.jsoup.nodes.Document
import scala.concurrent._
import scala.concurrent.duration._

class Medium extends Plugin {

	override def name: String = "Medium"

	override def parameters: List[String] = "-medium" :: "--medium-dev" :: Nil

	override def description: String = "Medium Blog"

	override def author: String = "Ketan Jindal"

	override def run(args: Array[String]): Boolean = {
		val res: Future[Document] = Scoup.parse("https://devurls.com/")
		try{
			val result = Await.result(res, Duration.Inf)
			fetchURL(result)
			true
		}
		catch {
			case hse: HttpStatusException => println(s"PLEASE TRY AGAIN.... (Status Code: ${hse.getStatusCode})")
			false
		}
	}
	def fetchURL(document: Document): Unit = {
		val links = document.select(".publisher-is-medium.publisher-block > .publisher-card > .bottom-shadow.publisher-data-wrapper > .publisher-data > .publisher-links > div.publisher-link > .link > .article-link")
		val text = document.select(".publisher-is-medium.publisher-block > .publisher-card > .bottom-shadow.publisher-data-wrapper > .publisher-data > .publisher-links > div.publisher-link > .aside > .text")
		for(i <- links.size - 1 to 0 by -1){
			val aTag = links.get(i)
			val meta = text.get(i)
			println(
				Str(f"${i + 1}%3d : ").overlay(Color.Blue ++ Bold.On)
					++
				Str(s"${aTag.text}").overlay(Color.LightMagenta ++ Bold.On)
			)
			println(Str(s"\t${aTag.attr("href")}").overlay(Color.DarkGray))
			println(Str(s"\t${meta.select(".text").text}\n").overlay(Color.Yellow))
		}
	}
}

