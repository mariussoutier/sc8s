package net.sc8s.circe.lagom

import akka.actor.ActorSystem
import akka.serialization.circe.CirceSerializerRegistry
import com.lightbend.lagom.scaladsl.server.LagomApplication
import com.typesafe.config.Config
import play.api.{Configuration, Environment}

trait CirceAkkaSerializationComponents {
  _: LagomApplication =>

  val circeSerializerRegistry: CirceSerializerRegistry

  override lazy val actorSystem: ActorSystem =
    ActorSystemProvider.start(config, environment, circeSerializerRegistry)
}

object ActorSystemProvider {
  def start(
             config: Config,
             environment: Environment,
             serializerRegistry: CirceSerializerRegistry
           ): ActorSystem = {
    val serializationSetup =
      CirceSerializerRegistry.serializationSetupFor(serializerRegistry)

    play.api.libs.concurrent.ActorSystemProvider.start(
      environment.classLoader,
      Configuration(config),
      serializationSetup
    )
  }
}

