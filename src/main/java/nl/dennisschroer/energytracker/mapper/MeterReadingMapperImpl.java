package nl.dennisschroer.energytracker.mapper;

import nl.dennisschroer.energytracker.controller.MeterReadingController;
import nl.dennisschroer.energytracker.entity.MeterReadingEntity;
import nl.dennisschroer.energytracker.model.MeterReading;
import nl.dennisschroer.energytracker.model.MeterReadingResource;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.time.ZoneOffset;
import java.util.Date;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class MeterReadingMapperImpl implements MeterReadingMapper {
    @Override
    public @NotNull MeterReadingResource mapToResource(@NotNull MeterReadingEntity entity) {
        MeterReadingResource resource = new MeterReadingResource();

        resource.setId(entity.getId());
        resource.setElectricityLow(entity.getElectricityLow());
        resource.setElectricityNormal(entity.getElectricityNormal());
        resource.setGas(entity.getGas());
        resource.setWater(entity.getWater());
        resource.setTimestamp(entity.getTimestamp().toInstant().atOffset(ZoneOffset.UTC));
        resource.setCreationDate(entity.getCreationDate().toInstant().atOffset(ZoneOffset.UTC));

        resource.add(linkTo(methodOn(MeterReadingController.class).getMeterReading(entity.getId())).withSelfRel());

        return resource;
    }

    @Override
    public @NotNull MeterReadingEntity mapToEntity(@NotNull MeterReading meterReading) {
        MeterReadingEntity entity = new MeterReadingEntity();

        entity.setElectricityLow(meterReading.getElectricityLow());
        entity.setElectricityNormal(meterReading.getElectricityNormal());
        entity.setGas(meterReading.getGas());
        entity.setWater(meterReading.getWater());
        entity.setTimestamp(Date.from(meterReading.getTimestamp().toInstant()));

        return entity;
    }
}
