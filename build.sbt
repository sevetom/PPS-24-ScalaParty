val scala3Version = "3.3.3" // Or any other valid Scala 3 version, e.g., "3.4.2"

enablePlugins(AssemblyPlugin)

lazy val root = project
  .in(file("."))
  .settings(
      name := "PPS-24-ScalaParty",
      version := "0.1.0-SNAPSHOT",

      scalaVersion := scala3Version,

      scalacOptions ++= Seq(
        "-indent"
      ),

      libraryDependencies ++= Seq(
          "org.scalameta" %% "munit" % "1.0.0" % Test,
          "it.unibo.alice.tuprolog" % "tuprolog" % "3.3.0",
          "org.scalatest" %% "scalatest" % "3.2.19" % "test",
          "org.scala-lang.modules" %% "scala-xml" % "2.2.0",
          "org.scalafx" %% "scalafx" % "21.0.0-R32",
          "com.typesafe.play" %% "play-json" % "2.10.7"
),

      assembly / assemblyJarName := "PPS-24-ScalaParty-fatjar.jar",
      assembly / mainClass := Some("it.unibo.party.ScalaParty"),
      assembly / assemblyMergeStrategy := {
        case PathList("META-INF", xs @ _*) => MergeStrategy.discard
        case x => MergeStrategy.first
    }
  )