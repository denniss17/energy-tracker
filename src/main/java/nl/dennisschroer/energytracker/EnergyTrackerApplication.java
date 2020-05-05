package nl.dennisschroer.energytracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.hateoas.RepresentationModel;

@EnableJpaAuditing
@SpringBootApplication
public class EnergyTrackerApplication {

    public static void main(String[] args) {
        SpringApplication.run(EnergyTrackerApplication.class, args);
    }

}
