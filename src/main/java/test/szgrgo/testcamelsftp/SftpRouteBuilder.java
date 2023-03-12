package test.szgrgo.testcamelsftp;

import com.hazelcast.core.HazelcastInstance;
import org.apache.camel.AggregationStrategy;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;

import org.apache.camel.spi.IdempotentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SftpRouteBuilder extends RouteBuilder {

    @Autowired
    private HazelcastInstance hazelcastInstance;

    @Autowired
    private AggregationStrategy aggregationStrategy;

    @Autowired
    private IdempotentRepository idempotentRepository;

    @Override
    public void configure() {
        from(sftpWithReadLockIdempotency())
               .routeId("pollSftpRoute1")
               .idempotentConsumer(header("CamelFileName"), idempotentRepository)
               .log(LoggingLevel.INFO, "File ${file:name} downloaded")
               .split(body().tokenize("\n"))
                .aggregate(aggregationStrategy)
                .body()
               .to("kafka:test?brokers=localhost:9094");
    }

    private static String sftpWithReadLockIdempotency() {
        return "sftp://localhost:22?username=demo&password=demo&readLock=idempotent&readLockLoggingLevel=INFO&idempotentRepository=#idempotentRepository";
    }
}
