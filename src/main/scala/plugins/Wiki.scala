package org.fetch.app
package plugins

import com.themillhousegroup.scoup._
import fansi._
import org.fetch.app.Plugin
import org.jsoup._
import org.jsoup.nodes.Document

import scala.concurrent._
import scala.concurrent.duration._
import scala.io._
import scala.util.control.Breaks


class Wiki extends Plugin {

	override def name: String = "Wikipedia"

	override def parameters: List[String] = "-wiki" :: "--wikipedia" :: Nil

	override def description: String = "Wikipedia - The Free Encyclopedia"

	override def author: String = "Shreyas Borole"


	override def run(args: Array[String]): Boolean = {


		val res: Future[Document] = Scoup.parse(s"https://en.wikipedia.org/wiki/${args.mkString("_").replace(" ", "_")}")

		try{
			val result = Await.result(res, Duration.Inf)
			fetchWiki(result)
			true
		}
		catch {
			case hse: HttpStatusException => println(s"Aww snap, something went wrong... (Status Code: ${hse.getStatusCode})")
			false
		}
	}

	def fetchWiki(doc: Document): Unit = {
		val para = doc.select(".mw-parser-output > p")

		if(para.text.contains("refer to:") || para.text.contains("can mean:")){
			val elements = doc.select(".mw-parser-output > ul > li > a").not(".mw-disambig")
			for(index <- elements.size - 1 to 0 by -1){
				val aTag = elements.get(index)
				println(
					Str(f"${index + 1}%3d => ").overlay(Color.Blue ++ Bold.On) ++ Str(s"${aTag.text}").overlay(Color.Magenta ++ Bold.On)
				)
				println(Str(s"\thttps://en.wikipedia.org${aTag.attr("href")}").overlay(Color.DarkGray))
			}

			Breaks.breakable {
				while (true)
					try {
						val choice = StdIn.readLine(
							(
							  Str("Enter an option ").overlay(Color.Green ++ Bold.On) ++ Str(s"[1-${elements.size}]: ").overlay(Color.Blue)
							  ).toString()
						)
						if (choice.isBlank || choice.matches("q|Q|exit|quit")) {

							Breaks.break()
						}

						choice.toInt match {
							case x if 1 <= x && x <= elements.size =>
								try {
									val result = Await.result(Scoup.parse("https://en.wikipedia.org" + elements.get(x - 1).attr("href")), Duration.Inf)
									fetchWiki(result)
								}
								catch {
									case hse: HttpStatusException => println(s"Aww snap, seems like you're offline... (Status Code: ${hse.getStatusCode})")
								}
								Breaks.break()

							case _ => println("Invalid number! Try again...")
						}
					} catch {
						case e: NumberFormatException => println("Please enter a number.")
					}
			}
		}
		else{
			val references = "\\[\\d*]".r
			Breaks.breakable{
				for(index <- 0 until para.size){
					if(para.get(index).hasText){
						val sectionText = new StringBuilder(references.replaceAllIn(para.get(index).text, ""))
						val highlights = para.get(index).children.not(s":matches($references)")
						highlights.forEach(highlight => {
							val i = sectionText.indexOf(highlight.text)

						highlight.tagName match {
							case "a" =>
								sectionText.replace(i, i + highlight.text.length, Str(highlight.text).overlay(Color.LightBlue).render)
							case "b" =>
								sectionText.replace(i, i + highlight.text.length, Str(highlight.text).overlay(Color.LightGreen ++ Bold.On).render)
							case "code" =>
								sectionText.replace(i, i + highlight.text.length, Str(highlight.text).overlay(Color.Cyan).render)
							case _ => ;
						}
					})
					println(sectionText)
						Breaks.break()
					}
				}
			}

			println()

			val note = doc.select(".navigation-not-searchable.hatnote")

			if(note.hasText){
				val highlights = note.get(0).children
				val sectionText =  new StringBuilder(note.get(0).text)

				highlights.forEach {
					highlight => {
						val i = sectionText.indexOf(highlight.text)
						sectionText.replace(i, i + highlight.text.length, Str(highlight.text).overlay(Color.Magenta).render)
					}
				}
				println(sectionText)
			}
		}
	}
}