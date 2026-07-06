-- Rooms table
CREATE TABLE IF NOT EXISTS rooms (
    id              INT AUTO_INCREMENT PRIMARY KEY,
    type            VARCHAR(50)   NOT NULL,
    name            VARCHAR(100)  NOT NULL,
    description     TEXT,
    price_per_night DECIMAL(10,2) NOT NULL,
    capacity        INT           NOT NULL
);

-- Sample rooms
INSERT INTO rooms (type, name, description, price_per_night, capacity) VALUES
    ('Deluxe', 'Deluxe Mountain View', 'Stone floors, linen curtains, private terrace, king bed.', 220.00, 2),
    ('Suite',  'Junior Suite',         'Living room with fireplace, canopied bed, private courtyard.', 380.00, 3),
    ('Villa',  'Garden Villa',         'Private walled garden, small pool, two bedrooms.', 680.00, 4);
