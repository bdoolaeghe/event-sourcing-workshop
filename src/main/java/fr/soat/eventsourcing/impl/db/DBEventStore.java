package fr.soat.eventsourcing.impl.db;

import com.google.common.annotations.VisibleForTesting;
import fr.soat.eventsourcing.api.EntityId;
import fr.soat.eventsourcing.api.Event;
import fr.soat.eventsourcing.api.EventStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;

@Repository
public class DBEventStore<ENTITY_ID extends EntityId, EVENT_TYPE extends Event<?>> implements EventStore<ENTITY_ID, EVENT_TYPE> {

    private final JdbcTemplate jdbcTemplate;
    private final ApplicationEventPublisher eventPublisher;

    private static final String SELECT =
            " SELECT ety.entity_id, "
            + "evt.event_id,"
            + "evt.event_sequence_id,"
            + "evt.event_type,"
            + "evt.timestamp,"
            + "evt.content" +
            " FROM entity ety LEFT OUTER JOIN event evt ON ety.entity_id = evt.entity_id " +
            " WHERE ety.entity_id = ? " +
            " ORDER BY evt.event_sequence_id";

    private static final String INSERT_EVENT =
            "INSERT INTO event ("
            + "entity_id, "
            + "event_sequence_id, "
            + "event_type, "
            + "content) VALUES (?,?,?,?)";

    private static final String INSERT_ENTITY_IF_NOT_EXIST =
            "INSERT INTO entity "
            + " (entity_id) VALUES (?)"
            + " ON CONFLICT DO NOTHING";

    private static final String TRUNCATE_ENTITY = "TRUNCATE entity";
    private static final String TRUNCATE_EVENT = "TRUNCATE event";

    @Autowired
    public DBEventStore(DataSource dataSource, ApplicationEventPublisher eventPublisher) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.eventPublisher = eventPublisher;
    }

    @Override
    public List<EVENT_TYPE> loadEvents(ENTITY_ID entityId) {
        List<EVENT_TYPE> events = jdbcTemplate.query(
                SELECT,
                new Object[]{entityId.getIdValue()},
                new EventMapper<>());
        if (events.isEmpty()) {
            // entity not found
            throw new IllegalArgumentException("entity (id='" + entityId + "') not found.");
        } else if (events.equals(singletonList(null))) {
            // entity exists, but with no event
            return emptyList();
        } else {
            return events;
        }
    }

    @Override
    public void store(ENTITY_ID entityId, List<EVENT_TYPE> events, int version) {
        jdbcTemplate.update(INSERT_ENTITY_IF_NOT_EXIST, entityId.getIdValue());
        if (!events.isEmpty()) {
            List<Object[]> batchArgs = createInsertBatchArgs(entityId, events, version);
            jdbcTemplate.batchUpdate(INSERT_EVENT, batchArgs);
            events.forEach(eventPublisher::publishEvent);
        }
    }

    @Override
    public int newEntityId() {
        return jdbcTemplate.queryForObject("SELECT nextval('entity_id_seq')", Integer.class);
    }

    private List<Object[]> createInsertBatchArgs(ENTITY_ID entityId, List<EVENT_TYPE> events, int version) {
        List<Object[]> batchArgs = new ArrayList<>();
        for (int i = 0; i < events.size(); i++) {
            EVENT_TYPE event = events.get(i);
            batchArgs.add(new Object[] {
                    entityId.getIdValue(),
                    version + i,
                    event.getClass().getName(),
                    EventMapper.toJson(events.get(i))
            });
        }
        return batchArgs;
    }

    @VisibleForTesting
    public void clear() {
        jdbcTemplate.execute(TRUNCATE_ENTITY);
        jdbcTemplate.execute(TRUNCATE_EVENT);
    }

}
