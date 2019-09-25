import kafka.AtLeastOnceConsumer;
import kafka.Producer;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.util.concurrent.CompletableFuture;

@Slf4j
public class ProducerConsumerTest {

    private BlopEventListener blopEventListener = new BlopEventListener();

    @Test
    public void should_produce_then_consume() throws InterruptedException {
        String blopTopic = "blop";

        // publish
        Producer producer = new Producer(blopTopic);
        producer.open();
        producer.publish(new BlopEvent("a blop happened !"), BlopEvent.class);
        producer.close();

        // Then consume
        AtLeastOnceConsumer consumer = new AtLeastOnceConsumer(blopEventListener, blopTopic);
        CompletableFuture.runAsync(() -> {
            consumer.start();
        });

        // then
        pendingMessage(5000);
        consumer.stop();
        Assertions.assertThat(blopEventListener.getReceived()).isNotNull();
        Assertions.assertThat(blopEventListener.getReceived().getBlop()).isEqualTo("a blop happened !");
    }

    private void pendingMessage(int timeout) throws InterruptedException {
        long waitTime = 100;
        long startTime = System.currentTimeMillis();
        while(startTime + timeout > System.currentTimeMillis()) {
            if (blopEventListener.getReceived() != null) {
                break;
            } else {
                Thread.sleep(waitTime);
            }
        }
    }


}
