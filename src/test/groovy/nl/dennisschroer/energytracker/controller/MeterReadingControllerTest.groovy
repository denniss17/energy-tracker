package nl.dennisschroer.energytracker.controller

import io.restassured.RestAssured
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification

import static io.restassured.RestAssured.given

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MeterReadingControllerTest extends Specification {

    @Value('${local.server.port}')
    private int port

    def setup() throws Exception {
        RestAssured.port = port
    }

    def "GET /meter-readings returns an empty collection by default"() {
        given:
        def request = given()

        when:
        def response = request.when().get("/meter-readings")

        then: "status code is OK"
        response.statusCode == 200

        and: "links contains reference to self"
        response.body().path("_links.self.href") == "http://localhost:$port/meter-readings"

        and: "list of embedded meter readings is empty"
        List meterReadings =  response.body().path("_embedded.meterReadings")
        meterReadings.isEmpty()
    }
}
