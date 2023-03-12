package test.szgrgo.testcamelsftp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KafkaReceiver {

    private static final Logger logger = LoggerFactory.getLogger(KafkaReceiver.class);

    public void receiveFromKafka(String message) {
        logger.info("Message from kafka {}", message);
    }
}
