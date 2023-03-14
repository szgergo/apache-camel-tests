package test.szgrgo.testcamelsftp;

import org.apache.camel.api.management.ManagedOperation;
import org.apache.camel.processor.idempotent.jdbc.JdbcMessageIdRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.support.TransactionTemplate;

import javax.sql.DataSource;

public class MyJdbcMessageIdRepository extends JdbcMessageIdRepository {

    private static final Logger logger = LoggerFactory.getLogger(MyJdbcMessageIdRepository.class);

    public MyJdbcMessageIdRepository(DataSource dataSource,
                                     TransactionTemplate transactionTemplate,
                                     String processorName) {
        super(dataSource, transactionTemplate, processorName);
    }

    @ManagedOperation(description = "Adds the key to the store")
    @Override
    public boolean add(final String key) {
        try {
            return super.add(key);
        } catch (Throwable e) {
            logger.warn("Unable to insert messageId {} to idempotent store. Rolled back", key);
        }

        return false;
    }
}
