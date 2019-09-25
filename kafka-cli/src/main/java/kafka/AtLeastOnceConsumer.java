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
public class AtLeastOnceConsumer {

    private final String[] topics;
    private final EventListener eventListener;
    private volatile boolean running;

    public AtLeastOnceConsumer(EventListener eventListener, String... topics) {
        this.eventListener = eventListener;
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
                    log.debug("waiting for message...");
            } else {
                log.debug("messageS received...");
                for (ConsumerRecord<String, String> rec : recs) {
                    try {
                        Object message = fromJson(rec.key(), rec.value());
                        log.debug("received: " + message);
                        eventListener.on(message);
                    } catch (Exception e) {
                        log.warn("Failure. Discarding event: " + rec.value(), e);
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


//    public static void main(String[] args) {
//        AtLeastOnceConsumer consumer = new AtLeastOnceConsumer(new ApplicationEventPublisher() {
//            @Override
//            public void publishEvent(Object o) {
//                System.out.println("Kafka message received: " + o);
//            }
//        }, "blop");
//        consumer.start();
//    }
}
