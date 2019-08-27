package fr.soat.conference.domain.order;


import fr.soat.conference.domain.booking.ConferenceName;
import fr.soat.conference.domain.booking.Seat;
import fr.soat.conference.domain.payment.AccountId;
import fr.soat.eventsourcing.api.AggregateRoot;
import fr.soat.eventsourcing.api.DecisionFunction;
import fr.soat.eventsourcing.api.EvolutionFunction;
import lombok.Getter;
import lombok.ToString;

import static fr.soat.conference.domain.order.OrderStatus.*;

@Getter
@ToString(callSuper = true, of = {"status", "conferenceName", "accoundId", "seat" })
public class Order extends AggregateRoot<OrderId> {

    private OrderStatus status;
    private ConferenceName conferenceName;
    private AccountId accountId;

    private Seat seat;

    public Order(OrderId orderId) {
        super(orderId);
        this.status = NEW;
    }

    @DecisionFunction
    public Order requestSeat(ConferenceName conferenceName, AccountId accountForPayment) {
        OrderCreated event = new OrderCreated(getId(), conferenceName, accountForPayment);
        apply(event);
        return this;
    }

    @EvolutionFunction
    void apply(OrderCreated orderCreated) {
        this.accountId = orderCreated.getAccountId();
        this.conferenceName = orderCreated.getConferenceName();
        recordChange(orderCreated);
    }

    @DecisionFunction
    public void assign(Seat bookedSeat) {
        apply(new SeatAssigned(getId(), bookedSeat));
    }

    @EvolutionFunction
    public void apply(SeatAssigned seatBooked) {
        this.seat = seatBooked.getBookedSeat();
        recordChange(seatBooked);
    }

    @DecisionFunction
    public void confirmRequest() {
        apply(new OrderConfirmed(getId()));
    }

    @EvolutionFunction
    void apply(OrderConfirmed orderConfirmed) {
        this.status = CONFIRMED;
        recordChange(orderConfirmed);
    }

    @DecisionFunction
    public void refuseRequest() {
        apply(new OrderRefused(getId()));
    }

    @EvolutionFunction
    void apply(OrderRefused orderRefused) {
        this.status = REFUSED;
        this.seat = null;
        recordChange(orderRefused);
    }

}
