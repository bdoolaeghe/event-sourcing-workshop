package fr.soat.eventsourcing.api;

import java.util.List;

public interface Entity<ENTITY_ID, EVENT_TYPE> {

//    private final List<EVENT_TYPE> changes = new ArrayList<>();

    ENTITY_ID getId();
    List<EVENT_TYPE> getEvents();

//    public Entity(ENTITY_ID id) {
//        this.id = id;
//    }

//    public Entity() {
//        this(null);
//    }

//    public List<EVENT_TYPE> getChanges() {
//        return new ArrayList<>(changes);
//    }
//
//    protected void recordChange(EVENT_TYPE event) {
//        changes.add(event);
//    }
//
//    public int getVersion() {
//        return changes.size();
//    }
//
//    @Override
//    public String toString() {
//        return getId().toString();
//    }
}
