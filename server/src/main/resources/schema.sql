CREATE TABLE IF NOT EXISTS users
(
    user_id   BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    user_name VARCHAR(255)                            NOT NULL,
    email     VARCHAR(255)                            NOT NULL,
    CONSTRAINT pk_users PRIMARY KEY (user_id),
    CONSTRAINT uq_users_email UNIQUE (email)
    );

CREATE TABLE IF NOT EXISTS requests
(
    request_id      BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    req_description VARCHAR(512)                            NOT NULL,
    requester_id    BIGINT                                  NOT NULL,
    created         TIMESTAMP WITHOUT TIME ZONE             NOT NULL,
    CONSTRAINT pk_requests PRIMARY KEY (request_id),
    CONSTRAINT fk_requester_id_to_users FOREIGN KEY (requester_id) REFERENCES users (user_id) ON DELETE CASCADE
    );

CREATE TABLE IF NOT EXISTS items
(
    item_id          BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    item_name        VARCHAR(255)                            NOT NULL,
    item_description VARCHAR(255)                            NOT NULL,
    available        BOOLEAN                                 NOT NULL,
    request_id       BIGINT,
    owner_id         BIGINT                                  NOT NULL,
    CONSTRAINT pk_items PRIMARY KEY (item_id),
    CONSTRAINT fk_items_owner_id_to_users FOREIGN KEY (owner_id) REFERENCES users (user_id) ON DELETE CASCADE,
    CONSTRAINT fk_items_request_id_to_requests FOREIGN KEY (request_id) REFERENCES requests (request_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS bookings
(
    booking_id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    booker_id  BIGINT                                  NOT NULL,
    item_id    BIGINT                                  NOT NULL,
    start_time TIMESTAMP WITHOUT TIME ZONE             NOT NULL,
    end_time   TIMESTAMP WITHOUT TIME ZONE             NOT NULL,
    status     VARCHAR(50)                             NOT NULL,
    CONSTRAINT pk_bookings PRIMARY KEY (booking_id),
    CONSTRAINT fk_bookings_booker_id_to_users FOREIGN KEY (booker_id) REFERENCES users(user_id) ON DELETE CASCADE,
    CONSTRAINT fk_bookings_item_id_to_items FOREIGN KEY (item_id) REFERENCES items(item_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS comments
(
    comment_id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    text       VARCHAR(1000)                           NOT NULL,
    item_id    BIGINT                                  NOT NULL,
    author_id  BIGINT                                  NOT NULL,
    created_time TIMESTAMP WITHOUT TIME ZONE           NOT NULL,
    CONSTRAINT pk_comments PRIMARY KEY (comment_id),
    CONSTRAINT fk_comments_item_id_to_items FOREIGN KEY (item_id) REFERENCES items (item_id) ON DELETE CASCADE,
    CONSTRAINT fk_comments_author_id_to_users FOREIGN KEY (author_id) REFERENCES users (user_id) ON DELETE CASCADE
);

