package fr.soat.banking.domain;

import fr.soat.eventsourcing.api.EventListener;

public interface TransferEventListener extends EventListener{

    void on(TransferRequested transferRequested);

    void on(TransferReceived transferReceived);

    void on(TransferRefused transferRefused);

    void on(TransferSent transferSent);

}
