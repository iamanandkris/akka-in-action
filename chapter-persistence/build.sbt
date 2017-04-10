name := "persistence"

version := "1.0"

organization := "com.manning"

scalaVersion := "2.11.8"

scalacOptions ++= Seq(
  "-deprecation",
  "-unchecked",
  "-Xfatal-warnings",
  "-Ywarn-unused",
  "-Ywarn-dead-code",
  "-feature",
  "-language:_"
)


resolvers ++= Seq("Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/",
                  "Sonatype snapshots"  at "http://oss.sonatype.org/content/repositories/snapshots/")

resolvers += Resolver.sonatypeRepo("releases")

addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.0" cross CrossVersion.full)

addCompilerPlugin("org.spire-math" %% "kind-projector" % "0.9.3")

parallelExecution in Test := false

fork := true

libraryDependencies ++= {
  val akkaVersion = "2.4.9"
  Seq(
    "com.typesafe.akka"         %%  "akka-actor"                          % akkaVersion,

    "com.typesafe.akka"         %%  "akka-persistence"                    % akkaVersion,
    "com.typesafe.akka"         %%  "akka-persistence-query-experimental" % akkaVersion,
    "org.iq80.leveldb"           %  "leveldb"                             % "0.7",
    "org.fusesource.leveldbjni"  %  "leveldbjni-all"                      % "1.8",

    "com.typesafe.akka"         %%  "akka-cluster"                        % akkaVersion,
    "com.typesafe.akka"         %%  "akka-cluster-tools"                  % akkaVersion,
    "com.typesafe.akka"         %%  "akka-cluster-sharding"               % akkaVersion,

    "com.typesafe.akka"         %% "akka-http-core"                       % akkaVersion, 
    "com.typesafe.akka"         %% "akka-http-experimental"               % akkaVersion, 
    "com.typesafe.akka"         %% "akka-http-spray-json-experimental"    % akkaVersion, 

    "com.typesafe.akka"         %%  "akka-testkit"                        % akkaVersion   % "test",
    "com.typesafe.akka"         %%  "akka-multi-node-testkit"             % akkaVersion   % "test",

    "commons-io"                %   "commons-io"                          % "2.4",

    "org.scalatest"             %%  "scalatest"                           % "2.2.4"       % "test",

    "com.typesafe.akka"         %%  "akka-slf4j"                          % akkaVersion,
    "ch.qos.logback"            %   "logback-classic"                     % "1.1.2",
    //"com.github.krasserm"       %%  "akka-persistence-cassandra"          % "0.6",
    "com.github.dnvriend"       %%  "akka-persistence-inmemory"           % "2.4.17.1",
    "com.github.thangiee"       %%  "freasy-monad"                        % "0.5.0",
    "org.typelevel"             %%  "cats"                                % "0.8.1",
    "io.monix"                  %%  "monix-eval"                          % "2.1.1",
    "io.monix"                  %%  "monix-cats"                          % "2.1.1"
  )
}

// Assembly settings
mainClass in Global := Some("aia.persistence.sharded.ShardedMain")

jarName in assembly := "persistence-examples.jar"
