package Simulations

import baseConfig.BaseSimulation
import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._


class rampUpAndDown extends BaseSimulation {
  val scn = scenario(scenarioName = "Video Game DB")
    .exec(http(requestName = "Get All Video Games - 1st call")
      .get("videogames")
      .check(status.is(200)))
    .pause(2)

    .exec(http(requestName = "Get Specific Game")
      .get("videogames/1")
      .check(status.is(200)))
    .pause(1, 3)

    .exec(http(requestName = "Get All Video Games - 2nd Call")
      .get("videogames")
      .check(status.is(200)))
    .pause(2000.milliseconds)

/*Below is the scenario that ramps up users..runs for some time and then ramps down users
  * instead of using "maxDuration"- define the run duration in "constantConcurrentUsers step!*/

  setUp(
    scn.inject(
      rampConcurrentUsers(1) to (10) during(20 seconds),
      constantConcurrentUsers(10) during (2 minutes),
      rampConcurrentUsers(10) to (1) during(20 seconds)
    ).protocols(httpConf)
  ).assertions(global.successfulRequests.percent.gt(99))
}
