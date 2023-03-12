package test.szgrgo.testcamelsftp;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import org.apache.camel.AggregationStrategy;
import org.apache.camel.CamelContext;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.processor.idempotent.hazelcast.HazelcastIdempotentRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SftpRouteBuilder extends RouteBuilder {

    @Autowired
    private CamelContext camelContext;

    @Override
    public void configure() {

        HazelcastIdempotentRepository idempotentRepository =
                new HazelcastIdempotentRepository(Hazelcast.newHazelcastInstance());
        camelContext.getRegistry().bind("idempotentRepository",idempotentRepository);

        AggregationStrategy aggregationStrategy = new MyAggregationStrategy();
        from(sftpWithIdempotentRepository())
               .routeId("pollSftpRoute1")
               .idempotentConsumer(header("CamelFileName"), idempotentRepository)
               .log(LoggingLevel.INFO, "File ${file:name} downloaded")
               .split(body().tokenize("\n"))
                .aggregate(aggregationStrategy)
                .method(Something.class, "getSomething")
               .to("kafka:test?brokers=localhost:9094");
    }

    private static String sftpWithIdempotentRepository() {
        return "sftp://localhost:22?username=demo&password=demo&readLock=idempotent&readLockLoggingLevel=WARN&idempotentRepository=#idempotentRepository";
    }
}
