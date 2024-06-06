ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.14"
libraryDependencies += "org.jfree" % "jfreechart" % "1.5.3"

lazy val root = (project in file("."))
  .settings(
    name := "TrafficSim"
  )