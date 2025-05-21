BEGIN;

CREATE TYPE country AS ENUM (
   'UNITED_KINGDOM',
    'INDIA',
    'VATICAN',
    'ITALY',
    'NORTH_KOREA'
);

CREATE TYPE eye_color AS ENUM (
    'GREEN',
    'BLUE',
    'ORANGE',
    'WHITE',
    'BROWN'
);

CREATE TYPE hair_color AS ENUM (
    'GREEN',
    'RED',
    'BLACK',
    'WHITE',
    'BROWN'
);

CREATE TYPE worker_position AS ENUM (
    'LABORER',
    'DEVELOPER',
    'LEAD_DEVELOPER',
    'COOK',
    'MANAGER_OF_CLEANING'
);

CREATE TYPE worker_status AS  ENUM (
    'HIRED',
    'RECOMMENDED_FOR_PROMOTION',
    'REGULAR'
);

END;