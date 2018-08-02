package sns

import com.typesafe.scalalogging.LazyLogging
import scopt.OptionParser

import scala.concurrent.ExecutionContext
import scala.util.{Failure, Success}

object Main extends App with LazyLogging {

  implicit val ec: ExecutionContext = ExecutionContext.global

  case class Config(region: String = "",
                    accessKey: String = "",
                    secretKey: String = "",
                    topicArn: String = "",
                    message: String = "")

  val commandLineParser = new OptionParser[Config]("alpakka-sns") {
    head("alpakka-sns", "0.1")

    opt[String]('r', "region")
      .action((v, c) => c.copy(region = v))
      .required()
      .text("region is required")

    opt[String]('a', "accessKey")
      .action((v, c) => c.copy(accessKey = v))
      .required()
      .text("accessKey is required")

    opt[String]('s', "secretKey")
      .action((v, c) => c.copy(secretKey = v))
      .required()
      .text("secretKey is required")

    opt[String]('t', "topicArn")
      .action((v, c) => c.copy(topicArn = v))
      .required()
      .text("topicArn is required")

    opt[String]('m', "message")
      .action((v, c) => c.copy(message = v))
      .required()
      .text("message is required")

    help("help").text("prints usage text")
  }

  commandLineParser.parse(args, Config()) match {
    case Some(config) =>
      val connector = new Connector(config)

      connector
        .publishToSns(config.message)
          .onComplete {
            case Success(value) => logger.info(s"Success: $value.")
            case Failure(exception) => logger.error(s"Failure: ${exception.getMessage}")
          }

      Thread.sleep(2000)
      logger.info("Shutting down connector.")
      connector.shutdown()

    case None =>
      logger.error("Error parsing command line arguments.")
  }

}
