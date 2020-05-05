package nl.dennisschroer.energytracker.mapper

import nl.dennisschroer.energytracker.factory.MeterReadingEntityFactory
import spock.lang.Specification

import java.time.ZoneOffset

class MeterReadingMapperImplTest extends Specification {
    def "mapToDto maps an MeterReadingEntity to an MeterReading"() {
        given:
        def mapper = new MeterReadingMapperImpl()
        def entity = MeterReadingEntityFactory.instance.build()

        when:
        def meterReading = mapper.mapToDto(entity)

        then:
        with(meterReading) {
            id == entity.id
            electricityLow == entity.electricityLow
            electricityNormal == entity.electricityNormal
            gas == entity.gas
            water == entity.water
            timestamp == entity.timestamp.toInstant().atOffset(ZoneOffset.UTC)
        }
    }
}
