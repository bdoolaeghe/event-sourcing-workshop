package kafka;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;

@Slf4j
public class Producer {

    private final String topic;
    private KafkaProducer<String, String> producer;

    public Producer(String topic) {
        this.topic = topic;
    }

    public void open() {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        producer = new KafkaProducer(props);
//        for (int i = 0; i < 1000; i++) {
//            ProducerRecord<String, String> data;
//            if (i % 2 == 0) {
//                data = new ProducerRecord<String, String>("even", 0, Integer.toString(i), String.format("%d is even", i));
//            } else {
//                data = new ProducerRecord<String, String>("odd", 0, Integer.toString(i), String.format("%d is odd", i));
//            }
//            Thread.sleep(1L);
//        }
    }

    public <EVENT_TYPE> void publish(EVENT_TYPE event, Class<EVENT_TYPE> eventClass) {
        ProducerRecord<String, String> data = new ProducerRecord<String, String>(topic, 0, eventClass.getName(), toJson(event));
        producer.send(data);
        log.debug("Sent: " + data);
    }

    final GsonBuilder builder = new GsonBuilder();
    final Gson gson = builder.create();

    private <EVENT_TYPE> String toJson(EVENT_TYPE event) {
        String json = gson.toJson(event);
        return json;
    }

    public void close() {
        producer.close();
    }

}
