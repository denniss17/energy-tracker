package nl.dennisschroer.energytracker.mapper

import nl.dennisschroer.energytracker.factory.MeterReadingEntityFactory
import nl.dennisschroer.energytracker.factory.MeterReadingFactory
import spock.lang.Specification

import java.time.OffsetDateTime
import java.time.ZoneOffset

class MeterReadingMapperImplTest extends Specification {
    def "mapToResource maps an MeterReadingEntity to a MeterReadingResource"() {
        given:
        def mapper = new MeterReadingMapperImpl()
        def entity = MeterReadingEntityFactory.instance.build()

        when:
        def meterReading = mapper.mapToResource(entity)

        then:
        with(meterReading) {
            id == entity.id
            electricityLow == entity.electricityLow
            electricityNormal == entity.electricityNormal
            gas == entity.gas
            water == entity.water
            timestamp == entity.timestamp.toInstant().atOffset(ZoneOffset.UTC)
            creationDate == entity.creationDate.toInstant().atOffset(ZoneOffset.UTC)
        }
    }

    def "mapToEntity maps an MeterReading to a MeterReadingEntity"() {
        given:
        def mapper = new MeterReadingMapperImpl()
        def meterReading = MeterReadingFactory.instance.build()

        when:
        def entity = mapper.mapToEntity(meterReading)

        then:
        with(entity) {
            id == null
            electricityLow == meterReading.electricityLow
            electricityNormal == meterReading.electricityNormal
            gas == meterReading.gas
            water == meterReading.water
            timestamp == Date.from(meterReading.timestamp.toInstant())
            creationDate == null
        }
    }
}
