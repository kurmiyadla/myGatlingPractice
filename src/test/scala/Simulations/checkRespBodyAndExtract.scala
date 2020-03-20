package Simulations

import baseConfig.BaseSimulation

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._

class checkRespBodyAndExtract extends BaseSimulation{

  val scn = scenario (scenarioName = "VideoGames DB")

    .exec(http(requestName = "Get Specific Video Game")
     .get("videogames/1")
      .check(jsonPath(path = "$.name").is(expected = "Resident Evil 4"))
     .check(status.is(200)))

      .exec(http(requestName = "Get All Video Games")
       .get("videogames")
        .check(jsonPath(path = "$[1].id").saveAs(key = "gameId"))
       .check(status.in(200, 210)))
        //with following code, we're extracting session info from above call
      .exec{session => println(session); session}

      .exec(http(requestName = "Get Specific game w/ extracted game id")
       .get("videogames/${gameId}")
       .check(jsonPath(path = "$.name").is(expected = "Gran Turismo 3"))
       .check(bodyString.saveAs("responsebody"))
       .check(status.not(404), status.not(500)))
       .exec{ session => println(session("responsebody").as[String]); session}


  setUp(
    scn.inject(atOnceUsers(1)
    ).protocols(httpConf))
     .assertions(global.responseTime.max.lt(3000))
}
