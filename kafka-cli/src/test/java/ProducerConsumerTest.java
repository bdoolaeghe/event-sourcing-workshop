import kafka.Consumer;
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
    private BlopListener blopListener;

    @Test
    public void should_produce_then_consume() throws InterruptedException {
        // publish on kafka
        String topic = "blop";
        Producer producer = new Producer(topic);
        producer.open();
        producer.publish(new BlopEvent("blop !"), BlopEvent.class);
        producer.close();

        // Then consume
        Consumer consumer = new Consumer(applicationEventPublisher, topic);
        CompletableFuture.runAsync(() -> {
            consumer.start();
        });

        // then
        TimeUnit.SECONDS.sleep(5);
        consumer.stop();
        Assertions.assertThat(blopListener.getReceived().getBlop()).isEqualTo("blop !");
    }




}
