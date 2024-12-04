CREATE TABLE sensors (
                         id BIGSERIAL PRIMARY KEY,
                         name VARCHAR(255) NOT NULL,
                         model VARCHAR(255) NOT NULL,
                         type VARCHAR(50),
                         unit VARCHAR(50),
                         range_from DECIMAL,
                         range_to DECIMAL
);