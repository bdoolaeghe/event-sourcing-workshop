import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;

@Slf4j
public class BlopEventListener {

    @Getter
    private volatile BlopEvent received;

    @EventListener
    public void on(BlopEvent blopEvent) {
        log.info("received message from kafka: " + blopEvent);
        this.received = blopEvent;
    }
}
