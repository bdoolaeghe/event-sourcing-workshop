package fr.soat.eventsourcing.impl.db;

import fr.soat.eventsourcing.api.AggregateId;
import fr.soat.eventsourcing.api.Event;
import fr.soat.eventsourcing.api.EventStore;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.List;

public class DBEventStore implements EventStore {

    private final JdbcTemplate jdbcTemplate;

    private final String SELECT = "SELECT id, "
            + "aggregate_id,"
            + "event_sequence_id,"
            + "event_type,"
            + "timestamp,"
            + "content" +
            " FROM event " +
            " WHERE aggregate_id = ? " +
            " ORDER BY event_sequence_id";

    private final String INSERT = "INSERT INTO event ("
            + "aggregate_id,"
            + "event_sequence_id,"
            + "event_type"
            + "content) VALUES (?,?,?,?)";


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
        throw new RuntimeException("implement me !");
    }

}
