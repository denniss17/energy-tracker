
create table meter_readings
(
    id                 uuid             not null
        constraint meter_reading_entity_pkey
            primary key,
    electricity_low    double precision not null,
    electricity_normal double precision not null,
    gas                double precision not null,
    water              double precision not null,
    timestamp          timestamp        not null
);

