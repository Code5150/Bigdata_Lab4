name := "bigdata_lab4_1"

version := "0.1"

scalaVersion := "2.12.15"

idePackagePrefix := Some("com.vladislav")

val sparkVersion = "3.1.2"

libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core" % sparkVersion,
  "org.apache.spark" %% "spark-sql" % sparkVersion,
  "org.apache.spark" %% "spark-mllib" % sparkVersion,
  "org.apache.spark" %% "spark-streaming" % sparkVersion,
  "org.apache.zookeeper" % "zookeeper" % "3.7.0" % "compile",
)