package fr.soat.eventsourcing.impl.db;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import fr.soat.eventsourcing.api.Event;
import org.springframework.jdbc.core.RowMapper;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EventMapper<EVENT_TYPE extends Event<?>> implements RowMapper<EVENT_TYPE> {

    private static final ObjectMapper objectMapper = configureObjectMapper();

    private static ObjectMapper configureObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        // map by field, not by property
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.NONE);
        objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        objectMapper.writerWithDefaultPrettyPrinter();
        objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        return objectMapper;
    }

    @Override
    public EVENT_TYPE mapRow(ResultSet rs, int i) throws SQLException {
        String jsonContent = rs.getString("content");
        if (jsonContent == null) {
            //TODO better implem than null content when no events
            return null;
        } else {
            String eventType = rs.getString("event_type");
            Class<? extends EVENT_TYPE> eventTypeClass = getEventTypeClass(eventType);
            return fromJson(jsonContent, eventTypeClass);
        }
    }

    public static <EVENT_TYPE extends Event<?>> String toJson(EVENT_TYPE event) {
        try {
            return objectMapper.writeValueAsString(event);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize event: " + event, e);
        }
    }

    public static <EVENT_TYPE extends Event<?>> EVENT_TYPE fromJson(String jsonContent, Class<? extends EVENT_TYPE> eventTypeClass) {
        try {
            return objectMapper.readValue(jsonContent, eventTypeClass);
        } catch (IOException e) {
            throw new RuntimeException("Failed to deserialize event: " + jsonContent, e);
        }
    }

    private Class<? extends EVENT_TYPE> getEventTypeClass(String eventType) {
        try {
            return (Class<? extends EVENT_TYPE>) Class.forName(eventType);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Unknown event type: " + eventType, e);
        }
    }
}
