package org.fetch.app
package plugins

import com.themillhousegroup.scoup._
import fansi._
import org.jsoup._
import org.jsoup.nodes.Document
import scala.concurrent._
import scala.concurrent.duration._

class Reddit extends Plugin {
	override def name: String = "Reddit - r/technology"

	override def parameters: List[String] = "-reddit" :: "--reddit-tech" :: Nil

	override def description: String = "Reddit: Get into anything! - r/technology"

	override def author: String = "Divya Gupta"

	override def run(args: Array[String]): Boolean = {
		val res: Future[Document] = Scoup.parse("https://techurls.com/")

		try {
			val result = Await.result(res, Duration.Inf)
			fetchRedditNews(result)
			true
		}
		catch {
			case hse: HttpStatusException => println(s"Aww snap, seems like you're offline... (Status Code: ${hse.getStatusCode})")
			false
		}
	}

	def fetchRedditNews(document: Document): Unit = {
		val links = document.select(".publisher-is-reddit.publisher-block > .publisher-card > .bottom-shadow.publisher-data-wrapper > .publisher-data > .publisher-links > div.publisher-link > .link > .article-link")
		val time = document.select(".publisher-is-reddit.publisher-block > .publisher-card > .bottom-shadow.publisher-data-wrapper > .publisher-data > .publisher-links > div.publisher-link > .aside > .text")
		for(i <- links.size - 1 to 0 by -1){
			val aTag = links.get(i)
			val meta = time.get(i)
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
