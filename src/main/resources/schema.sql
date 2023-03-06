CREATE TABLE IF NOT EXISTS notes (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    title VARCHAR NOT NULL,
    content VARCHAR NOT NULL,
    CONSTRAINT pk_notes PRIMARY KEY (id)
);