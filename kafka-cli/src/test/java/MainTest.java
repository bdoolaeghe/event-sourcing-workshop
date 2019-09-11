import kafka.Consumer;
import kafka.Producer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.TimeUnit;

@RunWith(SpringRunner.class)
//@ContextConfiguration //(classes = MainTestConfig.class)
public class MainTest {

    @Autowired
    ApplicationEventPublisher applicationEventPublisher;

    @Test
    public void doit() throws InterruptedException {
        Producer producer = new Producer("blop");
        Consumer consumer = new Consumer(applicationEventPublisher, "blop");
        consumer.start();
        producer.open();
        producer.publish("blop !", String.class);
        producer.close();
        consumer.stop();
        TimeUnit.SECONDS.sleep(5);
    }

    @EventListener
    public void on(String msg) {
        System.out.println("received messge from kafka: " + msg);
    }

}
