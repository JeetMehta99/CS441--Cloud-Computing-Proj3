package Server

import com.typesafe.config.{Config, ConfigFactory}
import io.grpc.examples.helloworld.LogFileReader.GreeterGrpc.GreeterBlockingStub
import io.grpc.examples.helloworld.LogFileReader.{GreeterGrpc, Input, Response}
import io.grpc.{ManagedChannel, ManagedChannelBuilder, Server, ServerBuilder, StatusRuntimeException}

import java.util.concurrent.TimeUnit
import java.util.logging.{Level, Logger}
import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success, Try}

/**
 * Implements the Log Function gRPC service.
 */

object lambdaGrpcServer {

  private val logger = Logger.getLogger(classOf[lambdaGrpcServer].getName)
  val config: Config = ConfigFactory.load("parameters")
  val port: Int = config.getInt("parameter.port")
  val url: String = config.getString("parameter.url")
  def main(args: Array[String]): Unit = {
    val server = new lambdaGrpcServer(ExecutionContext.global)
    startServer(server)
    blockServerUntilShutdown(server)
  }

  def startServer(server: lambdaGrpcServer): Unit = {
    server.start()
  }

  def blockServerUntilShutdown(server: lambdaGrpcServer): Unit =  {
    server.blockUntilShutdown()
  }
}

class lambdaGrpcServer(executionContext: ExecutionContext) {
  self =>
  private[this] var server: Server = null
  private val logger = Logger.getLogger(classOf[lambdaGrpcServer].getName)

  private def start(): Unit = {
    server = ServerBuilder.forPort(lambdaGrpcServer.port).addService(GreeterGrpc.bindService(new Logimpl, executionContext)).build.start
    logger.info("Server started, listening on " + lambdaGrpcServer.port)
    sys.addShutdownHook {
      System.err.println("shutting down gRPC server since JVM is shutting down")
      self.stop()
      System.err.println("server shut down")
    }
  }

  private def stop(): Unit = {
    if (server != null) {
      server.shutdown()
    }
  }

  private def blockUntilShutdown(): Unit = {
    if (server != null) {
      server.awaitTermination()
    }
  }

  class Logimpl extends GreeterGrpc.Greeter {
    override def logFinder(req: Input): Future[Response] = {
      //Call Lambda API Gateway

      Try(scala.io.Source.fromURL(lambdaGrpcServer.url + "/grpc?bucket=" + req.bucket + "&key=" + req.key + "&given_timestamp=" + req.givenTimestamp + "&interval=" + req.interval + "&pattern=" + req.pattern)) match {
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
}