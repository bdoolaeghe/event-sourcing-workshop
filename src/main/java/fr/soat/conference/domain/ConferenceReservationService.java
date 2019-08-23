package fr.soat.conference.domain;

import fr.soat.conference.domain.order.AccountId;
import fr.soat.conference.domain.order.Order;
import fr.soat.conference.infra.order.OrderRepository;
import fr.soat.eventsourcing.api.Command;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ConferenceReservationService {

    private final OrderRepository repository;

    @Autowired
    public ConferenceReservationService(OrderRepository repository) {
        this.repository = repository;
    }

    @Command
    public AccountId openAccount(String owner) {
        final Order order = Order
                        .create()
                        .open(owner);

        repository.save(order);
        return order.getId();
    }

    public Order loadAccount(AccountId id) {
        return repository.load(id);
    }

    @Command
    public void deposit(AccountId id, int amount) {
        final Order order = repository.load(id);
        order.deposit(amount);
        repository.save(order);
    }

    @Command
    public void withdraw(AccountId id, int amount) {
        final Order order = repository.load(id);
        order.withdraw(amount);
        repository.save(order);
    }

    @Command
    public void closeAccount(AccountId id) {
        final Order order = repository.load(id);
        order.close();
        repository.save(order);
    }

    @Command
    public void transfer(AccountId idFrom, AccountId idTo, int amount) {
        final Order orderFrom = repository.load(idFrom);
        orderFrom.requestTransfer(idTo, amount);
        repository.save(orderFrom);
    }
}
