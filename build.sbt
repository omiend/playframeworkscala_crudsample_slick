name := """playframeworkscala_crudsample_slick"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

crossScalaVersions := Seq("2.10.4", "2.11.1")

scalaVersion := "2.11.1"

libraryDependencies ++= Seq(
   jdbc
  ,cache
  ,"com.typesafe.slick" % "slick_2.11" % "2.1.0"
  ,"org.slf4j" % "slf4j-nop" % "1.7.7"
  ,"com.typesafe.play" % "play-slick_2.11" % "0.8.0"
)
