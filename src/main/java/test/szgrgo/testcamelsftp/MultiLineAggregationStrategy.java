package test.szgrgo.testcamelsftp;

import org.apache.camel.AggregationStrategy;
import org.apache.camel.Exchange;
import org.apache.camel.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class MultiLineAggregationStrategy implements AggregationStrategy, Predicate {

    private final List<String> list = new ArrayList<>();

    @Override
    public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {

        String body = newExchange.getIn().getBody(String.class);
        list.add(body);
        if (oldExchange == null) {
            return newExchange;
        }

        return oldExchange;
    }

    @Override
    public void onCompletion(Exchange exchange) {
        exchange.getIn().setBody(list.stream().collect(Collectors.joining()));
        list.clear();
    }

    @Override
    public boolean matches(Exchange exchange) {
        return list.size() >= 2 && list.get(list.size() - 1).equals("DK");
    }
}
