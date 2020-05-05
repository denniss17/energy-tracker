
create table meter_readings
(
    id                 uuid             not null
        constraint meter_reading_entity_pkey
            primary key,
    electricity_low    integer          not null,
    electricity_normal integer          not null,
    gas                integer          not null,
    timestamp          timestamp        not null,
    water              double precision not null
);

