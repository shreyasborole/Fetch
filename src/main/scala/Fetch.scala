package org.fetch.app

object Fetch extends App {
	def help = args(0) == "-help" || args(0) == "--help"

	try {
		if (args.length == 0) throw new RuntimeException("No arguments to parse.")

		val pluginManager = PluginManager("org.fetch.app.Plugin")
		pluginManager.loadPlugins()
		if (help) throw new RuntimeException("")

		val plugin = pluginManager.getPluginByParameter(args(0))
		if (plugin == null) throw new RuntimeException("Failed to load or locate plugin")

		val pluginsArgs = if(args.length == 1) null else args.drop(1).array
		if(!plugin.run(pluginsArgs)) throw new RuntimeException("Failed to run plugin")
	}catch {
		case re: RuntimeException => {
			println(re.getMessage)
			PluginManager.showHelp()
		}
	}
}