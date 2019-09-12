package kafka;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.context.ApplicationEventPublisher;

import java.util.Arrays;
import java.util.Properties;

@Slf4j
public class Consumer {

    private final ApplicationEventPublisher applicationEventPublisher;
    private final String[] topics;
    private volatile boolean running;

    public Consumer(ApplicationEventPublisher applicationEventPublisher, String... topics) {
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
        int count = 0;
        while (running) {
            count++;
            ConsumerRecords<String, String> recs = consumer.poll(10);
            if (recs.count() == 0) {
                if (count % 100 == 0)
                    System.out.println("waiting for message...");
            } else {
                System.out.println("messageS received...");
                for (ConsumerRecord<String, String> rec : recs) {
                    try {
                        Object message = fromJson(rec.key(), rec.value());
                        System.out.println("received: " + message);
                        applicationEventPublisher.publishEvent(message);
                    } catch (Exception e) {
                        System.err.println("Failure. Discarding event: " + rec.value());
                    } finally {
                        consumer.commitSync();
                    }
                }
            }
        }
    }

    final GsonBuilder builder = new GsonBuilder();
    final Gson gson = builder.create();

    private Object fromJson(String key, String value) {
        try {
            Object object = gson.fromJson(value, Class.forName(key));
            return object;
        } catch (ClassNotFoundException e) {
            System.err.println("Failed to parse json: " + e);
            throw new RuntimeException("failed to deser event: " + value + " (" + key + ")", e);
        }
    }

    public void stop() {
        running = false;
    }

}
