CREATE TABLE rides (
                       id UUID PRIMARY KEY,
                       rider_id UUID NOT NULL,
                       pickup_lat DECIMAL(9,6),
                       pickup_lng DECIMAL(9,6),
                       drop_lat DECIMAL(9,6),
                       drop_lng DECIMAL(9,6),
                       status VARCHAR(20) NOT NULL,
                       created_at TIMESTAMP NOT NULL
);

CREATE INDEX idx_rides_rider_id ON rides(rider_id);
CREATE INDEX idx_rides_status ON rides(status);
