package Simulations

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration.DurationInt

import baseConfig.BaseSimulation

class addPauseTime extends BaseSimulation {

  val scn = scenario(scenarioName = "Video Game DB")

    .exec(http(requestName = "Get All Video Games - 1st call")
    .get("videogames"))
    .pause(2)

    .exec(http(requestName = "Get Specific Game")
    .get("videogames/1"))
    .pause(1, 3)

    .exec(http(requestName = "Get All Video Games - 2nd Call")
    .get("videogames"))
    .pause(2000.milliseconds)

    setUp(
    scn.inject(atOnceUsers(1)
    ).protocols(httpConf)
  )

}
