import sbt._

name := "common"

scalaVersion := "2.11.7"

version := "BUILD"

libraryDependencies ++= Seq(
  // Add additional dependencies here; uses Apache Ivy info.
  // Example: "com.twilio.sdk" % "twilio-java-sdk" % "3.4.1"
)

publishTo := Some(sbt.Resolver.file("file",  new File( "path/to/my/maven-repo/releases" )) )