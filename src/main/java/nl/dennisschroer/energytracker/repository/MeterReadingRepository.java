package nl.dennisschroer.energytracker.repository;

import nl.dennisschroer.energytracker.entity.MeterReadingEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface MeterReadingRepository extends JpaRepository<MeterReadingEntity, UUID> {
}
