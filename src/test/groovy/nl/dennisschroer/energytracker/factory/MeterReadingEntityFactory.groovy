package nl.dennisschroer.energytracker.factory

import nl.dennisschroer.energytracker.entity.MeterReadingEntity
import nl.topicus.overheid.javafactorybot.Factory
import nl.topicus.overheid.javafactorybot.definition.Attribute

import java.util.concurrent.TimeUnit

@Singleton
class MeterReadingEntityFactory extends Factory<MeterReadingEntity> {
    Map<String, Attribute> attributes = [
            electricityNormal: value { faker.random().nextDouble().round(3) },
            electricityLow   : value { faker.random().nextDouble().round(3) },
            gas              : value { faker.random().nextDouble().round(3) },
            water            : value { faker.random().nextDouble().round(3) },
            timestamp        : value { faker.date().past(10, TimeUnit.DAYS) },
            creationDate     : value { faker.date().past(5, TimeUnit.DAYS) }
    ]
}
