val scala3Version = "3.3.3" // Or any other valid Scala 3 version, e.g., "3.4.2"

lazy val root = project
  .in(file("."))
  .settings(
      name := "PPS-24-ScalaParty",
      version := "0.1.0-SNAPSHOT",

      scalaVersion := scala3Version,

      libraryDependencies ++= Seq(
          "org.scalameta" %% "munit" % "1.0.0" % Test,
          "it.unibo.alice.tuprolog" % "tuprolog" % "3.3.0",
          "org.scalatest" %% "scalatest" % "3.2.19" % "test"
      )
  )