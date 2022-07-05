package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.model.dao.EventDaoImpl;

@Slf4j
@Service
public class EventService {

    private EventDaoImpl eventDao;

    @Autowired
    public EventService (EventDaoImpl eventDao) {
        this.eventDao = eventDao;
    }

    public void addEvent(Event event) {
        eventDao.add(event);
        log.debug("EVENT ADDED SUCCESSFUL - EVENT WITH ID:" + event.getEventId() + " ADDED");
    }
}
