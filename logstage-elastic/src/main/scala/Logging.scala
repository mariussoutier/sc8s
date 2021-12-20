package net.sc8s.logstage.elastic

import izumi.fundamentals.platform.language.CodePositionMaterializer
import izumi.logstage.api.Log.CustomContext
import izumi.logstage.api.Log.Level.Debug
import izumi.logstage.api.rendering.logunits.Styler.PadType
import izumi.logstage.api.rendering.logunits.{Extractor, Renderer, Styler}
import izumi.logstage.api.rendering.{RenderingOptions, StringRenderingPolicy}
import izumi.logstage.api.{IzLogger, Log}
import izumi.logstage.sink.slf4j.LogSinkLegacySlf4jImpl

trait Logging {
  protected def logContext: CustomContext = CustomContext()

  // used for event parameters & context prefixes
  protected lazy val loggerClass: String = this.getClass.getName.takeWhile(_ != '$')

  implicit lazy val log: IzLogger = {
    val renderPolicy =
      if (sys.props.get("logger.izumi.sink").contains("json"))
        LogstageCirceElasticRenderingPolicy(loggerClass)
      else
        new StringRenderingPolicy(RenderingOptions.default, Some(Logging.template))

    IzLogger(Debug, Seq(new LogSinkLegacySlf4jImpl(renderPolicy)))(logContext)
  }

  implicit class IzLoggerTags(log: IzLogger) {
    private[this] val empty = Log.Message.empty

    def traceT(tag: String, message: => Log.Message = empty)(implicit codePositionMaterializer: CodePositionMaterializer) =
      logT(Log.Level.Trace, tag, message)

    def debugT(tag: String, message: => Log.Message = empty)(implicit codePositionMaterializer: CodePositionMaterializer) =
      logT(Log.Level.Debug, tag, message)

    def infoT(tag: String, message: => Log.Message = empty)(implicit codePositionMaterializer: CodePositionMaterializer) =
      logT(Log.Level.Info, tag, message)

    def warnT(tag: String, message: => Log.Message = empty)(implicit codePositionMaterializer: CodePositionMaterializer) =
      logT(Log.Level.Warn, tag, message)

    def errorT(tag: String, message: => Log.Message = empty)(implicit codePositionMaterializer: CodePositionMaterializer) =
      logT(Log.Level.Error, tag, message)

    def critT(tag: String, message: => Log.Message = empty)(implicit codePositionMaterializer: CodePositionMaterializer) =
      logT(Log.Level.Crit, tag, message)

    private[this] def logT(
                            logLevel: Log.Level, tag: String, message: => Log.Message
                          )(
                            implicit codePositionMaterializer: CodePositionMaterializer
                          ) = {
      val maybePrefixedMessage = message match {
        case `empty` => empty
        case nonEmpty => Log.Message(" ") ++ nonEmpty
      }
      log.log(logLevel)(Log.Message(s"$tag") ++ maybePrefixedMessage)
    }
  }
}

object Logging {
  val template: Renderer.Aggregate = new Renderer.Aggregate(Seq(
    new Styler.Colored(
      Console.BLUE,
      Seq(
        new Styler.AdaptivePad(Seq(new Extractor.SourcePosition()), 8, PadType.Left, ' ')
      )
    ),
    Extractor.Space,
    new Styler.TrailingSpace(Seq(new Extractor.LoggerContext())),
    new Extractor.Message(),
  ))
}
