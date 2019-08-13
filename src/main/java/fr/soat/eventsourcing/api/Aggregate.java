package fr.soat.eventsourcing.api;

public interface Aggregate<AGGREGATE_ID> {

    AGGREGATE_ID getId();

    int getVersion();

}
