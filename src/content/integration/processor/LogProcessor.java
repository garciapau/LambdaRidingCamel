package content.integration.processor;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import java.util.Map;

/**
 * Created by u6023035 on 24/01/2017.
 */
public class LogProcessor implements Processor {

    public void process(Exchange exchange) throws Exception {
        System.out.println("Logger: "
                + "\t\theaders size:"+ exchange.getIn().getHeaders().size() + "\n");
        for (Map.Entry<String, Object> currentHeader : exchange.getIn().getHeaders().entrySet())
        {
            System.out.println("\t\tHeader name:" + currentHeader.getKey() + "\tValue:" + currentHeader.getValue());
        }
        System.out.println("\n\t\tBody:" + exchange.getIn().getBody(String.class));
    }
}
