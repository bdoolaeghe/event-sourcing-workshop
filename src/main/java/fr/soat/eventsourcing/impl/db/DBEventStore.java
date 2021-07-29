package fr.soat.eventsourcing.impl.db;

import com.google.common.annotations.VisibleForTesting;
import fr.soat.eventsourcing.api.EntityId;
import fr.soat.eventsourcing.api.Event;
import fr.soat.eventsourcing.api.EventStore;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

@Repository
public class DBEventStore<ENTITY_ID extends EntityId, EVENT_TYPE extends Event> implements EventStore<ENTITY_ID, EVENT_TYPE> {

    private final JdbcTemplate jdbcTemplate;

    private static final String SELECT =
            " SELECT event_id, "
            + "entity_id,"
            + "event_sequence_id,"
            + "event_type,"
            + "timestamp,"
            + "content" +
            " FROM event " +
            " WHERE entity_id = ? " +
            " ORDER BY event_sequence_id";

    private static final String INSERT =
            "INSERT INTO event ("
            + "entity_id, "
            + "event_sequence_id, "
            + "event_type, "
            + "content) VALUES (?,?,?,?)";

    private final String TRUNCATE = "TRUNCATE event";

    public DBEventStore(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public List<EVENT_TYPE> loadEvents(ENTITY_ID entityId) {
        return jdbcTemplate.query(
                SELECT,
                new Object[]{entityId.getValue()},
                new EventMapper<EVENT_TYPE>());
    }

    @Override
    public void store(ENTITY_ID entityId, List<EVENT_TYPE> events) {
        List<Object[]> batchArgs = createInsertBatchArgs(entityId, events);
        jdbcTemplate.batchUpdate(INSERT, batchArgs);
    }

    @Override
    public int newEntityId() {
        return jdbcTemplate.queryForObject("SELECT nextval('entity_id_seq')", Integer.class);
    }

    private List<Object[]> createInsertBatchArgs(ENTITY_ID entityId, List<EVENT_TYPE> events) {
        List<Object[]> batchArgs = new ArrayList<>();
        for (int i = 0; i < events.size(); i++) {
            EVENT_TYPE event = events.get(i);
            batchArgs.add(new Object[] {
                    entityId.getValue(),
                    i,
                    event.getClass().getName(),
                    EventMapper.toJson(events.get(i))
            });
        }
        return batchArgs;
    }

    @VisibleForTesting
    public void clear() {
        jdbcTemplate.execute(TRUNCATE);
    }

}
