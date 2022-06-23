ThisBuild / version := "1.0.0"

ThisBuild / scalaVersion := "2.13.7"

lazy val root = (project in file("."))
    .settings(
            name := "Fetch",
            idePackagePrefix := Some("org.fetch.app"),
            assemblyPackageScala / assembleArtifact := false,
            libraryDependencies += "com.colofabrix.scala" %% "figlet4s-core" % "0.3.1",
            libraryDependencies += "com.colofabrix.scala" %% "figlet4s-effects" % "0.3.1",
            libraryDependencies += "com.themillhousegroup" %% "scoup" % "1.0.0",
            libraryDependencies += "com.lihaoyi" %% "fansi" % "0.3.0",
            libraryDependencies += "com.hypertino" %% "expression-parser" % "0.3.0",
            libraryDependencies += "de.halcony" %% "scala-argparse" % "1.1.7",
            libraryDependencies += "org.clapper" %% "classutil" % "1.5.1"
    )

ThisBuild / assemblyMergeStrategy  := {
  case PathList("module-info.class") => MergeStrategy.discard
  case x if x.endsWith("/module-info.class") => MergeStrategy.discard
  case x =>
    val oldStrategy = (ThisBuild / assemblyMergeStrategy).value
    oldStrategy(x)
}