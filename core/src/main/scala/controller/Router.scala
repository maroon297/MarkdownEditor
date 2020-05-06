package controller

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import com.maroon297.codegen.definitions.{loginInfo, loginResponse}
import com.maroon297.codegen.login.{LoginHandler, LoginResource}
import models.Editors
import scalikejdbc.config.DBs

import scala.concurrent.Future
import scala.io.StdIn

object Router extends App {
  implicit val system = ActorSystem("my-system")
  implicit  val materializer = ActorMaterializer()
  implicit  val executionContext = system.dispatcher

  DBs.setupAll()

  val loginHandler = new LoginHandler {
    override def login(respond: LoginResource.loginResponse.type)(body: Option[loginInfo]): Future[LoginResource.loginResponse] = {
      body match {
        case Some(info) => {
          val userOption = Editors.find(info.userId)
          userOption match {
            case Some(user) => {
              if (user.password == info.password) {
                Future(respond.OK(loginResponse("ok.")))
              } else {
                Future(respond.OK(loginResponse("password not match.")))
              }
            }
            case None => Future(respond.OK(loginResponse("User not found.")))
          }
        }
        case None => Future(respond.OK(loginResponse("data nothing.")))
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