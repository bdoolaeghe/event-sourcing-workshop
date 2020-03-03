package fr.soat.eventsourcing.impl.db;

import com.google.common.annotations.VisibleForTesting;
import fr.soat.eventsourcing.api.AggregateId;
import fr.soat.eventsourcing.api.Event;
import fr.soat.eventsourcing.api.EventStore;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

@Repository
public class DBEventStore implements EventStore {

    private final JdbcTemplate jdbcTemplate;

    private static final String SELECT = "SELECT id, "
            + "aggregate_id,"
            + "event_sequence_id,"
            + "event_type,"
            + "timestamp,"
            + "content" +
            " FROM event " +
            " WHERE aggregate_id = ? " +
            " ORDER BY event_sequence_id";

    private static final String INSERT = "INSERT INTO event ("
            + "aggregate_id, "
            + "event_sequence_id, "
            + "event_type, "
            + "content) VALUES (?,?,?,?)";

    private final String TRUNCATE = "TRUNCATE event";

    public DBEventStore(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public List<Event> loadEvents(AggregateId aggregateId) {
        return jdbcTemplate.query(
                SELECT,
                new Object[]{aggregateId.getValue()},
                new EventMapper());
    }

    @Override
    public void store(AggregateId aggregateId, List<Event> events) {
        List<Object[]> batchArgs = createInsertBatchArgs(aggregateId, events);
        jdbcTemplate.batchUpdate(INSERT, batchArgs);
    }

    private List<Object[]> createInsertBatchArgs(AggregateId aggregateId, List<Event> events) {
        List<Object[]> batchArgs = new ArrayList<>();
        for (int i = 0; i < events.size(); i++) {
            Event event = events.get(i);
            batchArgs.add(new Object[] {
                    aggregateId.getValue(),
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
