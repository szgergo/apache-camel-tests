package test.szgrgo.testcamelsftp;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class KafkaRouteBuilder extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        from("kafka:test?brokers=localhost:9094")
                .bean(KafkaReceiver.class,"receiveFromKafka");
    }
}
