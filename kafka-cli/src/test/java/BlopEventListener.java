import kafka.EventListener;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BlopEventListener implements EventListener<BlopEvent> {

    @Getter
    private volatile BlopEvent received;

    public void on(BlopEvent blopEvent) {
        log.info("received message from kafka: " + blopEvent);
        this.received = blopEvent;
    }

}
