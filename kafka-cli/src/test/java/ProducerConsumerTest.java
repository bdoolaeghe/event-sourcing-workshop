import kafka.AtLeastOnceConsumer;
import kafka.Producer;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = MainTestConfig.class)
public class ProducerConsumerTest {

    @Autowired
    ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    private BlopEventListener blopEventListener;

    @Test
    public void should_produce_then_consume() throws InterruptedException {
        String blopTopic = "blop";

        // publish
        Producer producer = new Producer(blopTopic);
        producer.open();
        producer.publish(new BlopEvent("a blop happened !"), BlopEvent.class);
        producer.close();

        // Then consume
        AtLeastOnceConsumer consumer = new AtLeastOnceConsumer(applicationEventPublisher, blopTopic);
        CompletableFuture.runAsync(() -> {
            consumer.start();
        });

        // then
        TimeUnit.SECONDS.sleep(5);
        consumer.stop();

        Assertions.assertThat(blopEventListener.getReceived().getBlop()).isEqualTo("a blop happened !");
    }




}
