package fr.soat.conference.domain;

import fr.soat.conference.domain.order.AccountId;
import fr.soat.conference.domain.order.Order;
import fr.soat.conference.infra.order.OrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import static fr.soat.conference.domain.order.OrderStatus.OPEN;

@Service
@Slf4j
public class ReservationProcessManager {

    private final OrderRepository orderRepository;

    @Autowired
    public ReservationProcessManager(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @EventListener
    public void on(TransferRequested transferRequested) {
        log.info("consuming {}", transferRequested.getClass().getSimpleName());
        Order senderOrder = orderRepository.load(transferRequested.getAccountId());
        AccountId senderAccountId = senderOrder.getId();
        AccountId receiverAccountId = transferRequested.getReceiverAccountId();
        Order receiverOrder = orderRepository.load(receiverAccountId);
        int amount = transferRequested.getAmount();

        if (amount <= senderOrder.getBalance() &&
                receiverOrder.getStatus() == OPEN) {
            receiverOrder.receiveTransfer(senderAccountId, amount);
            orderRepository.save(receiverOrder);
        } else {
            senderOrder.refuseTransfer(receiverAccountId, amount);
            orderRepository.save(senderOrder);
        }
    }

    @EventListener
    public void on(TransferReceived transferReceived) {
        log.info("consuming {}", transferReceived.getClass().getSimpleName());
        Order senderOrder = orderRepository.load(transferReceived.getSenderAccountId());
        AccountId receiverAccountId = transferReceived.getAccountId();
        int amount = transferReceived.getAmount();
        senderOrder.sendTransfer(receiverAccountId, amount);
        orderRepository.save(senderOrder);
    }

    @EventListener
    public void on(TransferRefused transferRefused) {
        log.info("consuming {}", transferRefused.getClass().getSimpleName());
        // nothing to do (final step)
    }

    @EventListener
    public void on(TransferSent transferSent) {
        log.info("consuming {}", transferSent.getClass().getSimpleName());
        // nothing to do (final step)
    }
}
