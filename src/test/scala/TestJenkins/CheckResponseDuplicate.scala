package TestJenkins

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._

class CheckResponseDuplicate extends Simulation {

  val httpConf = http
    .baseUrl("http://localhost:8080/app/")
    .header("Accept", "application/json")

  val scn = scenario (scenarioName = "VideoGames DB")
    .forever{
      exec(http(requestName = "Get Specific Video Game")
        .get("videogames/1")
        .check(jsonPath(path = "$.name").is(expected = "Resident Evil 4"))
        .check(status.is(200)))
        .pause(1)

        .exec(http(requestName = "Get All Video Games")
          .get("videogames")
          .check(jsonPath(path = "$[1].id").saveAs(key = "gameId"))
          .check(status.in(200, 210)))
        //with following code, we're extracting session info from above call
        //     .exec{session => println(session); session}
        .pause(2)

        .exec(http(requestName = "Get Specific game w/ extracted game id")
          .get("videogames/${gameId}")
          .check(jsonPath(path = "$.name").is(expected = "Gran Turismo 3"))
          .check(bodyString.saveAs("responsebody"))
          .check(status.not(404), status.not(500)))
      //    .exec{ session => println(session("responsebody").as[String]); session}
        .pause(1)

    }

/*    setUp(
      scn.inject(atOnceUsers(1)
      ).protocols(httpConf))
      .assertions(global.responseTime.max.lt(3000))*/

  /*** Setup Load Simulation ***/
  setUp(
    scn.inject(
      nothingFor(2 seconds),
      rampUsers(5) during (10 seconds))
  )
    .protocols(httpConf)
    .maxDuration(30 seconds)

}
