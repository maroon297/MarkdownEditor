package controller

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import com.maroon297.codegen.definitions.{loginInfo, loginResponse}
import com.maroon297.codegen.login.{LoginHandler, LoginResource}

import scala.concurrent.Future
import scala.io.StdIn

object Router extends App {
  implicit val system = ActorSystem("my-system")
  implicit  val materializer = ActorMaterializer()
  implicit  val executionContext = system.dispatcher

  val loginHandler = new LoginHandler {
    override def login(respond: LoginResource.loginResponse.type)(body: Option[loginInfo]): Future[LoginResource.loginResponse] = {
      body match {
        case Some(info) => Future(respond.OK(loginResponse(s"userId:${info.userId} password:${info.password}")))
        case None => Future(respond.OK(loginResponse("not found.")))
      }

    }
  }
  val routes = LoginResource.routes(loginHandler)

  val bindingFuture = Http().bindAndHandle(routes, "localhost",8080)

  println(s"Server online at http://localhost:8080/\nPress RETURN to stop...")
  StdIn.readLine() // let it run until user presses return
    bindingFuture
      .flatMap(_.unbind()) // trigger unbinding from the port
      .onComplete(_ => system.terminate()) // and shutdown when done

}