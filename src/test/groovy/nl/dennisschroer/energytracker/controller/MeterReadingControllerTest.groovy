package nl.dennisschroer.energytracker.controller

import io.restassured.RestAssured
import nl.dennisschroer.energytracker.factory.MeterReadingEntityFactory
import nl.dennisschroer.energytracker.model.MeterReading
import nl.dennisschroer.energytracker.repository.MeterReadingRepository
import org.spockframework.spring.SpringBean
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import spock.lang.Specification

import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

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
        body.path("_embedded.meterReadings[0].timestamp") == meterReadingEntity.timestamp.toInstant().atOffset(ZoneOffset.UTC).format(DateTimeFormatter.ISO_OFFSET_DATE_TIME)
    }

    def "POST /meter-readings with empty request body returns a bad request error"() {
        given:
        def request = given()

        when:
        def response = request.when()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .post("/meter-readings")

        then: "status code is BAD REQUEST"
        response.peek()
        response.contentType == MediaType.APPLICATION_PROBLEM_JSON_VALUE
        response.statusCode == HttpStatus.BAD_REQUEST.value()
        def body = response.body()
        body.path("title") == "Bad Request"
        body.path("status") == HttpStatus.BAD_REQUEST.value()
    }

    def "POST /meter-readings with meter reading creates a meter reading"() {
        given:
        def request = given()
        def meterReadingEntity = MeterReadingEntityFactory.instance.build()
        def meterReading = new MeterReading(
                electricityNormal: meterReadingEntity.electricityNormal,
                electricityLow: meterReadingEntity.electricityLow,
                gas: meterReadingEntity.gas,
                water: meterReadingEntity.water
        )

        when:
        def response = request.when()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body(meterReading)
                .post("/meter-readings")

        then: "status code is CREATED"
        response.peek()
        response.statusCode == HttpStatus.CREATED.value()
        response.header(HttpHeaders.LOCATION) == "http://localhost:$port/meter-readings/1"
    }
}
