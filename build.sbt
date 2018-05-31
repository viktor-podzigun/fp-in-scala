
name := "fp-in-scala"

organization := "com.github.viktor-podzigun"

version := "1.0-SNAPSHOT"

scalaVersion := "2.11.8"

//fork := false

// to run coverage tests use:
//
// activator clean coverage test coverageReport
//
//
//coverageEnabled := true

coverageMinimum := 80

coverageFailOnMinimum := true

scalacOptions ++= Seq(
  //"-Xcheckinit",
  "-Xfatal-warnings",
  "-Xlint:_,-missing-interpolator",
  "-explaintypes",
  "-unchecked",
  "-deprecation",
  "-feature"
)

libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "3.0.1" % "test",
  "org.scalamock" %% "scalamock-scalatest-support" % "3.6.0" % "test"
)

resolvers ++= Seq(
  "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/"
)
