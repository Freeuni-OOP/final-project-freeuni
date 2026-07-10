-- Rooms table (აქ id იქნება კონკრეტული ოთახის ნომერი!)
CREATE TABLE IF NOT EXISTS rooms (
        id              INT PRIMARY KEY,
        type            VARCHAR(50)   NOT NULL,
        name            VARCHAR(100)  NOT NULL,
        description     TEXT,
        price_per_night DECIMAL(10,2) NOT NULL,
        capacity        INT           NOT NULL
    );

INSERT INTO rooms (id, type, name, description, price_per_night, capacity)
VALUES
        (101, 'Deluxe', 'Deluxe Mountain View', 'Stone floors, linen curtains, private terrace, king bed.', 220.00, 2),
        (102, 'Deluxe', 'Deluxe Mountain View', 'Stone floors, linen curtains, private terrace, king bed.', 220.00, 2),
        (103, 'Deluxe', 'Deluxe Mountain View', 'Stone floors, linen curtains, private terrace, king bed.', 220.00, 2),
        (104, 'Deluxe', 'Deluxe Mountain View', 'Stone floors, linen curtains, private terrace, king bed.', 220.00, 2),
        (105, 'Deluxe', 'Deluxe Mountain View', 'Stone floors, linen curtains, private terrace, king bed.', 220.00, 2),
        (106, 'Deluxe', 'Deluxe Mountain View', 'Stone floors, linen curtains, private terrace, king bed.', 220.00, 2),
        (107, 'Deluxe', 'Deluxe Mountain View', 'Stone floors, linen curtains, private terrace, king bed.', 220.00, 2),
        (108, 'Deluxe', 'Deluxe Mountain View', 'Stone floors, linen curtains, private terrace, king bed.', 220.00, 2),
        (109, 'Deluxe', 'Deluxe Mountain View', 'Stone floors, linen curtains, private terrace, king bed.', 220.00, 2),
        (110, 'Deluxe', 'Deluxe Mountain View', 'Stone floors, linen curtains, private terrace, king bed.', 220.00, 2),

        (201, 'Suite',  'Junior Suite',         'Living room with fireplace, canopied bed, private courtyard.', 380.00, 3),
        (202, 'Suite',  'Junior Suite',         'Living room with fireplace, canopied bed, private courtyard.', 380.00, 3),
        (203, 'Suite',  'Junior Suite',         'Living room with fireplace, canopied bed, private courtyard.', 380.00, 3),
        (204, 'Suite',  'Junior Suite',         'Living room with fireplace, canopied bed, private courtyard.', 380.00, 3),
        (205, 'Suite',  'Junior Suite',         'Living room with fireplace, canopied bed, private courtyard.', 380.00, 3),
        (206, 'Suite',  'Junior Suite',         'Living room with fireplace, canopied bed, private courtyard.', 380.00, 3),
        (207, 'Suite',  'Junior Suite',         'Living room with fireplace, canopied bed, private courtyard.', 380.00, 3),
        (208, 'Suite',  'Junior Suite',         'Living room with fireplace, canopied bed, private courtyard.', 380.00, 3),
        (209, 'Suite',  'Junior Suite',         'Living room with fireplace, canopied bed, private courtyard.', 380.00, 3),
        (210, 'Suite',  'Junior Suite',         'Living room with fireplace, canopied bed, private courtyard.', 380.00, 3),

        (301, 'Villa',  'Garden Villa',         'Private walled garden, small pool, two bedrooms.', 680.00, 4),
        (302, 'Villa',  'Garden Villa',         'Private walled garden, small pool, two bedrooms.', 680.00, 4),
        (303, 'Villa',  'Garden Villa',         'Private walled garden, small pool, two bedrooms.', 680.00, 4),
        (304, 'Villa',  'Garden Villa',         'Private walled garden, small pool, two bedrooms.', 680.00, 4),
        (305, 'Villa',  'Garden Villa',         'Private walled garden, small pool, two bedrooms.', 680.00, 4),
        (306, 'Villa',  'Garden Villa',         'Private walled garden, small pool, two bedrooms.', 680.00, 4),
        (307, 'Villa',  'Garden Villa',         'Private walled garden, small pool, two bedrooms.', 680.00, 4),
        (308, 'Villa',  'Garden Villa',         'Private walled garden, small pool, two bedrooms.', 680.00, 4),
        (309, 'Villa',  'Garden Villa',         'Private walled garden, small pool, two bedrooms.', 680.00, 4),
        (310, 'Villa',  'Garden Villa',         'Private walled garden, small pool, two bedrooms.', 680.00, 4);

