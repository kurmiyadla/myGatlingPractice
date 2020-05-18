package Simulations

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration.DurationInt
import baseConfig.BaseSimulation

class rampUsersLoadSimulation extends BaseSimulation {
  val scn = scenario(scenarioName = "Video Game DB")
    .forever() {
      exec(http(requestName = "Get All Video Games - 1st call")
        .get("videogames")
        .check(status.is(200)))
        .pause(2)

        .exec(http(requestName = "Get Specific Game")
          .get("videogames/1")
          .check(status.is(200)))
        .pause(1, 2)

        .exec(http(requestName = "Get All Video Games - 2nd Call")
          .get("videogames")
          .check(status.is(200)))
        .pause(1000.milliseconds)
    }
  setUp(
    scn.inject(
      // ***all these patterns are using open load injection model ***
      nothingFor(3 seconds),
      //constant users/sec simulation
      //constantUsersPerSec(2) during(40 seconds)
      //constant users/sec randomized
      constantUsersPerSec(2) during(40 seconds) randomized,
      //ramp users/s
      //rampUsersPerSec(1) to (3) during(40 seconds),
      //heavisideUsers
      //heavisideUsers(20) during (40 seconds)
    ).protocols(httpConf)
  ).maxDuration(5 minutes)
    .assertions(global.successfulRequests.percent.gt(99),
      global.responseTime.max.lt(1000))

}
