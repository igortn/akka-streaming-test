package sns

import akka.Done
import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.alpakka.sns.scaladsl.SnsPublisher
import akka.stream.scaladsl.Source
import com.amazonaws.auth.{AWSStaticCredentialsProvider, BasicAWSCredentials}
import com.amazonaws.services.sns.{AmazonSNSAsync, AmazonSNSAsyncClientBuilder}
import sns.Main.Config

import scala.concurrent.Future

class Connector(config: Config) {

  implicit val system: ActorSystem = ActorSystem()
  implicit val materializer: ActorMaterializer = ActorMaterializer()

  private val credentials = new BasicAWSCredentials(
    config.accessKey,
    config.secretKey
  )

  implicit val snsClient: AmazonSNSAsync = AmazonSNSAsyncClientBuilder
    .standard()
    .withRegion(config.region)
    .withCredentials(new AWSStaticCredentialsProvider(credentials))
    .build()

  def publishToSns(message: String): Future[Done] =
    Source
      .single(message)
    .runWith(SnsPublisher.sink(config.topicArn))

  def shutdown(): Unit = {
    materializer.shutdown()
    system.terminate()
  }

}

