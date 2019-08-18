package fr.soat.eventsourcing.impl;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import fr.soat.eventsourcing.api.AggregateId;
import fr.soat.eventsourcing.api.Event;
import fr.soat.eventsourcing.api.EventConcurrentUpdateException;
import fr.soat.eventsourcing.api.EventStore;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static com.google.common.collect.Lists.reverse;

@Slf4j
public class FSEventStore implements EventStore {

    private File storeDirectory;
    private final Object lock = new Object();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public FSEventStore() {
        this("/tmp/store");
    }

    public FSEventStore(String storeDirectory) {
        this.storeDirectory = new File(storeDirectory);
        createIfNotExists(storeDirectory);
        log.info("FS eventStore in " + this.storeDirectory);
        setupObjectMapper();
    }

    private void setupObjectMapper() {
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT)
                .setVisibility(objectMapper.getSerializationConfig().getDefaultVisibilityChecker()
                        .withFieldVisibility(JsonAutoDetect.Visibility.ANY)
                        .withGetterVisibility(JsonAutoDetect.Visibility.NONE)
                        .withSetterVisibility(JsonAutoDetect.Visibility.NONE)
                        .withCreatorVisibility(JsonAutoDetect.Visibility.NONE));

    }

    private void createIfNotExists(String storeDirectory) {
        if (this.storeDirectory.exists() && !this.storeDirectory.isDirectory()) {
            this.storeDirectory = new File(storeDirectory + "_" + UUID.randomUUID().toString());
        }
        if (!this.storeDirectory.exists()) {
            this.storeDirectory.mkdirs();
        }
    }

    @Override
    public List<Event> loadEvents(AggregateId aggregateId) {
        synchronized (lock) {
            List<Event> loadedEvents = new ArrayList<>();
            File aggregateDir = new File(this.storeDirectory + "/" + aggregateId.getValue());
            if (aggregateDir.exists()) {
                File[] eventFiles = aggregateDir.listFiles();
                Arrays.sort(eventFiles);
                for (File eventFile : eventFiles) {
                    String eventType = eventFile.getName().split("\\-")[1].replaceFirst("\\.json", "");
                    try {
                        Event event = (Event) objectMapper.readValue(eventFile, Class.forName(eventType));
                        loadedEvents.add(event);
                    } catch (IOException | ClassNotFoundException e) {
                        throw new RuntimeException("failed to parse event from " + eventFile, e);
                    }
                }
            }
            return loadedEvents;
        }
    }

    @Override
    public void store(AggregateId aggregateId, List<Event> events) {
        synchronized (lock) {
            List<Event> previousEvents = loadEvents(aggregateId);
            checkVersion(previousEvents, events);

            for (int i = previousEvents.size(); i < events.size(); i++) {
                // add
                Event event = events.get(i);
                String aggregateDir = this.storeDirectory.getAbsoluteFile()
                        + "/" + aggregateId.getValue() +
                        "/";
                if (!new File(aggregateDir).exists()) {
                    new File(aggregateDir).mkdir();
                }

                File eventFile = new File(aggregateDir + i + "-" + event.getClass().getName() + ".json");
                try {
                    objectMapper.writeValue(eventFile, event);
                } catch (IOException e) {
                    throw new RuntimeException("failed to save event file " + eventFile.getName(), e);
                }
            }
        }
    }

    private void checkVersion(List<Event> previousEvents, List<Event> events) {
        if (Collections.indexOfSubList(events, previousEvents) != 0) {
            String msg = "Failed to save events, version mismatch (there was a concurrent update)\n" +
                    "- In store: " + reverse(previousEvents) + "\n" +
                    "- Trying to save: " + reverse(events);
            throw new EventConcurrentUpdateException(msg);
        }
    }

    @Override
    public void clear() {
        try {
            FileUtils.deleteDirectory(this.storeDirectory);
        } catch (IOException e) {
            throw new RuntimeException("failed to clean store directory", e);
        }
        this.storeDirectory.mkdirs();

        log.warn("evenstore " + storeDirectory
                + " has been cleared");
    }

    @Override
    public String nextId() {
        synchronized (lock) {
            String[] ids = storeDirectory.list();
            Arrays.sort(ids);
            return String.valueOf((ids.length == 0) ? 1 : Integer.parseInt(ids[ids.length - 1]) + 1);
        }
    }
}
