package org.fetch.app
package plugins

import com.themillhousegroup.scoup._
import fansi._
import org.fetch.app.Plugin
import org.jsoup._
import org.jsoup.nodes.Document

import scala.concurrent._
import scala.concurrent.duration._

class HNews extends Plugin {

	override def name: String = "Hacker News"

	override def parameters: List[String] = "-hnews" :: "--hacker-news" :: Nil

	override def description: String = "Hacker News - Top 10 tech news"

	override def author: String = "Shreyas Borole"

	override def run(args: Array[String]): Boolean = {
		val res: Future[Document] = Scoup.parse("https://news.ycombinator.com/")

		try{
			val result = Await.result(res, 10.seconds)
			fetchHNews(result)
			true
		}
		catch {
			case hse: HttpStatusException => {
				println(s"Error occurred while sending web request (Status Code: ${hse.getStatusCode})")
				false
			}
		}
	}

	def fetchHNews(document: Document): Unit = {
		val links = document.select("td .titlelink")
		val subtext = document.select("td .subtext")

		for(i <- 9 to 0 by -1){
			val aTag = links.get(i)
			val meta = subtext.get(i)
			println(
				Str(f"${i + 1}%3d : ").overlay(Color.Blue ++ Bold.On)
					++
				Str(s"${aTag.text}").overlay(Color.LightMagenta ++ Bold.On)
			)
			println(Str(s"\t${aTag.attr("href")}").overlay(Color.DarkGray))
			println(
				Str(s"\t${meta.select(".score").text}").overlay(Color.LightGreen)
					++ " by " ++
				Str(s"${meta.select(".hnuser").text}").overlay(Color.Red)
					++ " about " ++
				Str(s"${meta.select(".age").text}\n").overlay(Color.Yellow)
			)
		}
	}

}
