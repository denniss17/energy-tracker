package nl.dennisschroer.energytracker.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.OffsetDateTime;
import java.util.Date;
import java.util.UUID;

@Data
@Entity(name = "meter_readings")
@EntityListeners(AuditingEntityListener.class)
public class MeterReadingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @CreatedDate
    private Date timestamp;

    private double electricityNormal;
    private double electricityLow;
    private double gas;
    private double water;
}