-- Users table
CREATE TABLE IF NOT EXISTS users (
    id              INT AUTO_INCREMENT PRIMARY KEY,
    first_name      VARCHAR(100)  NOT NULL,
    last_name       VARCHAR(100)  NOT NULL,
    email           VARCHAR(150)  NOT NULL UNIQUE,
    password_hash   VARCHAR(255)  NOT NULL,
    date_of_birth   DATE,
    role            VARCHAR(20)   NOT NULL DEFAULT 'USER',
    created_at      TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP
    );

-- Bookings table
CREATE TABLE IF NOT EXISTS bookings (
    id              INT AUTO_INCREMENT PRIMARY KEY,
    room_id         INT           NOT NULL,
    user_id         INT           NOT NULL,
    check_in        DATE          NOT NULL,
    check_out       DATE          NOT NULL,
     status         VARCHAR(20)   NOT NULL DEFAULT 'CONFIRMED',
    created_at      TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_bookings_room
    FOREIGN KEY (room_id) REFERENCES rooms(id)
    ON DELETE CASCADE,

    CONSTRAINT fk_bookings_user
    FOREIGN KEY (user_id) REFERENCES users(id)
    ON DELETE CASCADE,

    CONSTRAINT chk_dates
    CHECK (check_out > check_in)
    );

CREATE INDEX idx_bookings_room_dates ON bookings (room_id, check_in, check_out);
CREATE INDEX idx_bookings_user ON bookings (user_id);


-- bar
CREATE TABLE bar_table_reservations (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    guests INT NOT NULL,
    reservation_date DATE NOT NULL,
    reservation_time TIME NOT NULL,
    notes VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id)
);


-- tours
CREATE TABLE IF NOT EXISTS tours (
     id          INT AUTO_INCREMENT PRIMARY KEY,
     name        VARCHAR(100) NOT NULL,
     description TEXT
);

INSERT INTO tours (name, description) VALUES
      ('Kazbegi & Gergeti Trinity Church', 'Full day trip to Kazbegi and the Gergeti Trinity Church, lunch included.'),
      ('Gomis Mta — Above the Clouds', 'Overnight camping trip to Gomis Mta with a guided hike and campfire dinner.'),
      ('Tusheti — Ancient Towers & Wild Mountains', '3 day trip to Tusheti over the Abano Pass, accommodation and meals included.'),
      ('Kass Land — Georgia''s Adventure Park', 'Full day at Kass Land adventure park, entry wristband included.'),
      ('Mtatsminda — Tbilisi from Above', 'Half day trip up the Mtatsminda funicular with city views and amusement park.'),
      ('Horseback Riding in the Georgian Highlands', 'Half day guided horseback trail through mountain meadows and forest.'),
      ('Free University of Tbilisi', 'Half day guided tour of the Free University campus, coffee included.');


CREATE TABLE IF NOT EXISTS tour_reservations (
     id               INT AUTO_INCREMENT PRIMARY KEY,
     user_id          INT NOT NULL,
     tour_id          INT NOT NULL,
     full_name        VARCHAR(100) NOT NULL,
     email            VARCHAR(150) NOT NULL,
     guests           INT NOT NULL,
     tour_date        DATE NOT NULL,
     special_requests VARCHAR(255),
     created_at       TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

     FOREIGN KEY (user_id) REFERENCES users(id),
     FOREIGN KEY (tour_id) REFERENCES tours(id)
);

CREATE INDEX idx_tour_reservations_tour_date ON tour_reservations (tour_id, tour_date);


