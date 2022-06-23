package org.fetch.app

import com.colofabrix.scala.figlet4s.unsafe._
import fansi._
import org.clapper.classutil.ClassFinder
import java.io.File
import scala.collection.mutable.ArrayBuffer

object PluginManager{
	private val pluginList = new ArrayBuffer[Plugin]()
	def count: Int = pluginList.size
	def apply(classString: String) = new PluginManager(classString)

	def showHelp(): Unit = {
		if (pluginList.isEmpty) return

		Figlet4s.builder("Fetch")
				.withInternalFont("graffiti")
				.defaultMaxWidth()
				.render()
				.foreachLine(x => println(Str(x).overlay(Color.LightGreen ++ Bold.On)))

		println("\n" + Str("A simple plugin-based fetching utility").overlay(Color.LightRed ++ Bold.On) + "\n\n")
		println(
			Str("Plugins ").overlay(Color.Green ++ Bold.On)
			++
			Str("(").overlay(Bold.On)
			++
			Str(s"${count} loaded").overlay(Color.Cyan)
			++
			Str("):").overlay(Bold.On)
		)
		var i = 0
		for(plugin <- pluginList){
			i += 1
			println(
				Str(f"${i}%3d : ").overlay(Color.LightBlue ++ Bold.On)
					++
				Str(s"${plugin.name}").overlay(Color.LightMagenta ++ Bold.On)
			)
			println(
				Str(" Description: ").overlay(Color.Blue)
				++
				Str(s"${plugin.description}").overlay(Color.Cyan)
			)
			println(
				Str(" Author: ").overlay(Color.Blue)
				++
				Str(s"${plugin.author}").overlay(Color.LightGray)
			)
			println(
				Str(" Usage: ").overlay(Color.Blue)
				++
				Str(s"${plugin.parameters.mkString(", ")}  <parameters>").overlay(Color.Cyan)
			)
			println()
		}
	}
}

class PluginManager(classString: String) {
	def loadPlugins(): Unit = {
		val classes = ClassFinder(List(".").map(new File(_))).getClasses
        val plugins = ClassFinder.concreteSubclasses(classString, ClassFinder.classInfoMap(classes))
		if(plugins.isEmpty) throw new RuntimeException("No plugins found")
		plugins.foreach {
			classes =>
				PluginManager.pluginList += Class.forName(classes.name).getDeclaredConstructor().newInstance().asInstanceOf[Plugin]
		}
	}
	def getPluginByParameter(parameter: String): Plugin ={
		if (PluginManager.pluginList.isEmpty) loadPlugins()
		for(plugin <- PluginManager.pluginList){
			for (para <- plugin.parameters)
				if(para == parameter)
					return plugin
		}
		null
	}
}
