package fr.soat.festival.domain;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ConferenceBookingProcessManager {
//
//    private final OrderRepository orderRepository;
//    private final ConferenceRepository conferenceRepository;
//    private final AccountRepository accountRepository;
//
//    @Autowired
//    public ConferenceBookingProcessManager(OrderRepository orderRepository, ConferenceRepository conferenceRepository, AccountRepository accountRepository) {
//        this.orderRepository = orderRepository;
//        this.conferenceRepository = conferenceRepository;
//        this.accountRepository = accountRepository;
//    }
//
//    @EventListener
//    public void on(OrderRequested orderRequested) {
//        log.info("consuming {}", orderRequested.getClass().getSimpleName());
//        // book a seat
//        Conference conference = conferenceRepository.load(orderRequested.getConferenceName());
//        conference.bookSeat(orderRequested.getOrderId());
//        conferenceRepository.save(conference);
//    }
//
//    @EventListener
//    public void on(SeatBooked seatBooked) {
//        log.info("consuming {}", seatBooked.getClass().getSimpleName());
//        Order order = orderRepository.load(seatBooked.getOrderId());
//        // save seat
//        order.assign(seatBooked.getSeat());
//        orderRepository.save(order);
//
//        // make payment
//        Account account = accountRepository.load(order.getAccountId());
//        Conference conference = conferenceRepository.load(order.getConferenceName());
//        account.requestPayment(conference.getSeatPrice(), order.getId());
//        accountRepository.save(account);
//    }
//
//    @EventListener
//    public void on(SeatBookingRequestRefused seatBookingRequestRefused) {
//        log.info("consuming {}", seatBookingRequestRefused.getClass().getSimpleName());
//        Order order = orderRepository.load(seatBookingRequestRefused.getOrderId());
//        order.failSeatBooking();
//        orderRepository.save(order);
//    }
//
//    @EventListener
//    public void on(PaymentAccepted paymentAccepted) {
//        log.info("consuming {}", paymentAccepted.getClass().getSimpleName());
//        // confirm order
//        Order order = orderRepository.load(paymentAccepted.getOrderId());
//        order.confirmPayment(paymentAccepted.getPaymentReference());
//        orderRepository.save(order);
//    }
//
//    @EventListener
//    public void on(PaymentRefused paymentRefused) {
//        log.info("consuming {}", paymentRefused.getClass().getSimpleName());
//        // cancel order
//        Order order = orderRepository.load(paymentRefused.getOrderId());
//        order.refusePayment();
//        orderRepository.save(order);
//
//        // cancel seat booking
//        Conference conference = conferenceRepository.load(order.getConferenceName());
//        conference.cancelBooking(order.getSeat());
//        conferenceRepository.save(conference);
//    }
}
