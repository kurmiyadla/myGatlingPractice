package Simulations

import baseConfig.BaseSimulation
import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._


class stepUpLoadModel extends BaseSimulation {
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

  /*Below is the scenario that ramps up users at 5, 10, 15 20 etc.,
  */
  setUp(
    scn.inject(
      incrementUsersPerSec(5)
        .times(3)
        .eachLevelLasting(20 seconds)
        .separatedByRampsLasting(10 seconds)
        .startingFrom(5)
  ).protocols(httpConf)
  ).assertions(global.successfulRequests.percent.gt(99))
}
