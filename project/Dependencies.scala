import org.portablescala.sbtplatformdeps.PlatformDepsPlugin.autoImport._
import sbt._

object Dependencies {
  val scala213 = "2.13.8"

  val chimney = Def.setting("io.scalaland" %%% "chimney" % "0.6.2")
  val scalaTest = Def.setting("org.scalatest" %%% "scalatest" % "3.2.13")
  val scalamock = "org.scalamock" %% "scalamock" % "5.2.0" % Test
  val slf4j = "org.slf4j" % "slf4j-api" % "2.0.3"
  val scalaJavaTime = Def.setting("io.github.cquiroz" %%% "scala-java-time" % "2.4.0")
  val scalaXml = "org.scala-lang.modules" %% "scala-xml" % "2.1.0"

  val overrides = akka.overrides ++ Seq(
    /*
    need to wait for new releases of those
    [error] 	* org.scala-lang.modules:scala-xml_2.13:2.1.0 (early-semver) is selected over {1.2.0}
    [error] 	    +- org.scalatest:scalatest-core_2.13:3.2.13           (depends on 2.1.0)
    [error] 	    +- com.typesafe.play:twirl-api_2.13:1.5.1             (depends on 1.2.0)
    [error] 	    +- com.typesafe.play:play-ws-standalone-xml_2.13:2.1.6 (depends on 1.2.0)
    [error] 	    +- com.lightbend.lagom:lagom-api_2.13:1.6.7           (depends on 1.2.0)
    [error] 	    +- com.lightbend.lagom:lagom-akka-management-core_2.13:1.6.7 (depends on 1.2.0)
    but safe to upgrade as there were no code changes necessary:
    - https://github.com/playframework/twirl/pull/525/files
    - https://github.com/lagom/lagom/pull/3333/files
     */
    scalaXml
  )

  object play {
    // same as lagom uses
    val core = "com.typesafe.play" %% "play" % "2.8.16"
  }

  object lagom {
    private val lagomVersion = "1.6.7"
    val scaladslServer = "com.lightbend.lagom" %% "lagom-scaladsl-server" % lagomVersion
    val scaladslApi = "com.lightbend.lagom" %% "lagom-scaladsl-api" % lagomVersion

    object js {
      private val lagomJsVersion = "0.5.1-1.6.5"

      val scalaDslApi = Def.setting("com.github.mliarakos.lagomjs" %%% "lagomjs-scaladsl-api" % lagomJsVersion)
      val scalaDslClient = Def.setting("com.github.mliarakos.lagomjs" %%% "lagomjs-scaladsl-client" % lagomJsVersion)
    }
  }

  object akka {
    private val akkaVersion = "2.6.20"
    private val akkaHttpVersion = "10.1.13"
    private val akkaJs = "2.2.6.14"

    val actor = "com.typesafe.akka" %% "akka-actor" % akkaVersion
    val clusterShardingTyped = "com.typesafe.akka" %% "akka-cluster-sharding-typed" % akkaVersion
    val http = "com.typesafe.akka" %% "akka-http" % akkaHttpVersion
    val persistenceCassandra = "com.typesafe.akka" %% "akka-persistence-cassandra" % "1.0.6"
    val persistenceTestkit = "com.typesafe.akka" %% "akka-persistence-testkit" % akkaVersion
    val persistenceTyped = "com.typesafe.akka" %% "akka-persistence-typed" % akkaVersion
    val stream = "com.typesafe.akka" %% "akka-stream" % akkaVersion
    val streamTestkit = "com.typesafe.akka" %% "akka-stream-testkit" % akkaVersion % Test
    val streamTyped = "com.typesafe.akka" %% "akka-stream-typed" % akkaVersion
    val testkit = "com.typesafe.akka" %% "akka-testkit" % akkaVersion
    val testkitTyped = "com.typesafe.akka" %% "akka-actor-testkit-typed" % akkaVersion
    val typed = "com.typesafe.akka" %% "akka-actor-typed" % akkaVersion

    val httpCirce = "de.heikoseeberger" %% "akka-http-circe" % "1.35.3"

    val overrides = Seq(
      "akka-http",
      "akka-http-core",
      "akka-http-spray-json",
      "akka-parsing",
      "akka-http-xml"
    ).map("com.typesafe.akka" %% _ % akkaHttpVersion) ++
      Seq(
        "akka-actor",
        "akka-actor-testkit-typed",
        "akka-actor-typed",
        "akka-cluster",
        "akka-cluster-sharding",
        "akka-cluster-sharding-typed",
        "akka-cluster-tools",
        "akka-cluster-typed",
        "akka-coordination",
        "akka-discovery",
        "akka-distributed-data",
        "akka-persistence",
        "akka-persistence-query",
        "akka-persistence-typed",
        "akka-protobuf-v3",
        "akka-remote",
        "akka-serialization-jackson",
        "akka-slf4j",
        "akka-stream",
        "akka-stream-typed",
        "akka-stream-testkit"
      ).map("com.typesafe.akka" %% _ % akkaVersion)

    object projection {
      private val projectionVersion = "1.2.5"

      val eventsourced = "com.lightbend.akka" %% "akka-projection-eventsourced" % projectionVersion
      val cassandra = "com.lightbend.akka" %% "akka-projection-cassandra" % projectionVersion
      val testKit = "com.lightbend.akka" %% "akka-projection-testkit" % projectionVersion
    }

    object js {
      val stream = Def.setting("org.akka-js" %%% "akkajsactorstream" % akkaJs)
    }
  }

  object macwire {
    val macwireVersion = "2.5.8"

    val macros = "com.softwaremill.macwire" %% "macros" % macwireVersion % "provided"
    val macrosAkka = "com.softwaremill.macwire" %% "macrosakka" % macwireVersion % "provided"
    val util = "com.softwaremill.macwire" %% "util" % macwireVersion
  }

  object circe {
    private val circeVersion = "0.14.3"

    val core = Def.setting("io.circe" %%% "circe-core" % circeVersion)
    val generic = Def.setting("io.circe" %%% "circe-generic" % circeVersion)
    val genericExtras = Def.setting("io.circe" %%% "circe-generic-extras" % "0.14.2")
    val parser = Def.setting("io.circe" %%% "circe-parser" % circeVersion)
  }

  object logstage {
    private val izumiVersion = "1.0.10"

    val core = Def.setting("io.7mind.izumi" %%% "logstage-core" % izumiVersion)
    val circe = Def.setting("io.7mind.izumi" %%% "logstage-rendering-circe" % izumiVersion)
    val fromSlf4j = "io.7mind.izumi" %% "logstage-adapter-slf4j" % izumiVersion
    val toSlf4j = "io.7mind.izumi" %% "logstage-sink-slf4j" % izumiVersion
  }

  object cats {
    val core = Def.setting("org.typelevel" %%% "cats-core" % "2.8.0")
  }
}
