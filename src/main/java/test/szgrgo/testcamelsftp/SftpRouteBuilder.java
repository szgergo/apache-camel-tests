package test.szgrgo.testcamelsftp;

import org.apache.camel.AggregationStrategy;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class SftpRouteBuilder extends RouteBuilder {
    @Override
    public void configure() {
        AggregationStrategy aggregationStrategy = new MultiLineAggregationStrategy();
        from(sftpWithIdempotentRepository())
               .routeId("pollSftpRoute1")
               .log(LoggingLevel.INFO, "File ${file:name} downloaded")
               .split(body().tokenize("\n"))
                .aggregate(aggregationStrategy)
                .body()
               .to("kafka:test?brokers=localhost:9094");
    }

    private static String sftpWithIdempotentRepository() {
        return "sftp://localhost:22?username=demo&password=demo";
    }
}
