package primefactorkata

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._

class PrimeFactorKata extends Simulation {
    val target = System.getProperty("gatling.target", "http://localhost:8080")
    val maxUsers = System.getProperty("gatling.users", "50").toInt
    val userRampUp = System.getProperty("gatling.userRampUp", "90").toInt
    val duration = System.getProperty("gatling.duration", "120").toInt


    val httpProtocol = http
        .baseUrl(target) 
        .disableCaching
        .acceptHeader("application/json") 

    val feeder = jsonFile("numbers.json").random

    val scn = scenario("Example Scenario")
        .forever() {
            feed(feeder)
            .exec(
                http("factor_${n}")
                    .get("/generate/${n}")
                    .check(
                        status.is(200),
                        jsonPath("$").ofType[Seq[Any]].is("${factors}"),
                    )
            )
        }

    setUp(
        scn.inject(rampUsers(maxUsers) during (userRampUp seconds))
    )
    .maxDuration(duration seconds)
    .protocols(httpProtocol)
}