ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.12.15"

lazy val root = (project in file("."))
  .settings(
    name := "otus_hw_one"
  )

libraryDependencies ++= Seq(
  "org.apache.hadoop" % "hadoop-client" % "3.3.1"
)

Compile / mainClass  := Some("ReadAndTransformHDFS")

assembly / mainClass  := Some("ReadAndTransformHDFS")
assembly / assemblyJarName := "ReadAndTransformHDFS.jar"
assembly / assemblyMergeStrategy := {
  case PathList("META-INF", xs @ _*) => MergeStrategy.discard
  case x => MergeStrategy.first
}