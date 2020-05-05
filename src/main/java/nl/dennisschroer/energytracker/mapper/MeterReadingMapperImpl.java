package nl.dennisschroer.energytracker.mapper;

import nl.dennisschroer.energytracker.entity.MeterReadingEntity;
import nl.dennisschroer.energytracker.model.MeterReading;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.time.ZoneOffset;

@Service
public class MeterReadingMapperImpl implements MeterReadingMapper {
    @Override
    public @NotNull MeterReading mapToDto(@NotNull MeterReadingEntity entity) {
        MeterReading meterReading = new MeterReading();
        meterReading.setId(entity.getId());
        meterReading.setElectricityLow(entity.getElectricityLow());
        meterReading.setElectricityNormal(entity.getElectricityNormal());
        meterReading.setGas(entity.getGas());
        meterReading.setWater(entity.getWater());
        meterReading.setTimestamp(entity.getTimestamp().toInstant().atOffset(ZoneOffset.UTC));

        return meterReading;
    }
}
