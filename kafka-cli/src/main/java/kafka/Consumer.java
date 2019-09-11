package kafka;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.context.ApplicationEventPublisher;

import java.util.Arrays;
import java.util.Properties;

public class Consumer {

    private final ApplicationEventPublisher applicationEventPublisher;
    private final String[] topics;
    private volatile boolean running;

    public Consumer(ApplicationEventPublisher applicationEventPublisher, String ... topics) {
        this.applicationEventPublisher = applicationEventPublisher;
        this.topics = topics;
    }

    public void start() {
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "test-consumer-group");
        KafkaConsumer consumer = new KafkaConsumer(props);
        consumer.subscribe(Arrays.asList(topics));
        running = true;
        while (running) {
            ConsumerRecords<String, String> recs = consumer.poll(10);
            if (recs.count() == 0) {
            } else {
                for (ConsumerRecord<String, String> rec : recs) {
                    Object message = fromJson(rec.key(), rec.value());
                    System.out.printf("received: " + message);
                    applicationEventPublisher.publishEvent(message);
                }
            }
        }
    }

    private Object fromJson(String key, String value) {
        //FIXME
        return value;
    }

    public void stop() {
        running = false;
    }


//
//


}
