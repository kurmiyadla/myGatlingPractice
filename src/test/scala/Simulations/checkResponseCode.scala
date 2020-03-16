package Simulations

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration.DurationInt

import baseConfig.BaseSimulation

class checkResponseCode extends BaseSimulation {

  val scn = scenario(scenarioName = "Video Game DB")

    .exec(http(requestName = "Get All Video Games - 1st Call")
     .get("videogames")
      .check(status.is(expected = 200)))

    .exec(http(requestName = "Get Specific Game")
     .get("videogames/1")
      .check(status.in(200 to 210)))

    .exec(http(requestName = "Get All Video Games - 2nd Call")
      .get("videogames")
       .check(status.not(expected = 404), status.not(expected = 500)))

  setUp(
    scn.inject(atOnceUsers(1))
    ).protocols(httpConf)

}
