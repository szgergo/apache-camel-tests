package test.szgrgo.testcamelsftp;

import com.hazelcast.core.HazelcastInstance;
import org.apache.camel.processor.idempotent.hazelcast.HazelcastIdempotentRepository;
import org.apache.camel.spi.IdempotentRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class IdempotentRepositoryConfiguration {

    @Bean
    public IdempotentRepository idempotentRepository(HazelcastInstance hazelcastInstance) {
        return new HazelcastIdempotentRepository(hazelcastInstance);
    }
}
