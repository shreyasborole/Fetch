package org.fetch.app

trait Plugin {
	def name: String

	def parameters: List[String]

	def description: String

	def author: String

	def run(args: Array[String]): Boolean
}
