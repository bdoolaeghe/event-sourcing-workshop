package kafka;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;

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

    public <MESSAGE_TYPE> void publish(MESSAGE_TYPE message, Class<MESSAGE_TYPE> messageClass) {
        ProducerRecord<String, String> data = new ProducerRecord<String, String>(topic, 0, messageClass.toString(), toJson(message));
        producer.send(data);
    }

    private <MESSAGE_TYPE> String toJson(MESSAGE_TYPE message) {
        //FIXME gson
        return message.toString();
    }

    public void close() {
        producer.close();
    }

}
