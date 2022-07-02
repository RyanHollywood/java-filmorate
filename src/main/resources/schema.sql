--DROP TABLE users;
--DROP TABLE friends_status;
--DROP TABLE friends;
--DROP TABLE mpa ;
--DROP TABLE genres ;
--DROP TABLE films ;
--DROP TABLE film_genres;
--DROP TABLE likes ;

--DROP TABLE users, friends_status,friends,mpa,genres,films,film_genres,likes;





CREATE TABLE IF NOT EXISTS users (
    id BIGINT NOT NULL,
    email VARCHAR(255) NOT NULL,
    login VARCHAR(50) NOT NULL,
    name VARCHAR(50) NOT NULL,
    birthday DATE NOT NULL,
    CONSTRAINT users_pk PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS friends_status (
    status_id INTEGER NOT NULL,
    status_name VARCHAR(255) NOT NULL,
    CONSTRAINT friends_status_pk PRIMARY KEY (status_id)
);

CREATE TABLE IF NOT EXISTS friends (
    request_id BIGINT NOT NULL,
    response_id BIGINT NOT NULL,
    status_id INTEGER NOT NULL,
    CONSTRAINT friends_pk PRIMARY KEY (request_id, response_id),
    CONSTRAINT request_id_fk FOREIGN KEY (request_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT response_id_fk FOREIGN KEY (response_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT status_fk FOREIGN KEY (status_id) REFERENCES friends_status(status_id) ON DELETE CASCADE
);


CREATE TABLE IF NOT EXISTS mpa (
    mpa_id INTEGER NOT NULL,
    mpa_name VARCHAR(255) NOT NULL,
    CONSTRAINT mpa_pk PRIMARY KEY (mpa_id)
);

CREATE TABLE IF NOT EXISTS genres (
    genre_id INTEGER NOT NULL,
    genre_name VARCHAR(255),
    CONSTRAINT genres_pk PRIMARY KEY (genre_id)
);

CREATE TABLE IF NOT EXISTS directors (
    director_id INTEGER NOT NULL,
    director_name VARCHAR(255),
    CONSTRAINT directors_pk PRIMARY KEY (director_id)
);

CREATE TABLE IF NOT EXISTS films (
    id BIGINT NOT NULL,
    name VARCHAR(255) NOT NULL,
    description VARCHAR(200) NOT NULL,
    release_date DATE NOT NULL,
    duration BIGINT NOT NULL,
    mpa_id INTEGER NOT NULL,
    CONSTRAINT films_pk PRIMARY KEY (id),
    CONSTRAINT mpa_fk FOREIGN KEY (mpa_id) REFERENCES mpa(mpa_id) ON DELETE CASCADE
);


CREATE TABLE IF NOT EXISTS film_genres (
    film_id  INT NOT NULL,
    genre_id INT NOT NULL,
    CONSTRAINT film_genres_pk PRIMARY KEY (film_id, genre_id),
    CONSTRAINT film_genres_fk FOREIGN KEY (film_id) REFERENCES films (id) ON DELETE CASCADE,
    FOREIGN KEY (genre_id) REFERENCES genres (genre_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS film_directors (
    film_id  INT NOT NULL,
    director_id INT NOT NULL,
    CONSTRAINT film_directors_pk PRIMARY KEY (film_id, director_id),
    CONSTRAINT film_directors_fk FOREIGN KEY (film_id) REFERENCES films (id) ON DELETE CASCADE,
    FOREIGN KEY (director_id) REFERENCES directors (director_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS likes (
    user_id BIGINT NOT NULL,
    film_id BIGINT NOT NULL,
    CONSTRAINT likes_pk PRIMARY KEY(user_id, film_id),
    CONSTRAINT likes_user_fk FOREIGN KEY(user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT likes_film_fk FOREIGN KEY(film_id) REFERENCES films(id) ON DELETE CASCADE
);
