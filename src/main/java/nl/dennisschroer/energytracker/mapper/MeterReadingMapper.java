package nl.dennisschroer.energytracker.mapper;

import nl.dennisschroer.energytracker.entity.MeterReadingEntity;
import nl.dennisschroer.energytracker.model.MeterReading;
import nl.dennisschroer.energytracker.model.MeterReadingResource;

import javax.validation.constraints.NotNull;

public interface MeterReadingMapper {
    @NotNull MeterReadingResource mapToResource(@NotNull MeterReadingEntity entity);

    @NotNull MeterReadingEntity mapToEntity(@NotNull MeterReading meterReading);
}
