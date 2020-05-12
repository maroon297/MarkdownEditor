package controller

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.testkit.ScalatestRouteTest
import com.maroon297.codegen.login.LoginResource
import org.scalatest.FlatSpec
import org.scalatest.matchers.should.Matchers

class RouteHandlerSpec extends FlatSpec with Matchers with ScalatestRouteTest {
  val route = LoginResource.routes( new RouteHandler())
  behavior of "RouteHandler"
  it should "Login success" in {
    Post("/login") ~> route ~> check {
        status shouldEqual StatusCodes.OK
        responseAs[String] should fullyMatch regex ("""\{"token".*\}""")
    }
  }
}
