package ru.yandex.practicum.filmorate.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EventTest {

    private Event event;

    @BeforeEach
    void reload() {
        event = new Event(1, 2, 3, "Type", "Operation", 0);
    }

    @Test
    void compareTo() {
        Event otherEvent = new Event(2, 2, 3, "Other type", "Other operation", 0);
        assertTrue(event.compareTo(otherEvent) < 0);
    }

    @Test
    void getEventId() {
        assertEquals(1, event.getEventId());
    }

    @Test
    void getUserId() {
        assertEquals(2, event.getUserId());
    }

    @Test
    void getEntityId() {
        assertEquals(3, event.getEntityId());
    }

    @Test
    void getEventType() {
        assertEquals("Type", event.getEventType());
    }

    @Test
    void getOperation() {
        assertEquals("Operation", event.getOperation());
    }

    @Test
    void getTimestamp() {
        assertEquals(0, event.getTimestamp());
    }

    @Test
    void setEventId() {
        event.setEventId(2);
        assertEquals(2, event.getEventId());
    }

    @Test
    void setUserId() {
        event.setUserId(3);
        assertEquals(3, event.getUserId());
    }

    @Test
    void setEntityId() {
        event.setEntityId(4);
        assertEquals(4, event.getEntityId());
    }

    @Test
    void setEventType() {
        event.setEventType("Other type");
        assertEquals("Other type", event.getEventType());
    }

    @Test
    void setOperation() {
        event.setOperation("Other operation");
        assertEquals("Other operation", event.getOperation());
    }

    @Test
    void setTimestamp() {
        event.setTimestamp(1);
        assertEquals(1, event.getTimestamp());
    }

    @Test
    void testEquals() {
        Event sameEvent = new Event(1, 2, 3, "Type", "Operation", 0);
        assertTrue(event.equals(sameEvent));

        event.setEventId(2);
        assertFalse(event.equals(sameEvent));
    }

    @Test
    void canEqual() {
        Event otherEvent = new Event(2, 2, 3, "Other type", "Other operation", 0);
        assertTrue(event.canEqual(otherEvent));
    }

    @Test
    void testHashCode() {
        Event sameEvent = new Event(1, 2, 3, "Type", "Operation", 0);
        assertEquals(event.hashCode(), sameEvent.hashCode());
    }

    @Test
    void testToString() {
        assertEquals("Event(eventId=1, userId=2, entityId=3, eventType=Type, operation=Operation, timestamp=0)", event.toString());
    }
}