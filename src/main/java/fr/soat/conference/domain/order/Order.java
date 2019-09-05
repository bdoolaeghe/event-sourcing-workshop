package fr.soat.conference.domain.order;


import fr.soat.conference.domain.booking.ConferenceName;
import fr.soat.conference.domain.booking.Seat;
import fr.soat.conference.domain.payment.AccountId;
import fr.soat.conference.domain.payment.PaymentReference;
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
    private PaymentReference paymentReference;

    public Order(OrderId orderId) {
        super(orderId);
        this.status = NEW;
    }

    @DecisionFunction
    public Order requestBooking(ConferenceName conferenceName, AccountId accountForPayment) {
        OrderRequested event = new OrderRequested(getId(), conferenceName, accountForPayment);
        apply(event);
        return this;
    }

    @EvolutionFunction
    void apply(OrderRequested orderRequested) {
        //FIXME
        // should init the state of order (accountId, conferenceName)
        throw new RuntimeException("implement me !");
    }

    @DecisionFunction
    public Order assign(Seat bookedSeat) {
        //FIXME
        //  expected output event is:
        // - OrderSeatBooked
        throw new RuntimeException("implement me !");
    }

    @EvolutionFunction
    public void apply(OrderSeatBooked orderSeatBooked) {
        //FIXME
        // should update state (order status and assigned seat)
        throw new RuntimeException("implement me !");
    }

    @DecisionFunction
    public void failSeatBooking() {
        //FIXME
        //  expected output event is:
        // - OrderSeatBookingFailed
        throw new RuntimeException("implement me !");
    }

    @EvolutionFunction
    void apply(OrderSeatBookingFailed orderSeatBookingFailed) {
        //FIXME
        // should update state:
        // - order status
        // - (no) assigned seat
        throw new RuntimeException("implement me !");
    }

    @DecisionFunction
    public void confirmPayment(PaymentReference paymentReference) {
        //FIXME
        //  expected output event is:
        // - OrderPaid
        throw new RuntimeException("implement me !");
    }

    @EvolutionFunction
    void apply(OrderPaid orderPaid) {
        //FIXME
        // should update state:
        // - order status
        // - the payment reference
        throw new RuntimeException("implement me !");
    }

    @DecisionFunction
    public void refusePayment() {
        //FIXME
        //  expected output event is:
        // - OrderPaymentRefused
    }

    @EvolutionFunction
    void apply(OrderPaymentRefused orderPaymentRefused) {
        //FIXME
        // should update state:
        // - order status
        // - (no) payment reference
        // - but also the fact the is NO more assigned seat !
        throw new RuntimeException("implement me !");
    }

}
