package ClientSideTests

import com.typesafe.config.{Config, ConfigFactory}

import scala.concurrent.ExecutionContext
import scala.util.{Failure, Success, Try}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import Client.lambdaGrpcClient
import Server.lambdaGrpcServer
import Rest.RestClient
import sun.font.TrueTypeFont

class ClientTest extends AnyFlatSpec with Matchers{
  val config: Config = ConfigFactory.load("parameters")
  val bucket: String = config.getString("parameter.bucket")
  val key: String = config.getString("parameter.key")
  val pattern: String = config.getString("parameter.pattern")
  val url: String = config.getString("parameter.url")
  val pattern: String = "([a-c][e-g][0-3]|[A-Z][5-9][f-w]){5,15}"

  //Port Test
  it should "obtain port number" in {
    config.getInt("parameter.port") === 50058
  }

  //Time present or not

  it should "also have Time" in {
    Try(scala.io.Source.fromURL(url + "/GRPC?bucket=" + bucket + "&key=" + key + "&interval" + interval + "&pattern" + pattern)) match{
      case Success(response) => {
        True
        case Failure(response) => {
        }
        False
      }
    }
  }

  // Type test
  it should "obtain the Pattern" in {
    val config: Config = ConfigFactory.load("application.conf")
    config.getString("randomLogGenerator.Pattern") shouldBe String
  }
}