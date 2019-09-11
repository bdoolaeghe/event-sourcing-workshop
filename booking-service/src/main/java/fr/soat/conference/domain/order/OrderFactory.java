package fr.soat.conference.domain.order;

public class OrderFactory {

    public static Order create() {
        return new Order(OrderId.next());
    }

}
