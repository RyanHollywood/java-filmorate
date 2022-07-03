package ru.yandex.practicum.filmorate.storage.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.sql.Date;
import java.time.Duration;
import java.time.LocalDate;
import java.util.*;


@Component("filmDbStorage")
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;

    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Film get(long id) {
        SqlRowSet response = jdbcTemplate.queryForRowSet("SELECT * FROM films AS f INNER JOIN mpa AS m ON f.mpa_id = m.mpa_id WHERE id=?;", id);
        response.next();
        Film film = new Film(response.getLong("id"), response.getString("name"), response.getString("description"),
                LocalDate.parse(response.getString("release_date")), Duration.ofSeconds(response.getLong("duration")),
                new Mpa(response.getInt("mpa_id"), response.getString("mpa_name")));
        return getDirectors(getGenres(film));
    }

    @Override
    public Collection<Film> getAll() {
        SqlRowSet response = jdbcTemplate.queryForRowSet("SELECT * FROM films AS f INNER JOIN mpa AS m ON f.mpa_id = m.mpa_id;");
        Collection<Film> allFilms = new TreeSet<>();
        while (response.next()) {
            Film film = new Film(response.getLong("id"), response.getString("name"), response.getString("description"),
                    LocalDate.parse(response.getString("release_date")), Duration.ofSeconds(response.getLong("duration")),
                    new Mpa(response.getInt("mpa_id"), response.getString("mpa_name")));
            allFilms.add(getDirectors(getGenres(film)));
        }
        return allFilms;
    }

    @Override
    public Collection<Film> getPopular(int quantity, Integer year, Integer genreId) {
        if (quantity == 10 && year != 1894 || genreId != 0) {
            return getPopularByGenresAndYear(year, genreId);
        }
        Collection<Film> popular = new HashSet<>();
        SqlRowSet response = jdbcTemplate.queryForRowSet("SELECT f.id, f.name, f.description, f.release_date, f.duration, f.mpa_id, m.mpa_name, COUNT (l.user_id) " +
                "FROM films AS f INNER JOIN mpa AS m ON f.mpa_id = m.mpa_id LEFT JOIN likes AS l ON f.id = l.film_id GROUP BY f.id ORDER BY COUNT (l.user_id) DESC LIMIT ?;", quantity);
        while (response.next()) {
            Film film = new Film(response.getLong("id"), response.getString("name"), response.getString("description"),
                    LocalDate.parse(response.getString("release_date")), Duration.ofSeconds(response.getLong("duration")),
                    new Mpa(response.getInt("mpa_id"), response.getString("mpa_name")));
            popular.add(getDirectors(getGenres(film)));
        }
        return popular;
    }

    public Collection<Film> getPopularByGenresAndYear(Integer year, Integer genreId) {
        Collection<Film> popular = new HashSet<>();
        Integer yearNow = year;
        int genreIdMax = genreId;
        if (year == 1894) {
            yearNow = LocalDate.now().getYear();
        } else if (genreId == 0) {
            genreIdMax = 6;
        }
        SqlRowSet response = jdbcTemplate.queryForRowSet("" +
                        "SELECT " +
                        "f.id, " +
                        "f.name, " +
                        "f.description, " +
                        "f.release_date, " +
                        "f.duration, " +
                        "f.mpa_id, " +
                        "m.mpa_name, " +
                        "COUNT (g.film_id), " +
                        "COUNT (l.user_id) " +
                        "FROM films AS f " +
                        "INNER JOIN mpa AS m ON f.mpa_id = m.mpa_id " +
                        "LEFT JOIN likes AS l ON f.id = l.film_id " +
                        "right JOIN FILM_GENRES AS g ON f.id = g.FILM_ID " +
                        "where extract(year from f.release_date)  between ? and ? and g.genre_id between ? and ? " +
                        "GROUP BY f.id " +
                        "ORDER BY COUNT (l.user_id) DESC;"
                , year, yearNow, genreId, genreIdMax);
        while (response.next()) {
            Film film = get(response.getLong("id"));
            popular.add(film);
        }
        return popular;
    }

    public Collection<Film> getByDirectorByLikes(int directorId) {
        Collection<Film> sortedBylike = new ArrayList<>();
        SqlRowSet response = jdbcTemplate.queryForRowSet("SELECT f.id, f.name, f.description, f.release_date, f.duration, f.mpa_id, m.mpa_name, fd.director_id, COUNT (l.user_id) " +
                "FROM films AS f INNER JOIN mpa AS m ON f.mpa_id = m.mpa_id RIGHT JOIN film_directors AS fd ON f.id = fd.film_id " +
                "LEFT JOIN likes AS l ON f.id = l.film_id WHERE fd.director_id = ? GROUP BY f.id ORDER BY COUNT (l.user_id) DESC;", directorId);
        while (response.next()) {
            Film film = new Film(response.getLong("id"), response.getString("name"), response.getString("description"),
                    LocalDate.parse(response.getString("release_date")), Duration.ofSeconds(response.getLong("duration")),
                    new Mpa(response.getInt("mpa_id"), response.getString("mpa_name")));
            sortedBylike.add(getDirectors(getGenres(film)));
        }
        return sortedBylike;
    }

    public Collection<Film> getByDirectorByYear(int directorId) {
        Collection<Film> sortedByYear = new ArrayList<>();
        SqlRowSet response = jdbcTemplate.queryForRowSet("SELECT f.id, f.name, f.description, f.release_date, f.duration, f.mpa_id, m.mpa_name, fd.director_id, COUNT (l.user_id) " +
                "FROM films AS f INNER JOIN mpa AS m ON f.mpa_id = m.mpa_id RIGHT JOIN film_directors AS fd ON f.id = fd.film_id " +
                "LEFT JOIN likes AS l ON f.id = l.film_id WHERE fd.director_id = ? GROUP BY f.id ORDER BY f.release_date ASC;", directorId);
        while (response.next()) {
            Film film = new Film(response.getLong("id"), response.getString("name"), response.getString("description"),
                    LocalDate.parse(response.getString("release_date")), Duration.ofSeconds(response.getLong("duration")),
                    new Mpa(response.getInt("mpa_id"), response.getString("mpa_name")));
            sortedByYear.add(getDirectors(getGenres(film)));
        }
        return sortedByYear;
    }

    @Override
    public void add(Film film) {
        addFilm(film);
        addGenres(film);
        addDirectors(film);
    }

    @Override
    public void update(Film film) {
        addFilm(film);
        addGenres(film);
        addDirectors(film);
    }

    @Override
    public void delete(long id) {
        jdbcTemplate.update("DELETE FROM films WHERE id=?", id);
    }

    @Override
    public void deleteAll() {
        jdbcTemplate.execute("DELETE FROM films");
    }

    @Override
    public boolean contains(long id) {
        SqlRowSet response = jdbcTemplate.queryForRowSet("SELECT * FROM films WHERE id=?", id);
        return response.next();
    }

    @Override
    public long getNewId() {
        SqlRowSet response = jdbcTemplate.queryForRowSet("SELECT * FROM films ORDER BY id DESC LIMIT 1;");
        if (response.next()) {
            return response.getLong("id") + 1;
        }
        return 1;
    }

    @Override
    public void resetId() {
        //do nothing
    }

    @Override
    public void addLike(long filmId, long userId) {
        jdbcTemplate.update("MERGE INTO likes(user_id, film_id) VALUES (?, ?);", userId, filmId);
    }

    @Override
    public void deleteLike(long filmId, long userId) {
        jdbcTemplate.update("DELETE FROM likes WHERE user_id=? AND film_id =?", userId, filmId);
    }

    @Override
    public boolean containsLike(long filmId, long userId) {
        SqlRowSet response = jdbcTemplate.queryForRowSet("SELECT * FROM likes WHERE user_id=? AND film_id = ?", userId, filmId);
        return response.next();
    }

    public Collection<Film> searchFilm(String query, String by)  {
        Collection<Film> search = new HashSet<>();
        String director = '%' + query + '%';
        String title = '%' + query + '%';
        if (by.equals("director")) {
            return searchFilmDirector(director);
        } else if (by.equals("title")) {
            return searchFilmTitle(title);
        }
        SqlRowSet response = jdbcTemplate.queryForRowSet("" +
                "SELECT " +
                "f2.id, " +
                "f2.name, " +
                "f2.description, " +
                "f2.release_date, " +
                "f2.duration, " +
                "f2.mpa_id, " +
                "m.mpa_name, " +
                "d.film_id, " +
                "d2.director_name " +
                "FROM films AS f " +
                "INNER JOIN mpa AS m ON f.mpa_id = m.mpa_id " +
                "RIGHT JOIN film_directors AS d ON f.id = d.film_id " +
                "RIGHT JOIN films f2 on f2.id = d.film_id " +
                "LEFT JOIN directors d2 on d2.director_id = d.director_id " +
                "WHERE f2.name ILIKE ? or d2.director_name ILIKE ? " +
                "GROUP BY f2.id; ", title, director);
        while (response.next()) {
            search.add(get(response.getInt("id")));
        }
        return search;
    }

    private Collection<Film> searchFilmTitle(String query) {
        Collection<Film> search = new HashSet<>();
        SqlRowSet response = jdbcTemplate.queryForRowSet("" +
                "SELECT * " +
                "FROM FILMS " +
                "WHERE name ILIKE ? ", query);
        while (response.next()) {
            search.add(get(response.getInt("id")));
        }
        return search;
    }

    private Collection<Film> searchFilmDirector(String query) {
        Collection<Film> search = new HashSet<>();
        SqlRowSet response = jdbcTemplate.queryForRowSet("" +
                "SELECT " +
                "f.id, " +
                "f.name, " +
                "f.description, " +
                "f.release_date, " +
                "f.duration, " +
                "f.mpa_id, " +
                "m.mpa_name, " +
                "d.film_id, " +
                "d2.director_name " +
                "FROM films AS f " +
                "INNER JOIN mpa AS m ON f.mpa_id = m.mpa_id " +
                "RIGHT JOIN film_directors AS d ON f.id = d.film_id " +
                "LEFT JOIN directors d2 on d2.director_id = d.director_id " +
                "WHERE d2.director_name ILIKE ? " +
                "GROUP BY f.id ;", query);
        while (response.next()) {
            search.add(get(response.getInt("id")));
        }
        return search;
    }

    private void addGenres(Film film) {
        jdbcTemplate.update("DELETE FROM film_genres WHERE film_id = ?;", film.getId());
        if (!Optional.ofNullable(film.getGenres()).isEmpty()) {
            for (Genre genre : film.getGenres()) {
                jdbcTemplate.update("MERGE INTO film_genres(film_id, genre_id) VALUES(?,?)", film.getId(), genre.getId());
            }
        }
    }

    private Film getGenres(Film film) {
        SqlRowSet response = jdbcTemplate.queryForRowSet("SELECT * FROM film_genres AS fg " +
                "INNER JOIN genres AS g ON fg.genre_id = g.genre_id " +
                "WHERE film_id = ?;", film.getId());

        TreeSet<Genre> genres = new TreeSet<>();
        while (response.next()) {
            genres.add(new Genre(response.getInt("genre_id"), response.getString("genre_name")));
        }
        if (!genres.isEmpty()) {
            film.setGenres(genres);
        }
        return film;
    }


    private void addDirectors(Film film) {
        jdbcTemplate.update("DELETE FROM film_directors WHERE film_id = ?", film.getId());
        if (Optional.ofNullable(film.getDirectors()).isPresent()) {
            for (Director director : film.getDirectors()) {
                jdbcTemplate.update("INSERT INTO film_directors(film_id, director_id) VALUES (?, ?)", film.getId(), director.getId());
            }
        }
    }

    private Film getDirectors(Film film) {
        SqlRowSet response = jdbcTemplate.queryForRowSet("SELECT * FROM film_directors AS fd " +
                "INNER JOIN directors AS d ON fd.director_id = d.director_id " +
                "WHERE film_id = ?;", film.getId());
        TreeSet<Director> directors = new TreeSet<>();
        while (response.next()) {
            directors.add(new Director(response.getInt("director_id"), response.getString("director_name")));
        }
        //if (!directors.isEmpty()) {
        film.setDirectors(directors);
        //}
        return film;
    }

    private void addFilm(Film film) {
        jdbcTemplate.update("MERGE INTO films(id, name, description, release_date, duration, mpa_id) VALUES(?, ?, ?, ?, ?, ?)",
                film.getId(), film.getName(), film.getDescription(), Date.valueOf(film.getReleaseDate()), film.getDuration().getSeconds(), film.getMpa().getId());
    }

    public Collection<Film> getCommon(long userId, long friendId) {
        Collection<Film> commonFilms = new HashSet<>();
        SqlRowSet response = jdbcTemplate.queryForRowSet("SELECT * FROM films INNER JOIN mpa ON films.mpa_id = mpa.mpa_id " +
                "WHERE id IN (SELECT film_id FROM likes WHERE film_id IN " +
                "(SELECT id FROM films WHERE id IN (SELECT ul.film_id FROM " +
                "(SELECT * FROM likes WHERE user_id = ?) as ul " +
                "INNER JOIN (SELECT * FROM likes WHERE user_id = ?) as ul1 on ul.film_id = ul1.film_id)) " +
                "GROUP BY id ORDER BY COUNT(id) DESC);", userId, friendId);
        while (response.next()) {
            Film film = new Film(response.getLong("id"), response.getString("name"), response.getString("description"),
                    LocalDate.parse(Objects.requireNonNull(response.getString("release_date"))), Duration.ofSeconds(response.getLong(
                    "duration")),
                    new Mpa(response.getInt("mpa_id"), response.getString("mpa_name")));
            commonFilms.add(film);
        }
        return commonFilms;
    }
}

