package nl.dennisschroer.energytracker.controller;

import nl.dennisschroer.energytracker.api.MeterReadingsApi;
import nl.dennisschroer.energytracker.entity.MeterReadingEntity;
import nl.dennisschroer.energytracker.mapper.MeterReadingMapper;
import nl.dennisschroer.energytracker.model.MeterReading;
import nl.dennisschroer.energytracker.model.MeterReadingCollection;
import nl.dennisschroer.energytracker.model.MeterReadingCollectionEmbedded;
import nl.dennisschroer.energytracker.model.MeterReadingResource;
import nl.dennisschroer.energytracker.repository.MeterReadingRepository;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Controller
public class MeterReadingController implements MeterReadingsApi {
    private final MeterReadingRepository meterReadingRepository;

    private final MeterReadingMapper meterReadingMapper;

    public MeterReadingController(MeterReadingRepository meterReadingRepository, MeterReadingMapper meterReadingMapper) {
        this.meterReadingRepository = meterReadingRepository;
        this.meterReadingMapper = meterReadingMapper;
    }

    @Override
    public ResponseEntity<MeterReadingCollection> getMeterReadings() {
        List<MeterReadingResource> meterReadings = meterReadingRepository.findAll().stream()
                .map(meterReadingMapper::mapToResource)
                .collect(Collectors.toList());

        MeterReadingCollection response = new MeterReadingCollection();
        MeterReadingCollectionEmbedded embedded = new MeterReadingCollectionEmbedded();
        embedded.setMeterReadings(meterReadings);
        response.setEmbedded(embedded);

        response.add(linkTo(methodOn(MeterReadingController.class).getMeterReadings()).withSelfRel());

        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<MeterReadingResource> getMeterReading(UUID meterReadingId) {
        return null;
    }

    @Override
    public ResponseEntity<MeterReadingResource> createMeterReading(@Valid MeterReading meterReading) {
        MeterReadingEntity entity = meterReadingMapper.mapToEntity(meterReading);
        entity = meterReadingRepository.save(entity);

        MeterReadingResource resource = meterReadingMapper.mapToResource(entity);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .contentType(MediaTypes.HAL_JSON)
                .header(HttpHeaders.LOCATION,
                        linkTo(methodOn(MeterReadingController.class).getMeterReading(entity.getId())).toUri().toString())
                .body(resource);
    }
}
