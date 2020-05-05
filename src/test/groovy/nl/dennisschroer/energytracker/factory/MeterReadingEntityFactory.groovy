package nl.dennisschroer.energytracker.factory

import nl.dennisschroer.energytracker.entity.MeterReadingEntity
import nl.topicus.overheid.javafactorybot.Factory
import nl.topicus.overheid.javafactorybot.definition.Attribute

import java.time.OffsetDateTime
import java.util.concurrent.TimeUnit

@Singleton
class MeterReadingEntityFactory extends Factory<MeterReadingEntity> {
    Map<String, Attribute> attributes = [
            electricityNormal: value { faker.random().nextInt(1000) },
            electricityLow   : value { faker.random().nextInt(1000) },
            gas              : value { faker.random().nextInt(1000) },
            water            : value { faker.random().nextDouble() },
            timestamp        : value { faker.date().past(10, TimeUnit.DAYS) }
    ]
}
