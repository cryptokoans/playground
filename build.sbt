name := "cryptokoans"
organization := "com.cryptokoans"
version := "0.1"

scalaVersion := "2.12.6"

jsEnv := new org.scalajs.jsenv.jsdomnodejs.JSDOMNodeJSEnv()
scalaJSUseMainModuleInitializer := true
enablePlugins(ScalaJSBundlerPlugin)


useYarn := true // makes scalajs-bundler use yarn instead of npm

scalacOptions ++=
  "-encoding" :: "UTF-8" ::
    "-unchecked" ::
    "-deprecation" ::
    "-explaintypes" ::
    "-feature" ::
    "-language:_" ::
    "-Xfuture" ::
    "-Xlint" ::
    "-Ypartial-unification" ::
    "-Yno-adapted-args" ::
    "-Ywarn-extra-implicit" ::
    "-Ywarn-infer-any" ::
    "-Ywarn-value-discard" ::
    "-Ywarn-nullary-override" ::
    "-Ywarn-nullary-unit" ::
    Nil

//resolvers += "jitpack" at "https://jitpack.io"
libraryDependencies += "us.oyanglul" %%% "owlet" % "0.3.1-SNAPSHOT"

// hot reloading configuration:
// https://github.com/scalacenter/scalajs-bundler/issues/180
addCommandAlias("dev", "; compile; fastOptJS::startWebpackDevServer; devwatch; fastOptJS::stopWebpackDevServer")
addCommandAlias("devwatch", "~; fastOptJS; copyFastOptJS")

version in webpack := "4.16.1"
version in startWebpackDevServer := "3.1.4"
webpackDevServerExtraArgs := Seq("--progress", "--color")
webpackConfigFile in fastOptJS := Some(baseDirectory.value / "webpack.config.dev.js")

webpackBundlingMode in fastOptJS := BundlingMode.LibraryOnly() // https://scalacenter.github.io/scalajs-bundler/cookbook.html#performance

// when running the "dev" alias, after every fastOptJS compile all artifacts are copied into
// a folder which is served and watched by the webpack devserver.
// this is a workaround for: https://github.com/scalacenter/scalajs-bundler/issues/180
lazy val copyFastOptJS = TaskKey[Unit]("copyFastOptJS", "Copy javascript files to target directory")
copyFastOptJS := {
  val inDir = (crossTarget in(Compile, fastOptJS)).value
  val outDir = (crossTarget in(Compile, fastOptJS)).value / "dev"
  val files = Seq(name.value.toLowerCase + "-fastopt-loader.js", name.value.toLowerCase + "-fastopt.js") map { p => (inDir / p, outDir / p) }
  IO.copy(files, overwrite = true, preserveLastModified = true, preserveExecutable = true)
}

mappings in makeSite ++= Seq(
  file("assets/index.html") -> "index.html",
  target.value / "scala-2.12/scalajs-bundler/main/cryptokoans-fastopt.js" -> "cryptokoans-fastopt.js",
  target.value / "scala-2.12/scalajs-bundler/main/cryptokoans-fastopt-library.js" -> "cryptokoans-fastopt-library.js",
  target.value / "scala-2.12/scalajs-bundler/main/cryptokoans-fastopt-loader.js" -> "cryptokoans-fastopt-loader.js"
)

enablePlugins(GhpagesPlugin)

git.remoteRepo := "git@github.com:cryptokoans/playground.git"

// Bintray repo is used so far. Migration to Maven Central is planned
resolvers += Resolver.bintrayRepo("fluencelabs", "releases")

val cryptoV = "0.0.3"

libraryDependencies ++= Seq(
  "one.fluence" %%% "crypto-core" % cryptoV, // basic types and APIs
  "one.fluence" %%% "crypto-hashsign" % cryptoV, // hashers and signatures
  "one.fluence" %%% "crypto-cipher" % cryptoV, // encoding and decoding
  "one.fluence" %%% "crypto-keystore" % cryptoV, // serialize and store a keypair
  "one.fluence" %%% "crypto-jwt" % cryptoV // simple JWT implementation
)

jsDependencies += "org.webjars.bower" % "jshashes" % "1.0.5" / "1.0.5/hashes.min.js"
