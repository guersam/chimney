import sbtcrossproject.CrossPlugin.autoImport.{crossProject, CrossType}

val versions = new {
  val scala212 = "2.12.15"
  val scala213 = "2.13.7"
  val scala3 = "3.1.0"
}

ThisBuild / scalafixScalaBinaryVersion :=
  CrossVersion.binaryScalaVersion(scalaVersion.value)


val settings =
  Seq(
    version := "0.6.1",
    scalaVersion := versions.scala3,
    crossScalaVersions := Seq(versions.scala212, versions.scala213, versions.scala3),
    scalacOptions ++= Seq(
//      "-target:jvm-1.8",
      "-encoding", "UTF-8",
      "-unchecked",
      "-deprecation",
//      "-explaintypes",
      "-feature",
//      "-Ywarn-dead-code",
//      "-Ywarn-numeric-widen",
//      "-Xlint:adapted-args",
//      "-Xlint:delayedinit-select",
//      "-Xlint:doc-detached",
//      "-Xlint:inaccessible",
//      "-Xlint:infer-any",
//      "-Xlint:nullary-unit",
//      "-Xlint:option-implicit",
//      "-Xlint:package-object-classes",
//      "-Xlint:poly-implicit-overload",
//      "-Xlint:private-shadow",
//      "-Xlint:stars-align",
//      "-Xlint:type-parameter-shadow",
//      "-Ywarn-unused:locals",
//      "-Ywarn-macros:after",
//      "-Xfatal-warnings",
      "-language:higherKinds"
    ),
    scalacOptions ++=
      (CrossVersion.partialVersion(scalaVersion.value) match {
        case Some((2, 12)) => Seq(
          "-Xfuture",
//          "-Xsource:3",
          "-Xexperimental",
          "-Yno-adapted-args",
          "-Ywarn-inaccessible",
          "-Ywarn-infer-any",
          "-Ywarn-nullary-override",
          "-Ywarn-nullary-unit",
          "-Xlint:by-name-right-associative",
          "-Xlint:unsound-match",
          "-Xlint:nullary-override"
        )
        case Some((2, 13)) => Seq(
//          "-Xsource:3",
        )
        case Some((3, _)) => Seq(
          "-Ykind-projector",
          "-Xmax-inlines", "64"
        )
        case _ => Seq.empty
      }),
    Compile / console / scalacOptions --= Seq("-Ywarn-unused:imports", "-Xfatal-warnings"),
    Compile / crossPaths := true
  )


val scala3Settings = Seq(
  testFrameworks += new TestFramework("utest.runner.Framework"),
  version := "0.1.0",
  scalaVersion := versions.scala3,
  scalacOptions ++= Seq(
    // "-Xtarget:1.8",
    "-encoding", "UTF-8",
    "-unchecked",
    "-deprecation",
    "-feature",
    "-language:higherKinds",
    "-Xmax-inlines", "64"
  )
)

val dependencies = Seq(
  libraryDependencies ++= (
    Seq(
      "org.scala-lang.modules" %%% "scala-collection-compat" % "2.6.0",
      "com.lihaoyi" %%% "utest" % "0.7.10" % "test"
    ) ++
      (CrossVersion.partialVersion(scalaVersion.value) match {
        case Some((2, 12 | 13)) => Seq(
          compilerPlugin("org.typelevel" % "kind-projector" % "0.13.2" cross CrossVersion.full),
          "org.scala-lang" % "scala-reflect" % scalaVersion.value % "provided"
        )
        case _ => Seq.empty
      })
  )
)

lazy val root = project
  .in(file("."))
  .settings(settings)
  .settings(publishSettings)
  .settings(noPublishSettings)
  .aggregate(chimneyJVM, chimneyJS, chimneyCatsJVM, chimneyCatsJS, chimney3)
  .dependsOn(chimneyJVM, chimneyJS, chimneyCatsJVM, chimneyCatsJS, chimney3)
  .enablePlugins(SphinxPlugin, GhpagesPlugin)
  .settings(
    Sphinx / version := version.value,
    Sphinx / sourceDirectory := file("docs") / "source",
    git.remoteRepo := "git@github.com:scalalandio/chimney.git"
  )

lazy val chimney = crossProject(JSPlatform, JVMPlatform)
  .crossType(CrossType.Pure)
  .dependsOn(protos % "test->test")
  .settings(
    moduleName := "chimney",
    name := "chimney",
    description := "Scala library for boilerplate free data rewriting",
    testFrameworks += new TestFramework("utest.runner.Framework"),
  )
  .settings(settings)
  .settings(publishSettings)
  .settings(dependencies)

lazy val chimneyJVM = chimney.jvm
lazy val chimneyJS = chimney.js

lazy val chimneyCats = crossProject(JSPlatform, JVMPlatform)
  .crossType(CrossType.Pure)
  .dependsOn(chimney % "test->test;compile->compile")
  .settings(
    moduleName := "chimney-cats",
    name := "chimney-cats",
    description := "Chimney module for validated transformers support",
    testFrameworks += new TestFramework("utest.runner.Framework")
  )
  .settings(settings)
  .settings(publishSettings)
  .settings(dependencies)
  .settings(libraryDependencies += "org.typelevel" %%% "cats-core" % "2.6.1" % "provided")

lazy val chimneyCatsJVM = chimneyCats.jvm
lazy val chimneyCatsJS = chimneyCats.js

lazy val protos = crossProject(JSPlatform, JVMPlatform)
  .crossType(CrossType.Pure)
  .settings(
    moduleName := "chimney-protos",
    name := "chimney-protos"
  )
  .settings(settings)
  .settings(dependencies)
  .settings(noPublishSettings)

lazy val protosJVM = protos.jvm
lazy val protosJS = protos.js

lazy val chimney3 = project.in(file("chimney3"))
  // .crossType(CrossType.Pure)
  .settings(
    moduleName := "chimney3",
    name := "chimney3",
    description := "Scala 3 library for boilerplate free data rewriting "
  )
  .settings(scala3Settings)
  .settings(publishSettings)


lazy val publishSettings = Seq(
  organization := "io.scalaland",
  homepage := Some(url("https://scalaland.io")),
  licenses := Seq("Apache-2.0" -> url("http://www.apache.org/licenses/LICENSE-2.0")),
  scmInfo := Some(
    ScmInfo(url("https://github.com/scalalandio/chimney"), "scm:git:git@github.com:scalalandio/chimney.git")
  ),
  publishTo := sonatypePublishToBundle.value,
  publishMavenStyle := true,
  Test / publishArtifact := false,
  pomIncludeRepository := { _ =>
    false
  },
  pomExtra := (
    <developers>
      <developer>
        <id>krzemin</id>
        <name>Piotr Krzemiński</name>
        <url>http://github.com/krzemin</url>
      </developer>
      <developer>
        <id>MateuszKubuszok</id>
        <name>Mateusz Kubuszok</name>
        <url>http://github.com/MateuszKubuszok</url>
      </developer>
      <developer>
        <id>wookievx</id>
        <name>Łukasz Lampart</name>
        <url>https://github.com/wookievx</url>
      </developer>
    </developers>
  )
)

lazy val noPublishSettings =
  Seq(publish / skip := true, publishArtifact := false)
