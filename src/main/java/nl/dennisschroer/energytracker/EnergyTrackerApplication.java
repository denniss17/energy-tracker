package nl.dennisschroer.energytracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.hateoas.RepresentationModel;

@SpringBootApplication
public class EnergyTrackerApplication {

    public static void main(String[] args) {
        SpringApplication.run(EnergyTrackerApplication.class, args);
    }

}
