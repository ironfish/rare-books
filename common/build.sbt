val akkaVersion = "2.5.17"
val logbackVer = "1.2.3"
val parserVer = "1.1.1"

name := "common"
organization := "com.rarebooks"
version := "0.5-SNAPSHOT"
scalaVersion := "2.12.7"
resolvers += "Sonatype OSS Releases" at "https://oss.sonatype.org/content/repositories/releases"
libraryDependencies ++= Seq(
  "com.typesafe.akka"        %% "akka-actor"                % akkaVersion,
  "com.typesafe.akka"        %% "akka-slf4j"                % akkaVersion,
  "ch.qos.logback"           %  "logback-classic"           % logbackVer,
  "org.scala-lang.modules"   %% "scala-parser-combinators"  % parserVer,
  "com.typesafe.akka"        %% "akka-testkit"              % akkaVersion    % "test",
  "org.scalatest"            %% "scalatest"                 % "3.0.5"        % "test"
)
