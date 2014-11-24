name := """playframeworkscala_crudsample_slick"""
version := "1.0-SNAPSHOT"
lazy val root = (project in file(".")).enablePlugins(PlayScala)
crossScalaVersions := Seq("2.10.4", "2.11.2")
scalaVersion := "2.11.1"
libraryDependencies ++= Seq(
   jdbc
  ,"mysql" % "mysql-connector-java" % "5.1.20"
  ,"com.typesafe.slick" % "slick_2.11" % "2.1.0"
  ,"com.typesafe.play" % "play-slick_2.11" % "0.8.0"
  ,"org.slf4j" % "slf4j-nop" % "1.7.7"
  ,"joda-time" % "joda-time" % "2.4"
  ,"org.joda" % "joda-convert" % "1.6"
  ,"com.github.tototoshi" %% "slick-joda-mapper" % "1.2.0"
)
