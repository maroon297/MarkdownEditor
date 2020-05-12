package controller
import com.maroon297.codegen.definitions.{loginInfo, loginResponse}
import com.maroon297.codegen.login.{LoginHandler, LoginResource}
import models.Editors

import scala.concurrent.{ExecutionContextExecutor, Future}

class RouteHandler(implicit executionContext : ExecutionContextExecutor)  extends LoginHandler{
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
