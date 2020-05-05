package nl.dennisschroer.energytracker.controller;

import nl.dennisschroer.energytracker.api.MeterReadingsApi;
import nl.dennisschroer.energytracker.model.MeterReadingCollection;
import nl.dennisschroer.energytracker.model.MeterReadingCollectionEmbedded;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import java.util.Collections;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Controller
public class MeterReadingController implements MeterReadingsApi {
    @Override
    public ResponseEntity<MeterReadingCollection> getMeterReadings() {
        MeterReadingCollection response = new MeterReadingCollection();

        response.add(linkTo(methodOn(MeterReadingController.class).getMeterReadings()).withSelfRel());

        MeterReadingCollectionEmbedded embedded = new MeterReadingCollectionEmbedded();
        embedded.setMeterReadings(Collections.emptyList());
        response.setEmbedded(embedded);

        return ResponseEntity.ok(response);
    }
}
