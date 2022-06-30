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
    CONSTRAINT request_id_fk FOREIGN KEY (request_id) REFERENCES users(id),
    CONSTRAINT response_id_fk FOREIGN KEY (response_id) REFERENCES users(id),
    CONSTRAINT status_fk FOREIGN KEY (status_id) REFERENCES friends_status(status_id)
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

CREATE TABLE IF NOT EXISTS films (
    id BIGINT NOT NULL,
    name VARCHAR(255) NOT NULL,
    description VARCHAR(200) NOT NULL,
    release_date DATE NOT NULL,
    duration BIGINT NOT NULL,
    mpa_id INTEGER NOT NULL,
    CONSTRAINT films_pk PRIMARY KEY (id),
    CONSTRAINT mpa_fk FOREIGN KEY (mpa_id) REFERENCES mpa(mpa_id)
);


CREATE TABLE IF NOT EXISTS film_genres (
    film_id  INT NOT NULL,
    genre_id INT NOT NULL,
    CONSTRAINT film_genres_pk PRIMARY KEY (film_id, genre_id),
    CONSTRAINT film_genres_fk FOREIGN KEY (film_id) REFERENCES films (id),
    FOREIGN KEY (genre_id) REFERENCES genres (genre_id)
);


CREATE TABLE IF NOT EXISTS likes (
    user_id BIGINT NOT NULL,
    film_id BIGINT NOT NULL,
    CONSTRAINT likes_pk PRIMARY KEY(user_id, film_id),
    CONSTRAINT likes_user_fk FOREIGN KEY(user_id) REFERENCES users(id),
    CONSTRAINT likes_film_fk FOREIGN KEY(film_id) REFERENCES films(id)
);


/*insert into films (id,name,description,release_date,duration,mpa_id) values (1,'TERMINATOR','test','1999-04-30',200,1);
insert into films (id,name,description,release_date,duration,mpa_id) values (2,'TERMINATOR','test','1999-04-30',200,1);
insert into films (id,name,description,release_date,duration,mpa_id) values (3,'TERMINATOR','test','1999-04-30',200,1);
insert into films (id,name,description,release_date,duration,mpa_id) values (4,'TERMINATOR','test','1999-04-30',200,1);
insert into films (id,name,description,release_date,duration,mpa_id) values (5,'TERMINATOR','test','1999-04-30',200,1);
insert into films (id,name,description,release_date,duration,mpa_id) values (6,'REMBO','test','1990-04-30',200,1);
insert into films (id,name,description,release_date,duration,mpa_id) values (7,'REMBO','test','1990-04-30',200,1);
insert into films (id,name,description,release_date,duration,mpa_id) values (8,'REMBO','test','1990-04-30',200,1);
insert into films (id,name,description,release_date,duration,mpa_id) values (9,'REMBO','test','1990-04-30',200,1);
insert into films (id,name,description,release_date,duration,mpa_id) values (10,'REMBO','test','1990-04-30',200,1);
insert into films (id,name,description,release_date,duration,mpa_id) values (11,'ALIENS','test','2021-04-30',200,1);
insert into films (id,name,description,release_date,duration,mpa_id) values (12,'ALIENS','test','2021-04-30',200,1);
insert into films (id,name,description,release_date,duration,mpa_id) values (13,'ALIENS','test','2021-04-30',200,1);
insert into films (id,name,description,release_date,duration,mpa_id) values (14,'ALIENS','test','2021-04-30',200,1);
insert into films (id,name,description,release_date,duration,mpa_id) values (15,'ALIENS','test','2021-04-30',200,1);
insert into films (id,name,description,release_date,duration,mpa_id) values (16,'PREDATOR','test','2011-04-30',200,1);
insert into films (id,name,description,release_date,duration,mpa_id) values (17,'PREDATOR','test','2011-04-30',200,1);
insert into films (id,name,description,release_date,duration,mpa_id) values (18,'PREDATOR','test','2011-04-30',200,1);
insert into films (id,name,description,release_date,duration,mpa_id) values (19,'PREDATOR','test','2011-04-30',200,1);
insert into films (id,name,description,release_date,duration,mpa_id) values (20,'PREDATOR','test','2011-04-30',200,1);*/


