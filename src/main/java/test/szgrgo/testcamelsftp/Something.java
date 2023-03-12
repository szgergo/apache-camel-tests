package test.szgrgo.testcamelsftp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class Something {

    private static final Logger logger = LoggerFactory.getLogger(Something.class);

    public String getSomething(String line) {
        logger.info("line {}", line);
        return line;
    }
}
