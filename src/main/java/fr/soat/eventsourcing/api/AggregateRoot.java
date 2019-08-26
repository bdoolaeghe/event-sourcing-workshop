package fr.soat.eventsourcing.api;

import java.util.ArrayList;
import java.util.List;

public abstract class AggregateRoot<AGGREGATE_ID> {

    private AGGREGATE_ID id;
    private List<Event> changes = new ArrayList<>();

    public AggregateRoot(AGGREGATE_ID id) {
        this.id = id;
    }

    public AGGREGATE_ID getId() {
        return id;
    }

    public List<Event> getChanges() {
        return new ArrayList<>(changes);
    }

    protected void recordChange(Event event) {
        changes.add(event);
    }

    public int getVersion() {
        return changes.size();
    }

}
