package Rest

import com.typesafe.config.{Config, ConfigFactory}
import io.grpc.examples.helloworld.LogFileReader.Response

import scala.concurrent.Future
import scala.util.{Failure, Success, Try}
import java.util.concurrent.TimeUnit
import java.util.logging.{Level, Logger}

object RestClient {

  def main(args: Array[String]): Unit = {
    val config: Config = ConfigFactory.load("parameters")
    val bucket: String = config.getString("parameter.bucket")
    val key: String = config.getString("parameter.key")
    val pattern: String = config.getString("parameter.pattern")
    val port: Int = config.getInt("parameter.port")
    val given_timestamp: String = config.getString("parameter.given_timestamp")
    val interval: String = config.getString("parameter.interval")
    val url: String = config.getString("parameter.url")

    Try(scala.io.Source.fromURL(url + "/rest?bucket=" + bucket + "&key=" + key + "&given_timestamp=" + given_timestamp + "&interval=" + interval + "&pattern=" + pattern)) match {
      case Success(response) => {
        val result = response.mkString
        val reply = Response(message = result)
        response.close()
        Future.successful(reply)
      }
      case Failure(response) => {
        val result = "Timestamp not found"
        val reply = Response(message = result)
        Future.successful(reply)
      }
    }
  }
}
