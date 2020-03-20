package Simulations

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import baseConfig.BaseSimulation
import scala.concurrent.duration._
import io.gatling.core.structure.ChainBuilder

//In this script we're simlifying below scripts into chain building. exec calls 1 and 3 are same, so we can use one call

class codeReuseWithObjects extends BaseSimulation {

  /*  val scn = scenario(scenarioName = "Video Game DB")

    .exec(http(requestName = "Get All Video Games")
      .get("videogames")
      .check(status.is(expected = 200)))

    .exec(http(requestName = "Get Specific Game")
      .get("videogames/1")
      .check(status.in(200 to 210)))

    .exec(http(requestName = "Get All Video Games")
      .get("videogames")
      .check(status.is(expected = 200)))*/

  /** below code does the same thing as above but a little bit neatly!  **/

  def getAllVideoGames(): ChainBuilder = {
    exec(http(requestName = "Get All Video Games")
      .get("videogames")
      .check(status.is(expected = 200)))
  }

  def getSpecificVideoGame(): ChainBuilder = {
    exec(http(requestName = "Get Specific Game")
      .get("videogames/1")
      .check(status.in(200 to 210)))
  }

  val scn = scenario(scenarioName = "Get all video Games")
    .exec(getAllVideoGames())
    .pause(2)
    .exec(getSpecificVideoGame())
    .pause(1)
    .exec(getAllVideoGames())

  setUp(
    scn.inject(
      rampUsers(10) during (5)
  ).protocols(httpConf))
}
