package Client

import com.typesafe.config.{Config, ConfigFactory}
import io.grpc.examples.helloworld.LogFileReader.GreeterGrpc.GreeterBlockingStub
import io.grpc.examples.helloworld.LogFileReader.{GreeterGrpc, Input}
import io.grpc.{ManagedChannel, ManagedChannelBuilder, StatusRuntimeException}
import java.util.concurrent.TimeUnit
import java.util.logging.{Level, Logger}

object lambdaGrpcClient {
  private[this] val logger = Logger.getLogger(classOf[lambdaGrpcClient].getName)
  val config: Config = ConfigFactory.load("parameters")
  val bucket: String = config.getString("parameter.bucket")
  val key: String = config.getString("parameter.key")
  val pattern: String = config.getString("parameter.pattern")
  val port: Int = config.getInt("parameter.port")
  val given_timestamp: String = config.getString("parameter.given_timestamp")
  val interval: String = config.getString("parameter.interval")

  /**
   *
   * @param host localhost
   * @param port port given in parameters.conf
   * @return
   */
  def apply(host: String, port: Int): lambdaGrpcClient = {
    val channel = ManagedChannelBuilder.forAddress(host, port).usePlaintext().build
    val blockingStub = GreeterGrpc.blockingStub(channel)
    new lambdaGrpcClient(channel, blockingStub)
  }

  def main(args: Array[String]): Unit = {
    val client = lambdaGrpcClient("localhost", port)
    try {
      client.callServer(bucket, key, given_timestamp, interval, pattern)
    }
    finally {
      client.stop()
    }
  }
}

class lambdaGrpcClient private(private val channel: ManagedChannel, private val blockingStub: GreeterBlockingStub) {
  private[this] val logger = Logger.getLogger(classOf[lambdaGrpcClient].getName)

  def stop(): Unit = {
    channel.shutdown.awaitTermination(5, TimeUnit.SECONDS)
  }

  /**
   *
   * @param given_timestamp timestamp provided by the client
   * @param interval the interval which helps to search log statements
   */
  def callServer(bucket: String, key:String, given_timestamp: String, interval: String, pattern: String): Unit = {
    val request = Input(bucket, key, given_timestamp, interval, pattern)
    try{
      val response = blockingStub.logFinder(request)
      logger.info("Result= " + response.message)
      response.message
    }
    catch {
      case e:StatusRuntimeException =>
        logger.log(Level.WARNING, "RPC failed: {0}", e.getStatus)
    }
  }
}
