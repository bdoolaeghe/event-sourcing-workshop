import lombok.Getter;
import org.springframework.context.event.EventListener;

public class BlopListener {

    @Getter
    private volatile BlopEvent received;

    @EventListener
    public void on(BlopEvent blopEvent) {
        System.out.println("received messge from kafka: " + blopEvent);
        this.received = blopEvent;
    }
}
