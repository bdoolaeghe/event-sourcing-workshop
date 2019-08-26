package fr.soat.conference.domain.order;


import fr.soat.conference.domain.booking.ConferenceName;
import fr.soat.conference.domain.booking.Seat;
import fr.soat.conference.domain.payment.AccountId;
import fr.soat.conference.domain.payment.PaymentAccepted;
import fr.soat.eventsourcing.api.AggregateRoot;
import fr.soat.eventsourcing.api.DecisionFunction;
import fr.soat.eventsourcing.api.EvolutionFunction;
import lombok.Getter;

import static fr.soat.conference.domain.order.OrderStatus.*;

@Getter
public class Order extends AggregateRoot<OrderId> {

    private AccountId accountId;
    private ConferenceName conferenceName;
    private Seat seat;
    private OrderStatus status;

    public Order(OrderId orderId) {
        super(orderId);
        this.status = NEW;
    }

    /* aggregate evolutions  */

    @EvolutionFunction
    void apply(OrderCreated orderCreated) {
        this.accountId = orderCreated.getAccountId();
        this.conferenceName = orderCreated.getConferenceName();
        recordChange(orderCreated);
    }

    @EvolutionFunction
    void apply(OrderConfirmed orderConfirmed) {
        this.status = CONFIRMED;
        recordChange(orderConfirmed);
    }

    @EvolutionFunction
    void apply(OrderRefused orderRefused) {
        this.status = REFUSED;
        recordChange(orderRefused);
    }

    public boolean isConfirmed() {
        return status == CONFIRMED;
    }

    public boolean isRefused() {
        return status == REFUSED;
    }

    /* decisions invoked by commands */

    @DecisionFunction
    public Order requestSeat(ConferenceName conferenceName, AccountId accountForPayment) {
        OrderCreated event = new OrderCreated(getId(), conferenceName, accountForPayment);
        apply(event);
        return this;
    }

    @EvolutionFunction
    public void apply(SeatAssigned seatBooked) {
        this.seat = seatBooked.getBookedSeat();
        recordChange(seatBooked);
    }

    public void apply(PaymentAccepted paymentAccepted) {
        recordChange(paymentAccepted);
    }

    @DecisionFunction
    public void confirmRequest() {
        apply(new OrderConfirmed(getId()));
    }

    public void refuseRequest() {
        apply(new OrderRefused(getId()));
    }

    @DecisionFunction
    public void assign(Seat bookedSeat) {
        apply(new SeatAssigned(getId(), bookedSeat));
    }

    /* Transfer management */

//    @DecisionFunction
//    public Order requestTransfer(OrderId receiverOrderId, int amount) {
//        if (status != OPEN) {
//            throw new UnsupportedOperationException("Can not transfer from a " + status + " account");
//        }
//        apply(new TransferRequested(getId(), receiverOrderId, amount));
//        return this;
//    }
//
//    @DecisionFunction
//    public void receiveTransfer(OrderId sourceOrderId, int amount) {
//        apply(new TransferReceived(getId(), sourceOrderId, amount));
//    }
//
//    @DecisionFunction
//    public void refuseTransfer(OrderId targetOrderId, int amount ) {
//        apply(new TransferRefused(getId(), targetOrderId, amount));
//    }
//
//    @DecisionFunction
//    public void sendTransfer(OrderId targetOrderId, int amount ) {
//        apply(new TransferSent(getId(), targetOrderId, amount));
//    }
//
//    @EvolutionFunction
//    public void apply(TransferRequested transferRequested) {
//        recordChange(transferRequested);
//    }
//
//    @EvolutionFunction
//    public void apply(TransferRefused event) {
//        recordChange(event);
//    }
//
//    @EvolutionFunction
//    public void apply(TransferSent event) {
//        this.balance -= event.getAmount();
//        recordChange(event);
//    }
//
//    @EvolutionFunction
//    public void apply(TransferReceived event) {
//        this.balance += event.getAmount();
//        recordChange(event);
//    }

}
