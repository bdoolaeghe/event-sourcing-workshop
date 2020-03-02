package fr.soat.eventsourcing.impl.db;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.soat.eventsourcing.api.Event;
import org.springframework.jdbc.core.RowMapper;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EventMapper implements RowMapper<Event> {

    final private ObjectMapper objectMapper = configureObjectMapper();

    private ObjectMapper configureObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        // map by field, not by property
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.NONE);
        objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        return objectMapper;
    }

    @Override
    public Event mapRow(ResultSet rs, int i) throws SQLException {
        String eventType = rs.getString("event_type");
        String jsonContent = rs.getString("content");
        Class<? extends Event> eventTypeClass = getEventTypeClass(eventType);
        try {
            return objectMapper.readValue(jsonContent, eventTypeClass);
        } catch (IOException e) {
            throw new RuntimeException("Failed to deserialize event: " + jsonContent, e);
        }
    }

    private Class<? extends Event> getEventTypeClass(String eventType) {
        try {
            return (Class<? extends Event>) Class.forName(eventType);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Unknown event type: " + eventType, e);
        }
    }
}
