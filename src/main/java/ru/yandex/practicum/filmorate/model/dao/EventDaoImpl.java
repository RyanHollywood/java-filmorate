package ru.yandex.practicum.filmorate.model.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Event;

@Component
public class EventDaoImpl implements EventDao {

    private final JdbcTemplate jdbcTemplate;

    public EventDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void add(Event event) {
        jdbcTemplate.update("INSERT INTO events(user_id,entity_id, event_type, operation, timestamp) " +
                "VALUES (?, ?, ?, ?, ?)", event.getUserId(), event.getEntityId(), event.getEventType(), event.getOperation(), System.currentTimeMillis());
    }
}
