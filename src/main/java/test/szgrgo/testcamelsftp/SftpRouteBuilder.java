package test.szgrgo.testcamelsftp;

import org.apache.camel.AggregationStrategy;
import org.apache.camel.CamelContext;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.processor.idempotent.jdbc.JdbcMessageIdRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionTemplate;

import javax.sql.DataSource;

@Component
public class SftpRouteBuilder extends RouteBuilder {

    @Autowired
    private CamelContext camelContext;

    @Autowired
    private TransactionTemplate transactionTemplate;

    @Autowired
    private DataSource dataSource;
    @Override
    public void configure() {
        AggregationStrategy aggregationStrategy = new MultiLineAggregationStrategy();
        JdbcMessageIdRepository idempotentRepository = new JdbcMessageIdRepository(dataSource,
                transactionTemplate,
                "testProcessor");
        //Idempotency table: setCreateTableIfNotExists does not work, table can't be created from here, needs manual work or put in docker-compose:
        // SQL: CREATE TABLE CAMEL_MESSAGEPROCESSED (processorName VARCHAR(255), messageId VARCHAR(100), createdAt TIMESTAMP, PRIMARY KEY (processorName, messageId))
        camelContext.getRegistry().bind("idempotentRepository", idempotentRepository);


        from("sftp://localhost:22?username=demo&password=demo")
               .routeId("pollSftpRoute1")
               .idempotentConsumer(header("CamelFileName"), idempotentRepository)
               .log(LoggingLevel.INFO, "File ${file:name} downloaded")
               .split(body().tokenize("\n"))
                .aggregate(aggregationStrategy)
                .body()
               .to("kafka:test?brokers=localhost:9094");
    }
}
