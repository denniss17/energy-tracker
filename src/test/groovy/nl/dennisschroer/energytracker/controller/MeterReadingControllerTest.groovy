package nl.dennisschroer.energytracker.controller

import io.restassured.RestAssured
import nl.dennisschroer.energytracker.factory.MeterReadingEntityFactory
import nl.dennisschroer.energytracker.repository.MeterReadingRepository
import org.spockframework.spring.SpringBean
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification

import java.time.ZoneOffset

import static io.restassured.RestAssured.given

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MeterReadingControllerTest extends Specification {

    @SpringBean
    private MeterReadingRepository meterReadingRepository = Mock()

    @Value('${local.server.port}')
    private int port

    def setup() throws Exception {
        RestAssured.port = port
    }

    def "GET /meter-readings returns an empty collection by default"() {
        given:
        def request = given()
        meterReadingRepository.findAll() >> []

        when:
        def response = request.when().get("/meter-readings")

        then: "status code is OK"
        response.statusCode == 200

        and: "links contains reference to self"
        response.body().path("_links.self.href") == "http://localhost:$port/meter-readings"

        and: "list of embedded meter readings is empty"
        List meterReadings = response.body().path("_embedded.meterReadings")
        meterReadings.isEmpty()
    }

    def "GET /meter-readings returns a collection of meter readings"() {
        given:
        def request = given()
        def meterReadingEntity = MeterReadingEntityFactory.instance.build()
        meterReadingRepository.findAll() >> [meterReadingEntity]

        when:
        def response = request.when().get("/meter-readings")

        then: "status code is OK"
        def body = response.body()
        response.statusCode == 200

        and: "links contains reference to self"
        body.path("_links.self.href") == "http://localhost:$port/meter-readings"

        and: "list of embedded meter readings contains 1 meter reading"
        (body.path("_embedded.meterReadings") as List).size() == 1
        body.path("_embedded.meterReadings[0].id") == meterReadingEntity.id
        body.path("_embedded.meterReadings[0].electricityLow") == meterReadingEntity.electricityLow
        body.path("_embedded.meterReadings[0].timestamp") == meterReadingEntity.timestamp.toInstant().atOffset(ZoneOffset.UTC).toString()
    }
}