MERGE INTO genres (genre_id, genre_name)
    VALUES (1, 'Комедия'),
    (2, 'Драма'),
    (3, 'Мультфильм'),
    (4, 'Триллер'),
    (5, 'Документальный'),
    (6, 'Боевик');

MERGE INTO mpa (mpa_id, mpa_name)
    VALUES (1, 'G'),
    (2, 'PG'),
    (3, 'PG-13'),
    (4, 'R'),
    (5, 'NC-17');

MERGE INTO friends_status(status_id, status_name)
    VALUES (1, 'CONFIRMED');


/*SELECT
    f.id,
    f.name,
    f.description,
    f.release_date,
    f.duration,
    f.mpa_id,
    m.mpa_name,
    COUNT (l.user_id)
                FROM films AS f
                INNER JOIN mpa AS m ON f.mpa_id = m.mpa_id
                LEFT JOIN likes AS l ON f.id = l.film_id
                where extract(year from f.release_date) = '1999'
                GROUP BY f.id
                ORDER BY COUNT (l.user_id) DESC
                LIMIT 5;*/



/*
SELECT
    f.id,
    f.name,
    f.description,
    f.release_date,
    f.duration,
    f.mpa_id,
    m.mpa_name,
    COUNT (g.film_id),
    COUNT (l.user_id)
FROM films AS f
         INNER JOIN mpa AS m ON f.mpa_id = m.mpa_id
         LEFT JOIN likes AS l ON f.id = l.film_id
         right JOIN FILM_GENRES AS g ON f.id = g.FILM_ID
         where extract(year from f.release_date) = '2021' and g.genre_id = 4
GROUP BY f.id
ORDER BY COUNT (l.user_id) DESC
LIMIT 5;
*/

/*
DELETE FROM film_genres WHERE film_id=1;
MERGE INTO film_genres(film_id, genre_id) VALUES(1,1);

DELETE FROM film_genres WHERE film_id=2;
MERGE INTO film_genres(film_id, genre_id) VALUES(2,1);

DELETE FROM film_genres WHERE film_id=3;
MERGE INTO film_genres(film_id, genre_id) VALUES(3,1);

DELETE FROM film_genres WHERE film_id=4;
MERGE INTO film_genres(film_id, genre_id) VALUES(4,2);

DELETE FROM film_genres WHERE film_id=5;
MERGE INTO film_genres(film_id, genre_id) VALUES(5,2);

DELETE FROM film_genres WHERE film_id=6;
MERGE INTO film_genres(film_id, genre_id) VALUES(6,3);

DELETE FROM film_genres WHERE film_id=7;
MERGE INTO film_genres(film_id, genre_id) VALUES(7,3);

DELETE FROM film_genres WHERE film_id=8;
MERGE INTO film_genres(film_id, genre_id) VALUES(8,3);

DELETE FROM film_genres WHERE film_id=9;
MERGE INTO film_genres(film_id, genre_id) VALUES(9,1);

DELETE FROM film_genres WHERE film_id=10;
MERGE INTO film_genres(film_id, genre_id) VALUES(10,1);

DELETE FROM film_genres WHERE film_id=11;
MERGE INTO film_genres(film_id, genre_id) VALUES(11,1);

DELETE FROM film_genres WHERE film_id=12;
MERGE INTO film_genres(film_id, genre_id) VALUES(12,4);

DELETE FROM film_genres WHERE film_id=13;
MERGE INTO film_genres(film_id, genre_id) VALUES(13,4);

DELETE FROM film_genres WHERE film_id=14;
MERGE INTO film_genres(film_id, genre_id) VALUES(14,4);

DELETE FROM film_genres WHERE film_id=15;
MERGE INTO film_genres(film_id, genre_id) VALUES(15,3);

DELETE FROM film_genres WHERE film_id=16;
MERGE INTO film_genres(film_id, genre_id) VALUES(16,3);*/

