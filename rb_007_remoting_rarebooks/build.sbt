val akkaVersion = "2.5.17"
val logbackVer = "1.2.3"

name := "rb_007_remoting_rarebooks"
organization := "com.rarebooks"
version := "0.5-SNAPSHOT"
scalaVersion := "2.12.7"
scalacOptions ++= Seq(
  "-unchecked",
  "-deprecation",
  "-language:_",
  "-target:jvm-1.8",
  "-encoding", "UTF-8"
)
parallelExecution in Test := false
logBuffered in Test := false
parallelExecution in ThisBuild := false
resolvers += "Sonatype OSS Releases" at "https://oss.sonatype.org/content/repositories/releases"
libraryDependencies ++= Seq(
  "com.typesafe.akka"      %% "akka-actor"       % akkaVersion,
  "com.typesafe.akka"      %% "akka-remote"      % akkaVersion,
  "com.typesafe.akka"      %% "akka-slf4j"       % akkaVersion,
  "ch.qos.logback"         %  "logback-classic"  % logbackVer,
  "com.typesafe.akka"      %% "akka-testkit"     % akkaVersion    % "test",
  "org.scalatest"          %% "scalatest"        % "3.0.5"        % "test"
)

addCommandAlias("rb", "runMain com.rarebooks.library.RareBooksApp -Dakka.remote.netty.tcp.port=2551")