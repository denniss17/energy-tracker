package nl.dennisschroer.energytracker.mapper;

import nl.dennisschroer.energytracker.entity.MeterReadingEntity;
import nl.dennisschroer.energytracker.model.MeterReading;

import javax.validation.constraints.NotNull;

public interface MeterReadingMapper {
    @NotNull MeterReading mapToDto(@NotNull MeterReadingEntity entity);
}
