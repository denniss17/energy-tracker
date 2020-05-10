package nl.dennisschroer.energytracker.controller

import io.restassured.RestAssured
import nl.dennisschroer.energytracker.entity.MeterReadingEntity
import nl.dennisschroer.energytracker.factory.MeterReadingEntityFactory
import nl.dennisschroer.energytracker.factory.MeterReadingFactory
import nl.dennisschroer.energytracker.repository.MeterReadingRepository
import org.spockframework.spring.SpringBean
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import spock.lang.Specification

import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

import static io.restassured.RestAssured.given

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MeterReadingControllerTest extends Specification {

    @SpringBean
    private MeterReadingRepository meterReadingRepository = Mock()

    @LocalServerPort
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
        def meterReadingEntity = MeterReadingEntityFactory.instance.build(id: UUID.randomUUID())
        meterReadingRepository.findAll() >> [meterReadingEntity]

        when:
        def response = given().when().get("/meter-readings")

        then: "status code is OK"
        def body = response.body()
        response.statusCode == 200
        response.contentType == MediaType.APPLICATION_JSON_VALUE

        and: "links contains reference to self"
        body.path("_links.self.href") == "http://localhost:$port/meter-readings"

        and: "list of embedded meter readings contains 1 meter reading"
        (body.path("_embedded.meterReadings") as List).size() == 1
        body.path("_embedded.meterReadings[0].id") == meterReadingEntity.id.toString()
        body.path("_embedded.meterReadings[0]._links.self.href") == "http://localhost:$port/meter-readings/${meterReadingEntity.id}"
        body.path("_embedded.meterReadings[0].electricityLow") as Double == meterReadingEntity.electricityLow
        body.path("_embedded.meterReadings[0].timestamp") == formatDate(meterReadingEntity.timestamp)
    }

    def "POST /meter-readings with empty request body returns a bad request error"() {
        when:
        def response = given().when()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .post("/meter-readings")

        then: "status code is BAD REQUEST"
        response.peek()
        response.contentType == MediaType.APPLICATION_PROBLEM_JSON_VALUE
        response.statusCode == HttpStatus.BAD_REQUEST.value()

        and: "body contains problem"
        def body = response.body()
        body.path("title") == "Bad Request"
        body.path("status") == HttpStatus.BAD_REQUEST.value()
    }

    def "POST /meter-readings with meter reading creates a meter reading"() {
        given:
        def id = UUID.randomUUID()
        def creationDate = new Date()
        def meterReading = MeterReadingFactory.instance.build()

        when:
        def response = given()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body(meterReading)
                .post("/meter-readings")

        then: "entity is saved"
        1 * meterReadingRepository.save(_) >> { MeterReadingEntity it ->
            it.creationDate = creationDate
            it.id = id
            it
        }

        and: "status code is CREATED"
        response.statusCode == HttpStatus.CREATED.value()
        response.contentType == MediaType.APPLICATION_JSON_VALUE
        response.header(HttpHeaders.LOCATION) == "http://localhost:$port/meter-readings/$id"

        and: "response body contains meter reading"
        def body = response.body()
        body.path("electricityNormal") as Double == meterReading.electricityNormal
        body.path("electricityLow") as Double == meterReading.electricityLow
        body.path("gas") as Double == meterReading.gas
        body.path("water") as Double == meterReading.water
        body.path("timestamp") == formatDate(meterReading.timestamp)
        body.path("id") == id.toString()
        body.path("creationDate") == formatDate(creationDate)

        and: "response body contains link to self"
        body.path("_links.self.href") == "http://localhost:$port/meter-readings/$id"
    }

    def formatDate(OffsetDateTime date){
        date.toLocalDateTime().atOffset(ZoneOffset.UTC).format(DateTimeFormatter.ISO_OFFSET_DATE_TIME)
    }

    def formatDate(Date date){
        date.toLocalDateTime().atOffset(ZoneOffset.UTC).format(DateTimeFormatter.ISO_OFFSET_DATE_TIME)
    }
}
