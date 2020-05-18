package Simulations

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._
import baseConfig.BaseSimulation

class closedModelSimulations extends BaseSimulation {

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
//We added forever block ^^ above scenario, so we can add maxDuration injection below!
  //This is a closed load model below
  setUp(
    scn.inject(
      // ***all these patterns are using CLOSED load injection model ***
      //nothingFor(3 seconds),
      //constant conccurent users - are constant
      //constantConcurrentUsers(10) during(40 seconds),
      //ramp conccurent users - ramps linearly
      rampConcurrentUsers(2) to (20) during(40 seconds),
    ).protocols(httpConf)
  ).maxDuration(2 minutes)
    .assertions(global.successfulRequests.percent.gt(99),
      global.responseTime.max.lt(1000))

}
