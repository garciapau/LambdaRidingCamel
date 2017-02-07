package content.integration.lambda;

import org.apache.camel.CamelContext;
import org.apache.camel.ConsumerTemplate;
import org.apache.camel.Exchange;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.spring.SpringCamelContext;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.AbstractXmlApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import com.amazonaws.services.lambda.runtime.events.S3Event;

public class LambdaFunctionHandler implements RequestHandler<S3Event, String> {

    @Override
    public String handleRequest(S3Event input, Context context) {
        context.getLogger().log("\n\t\t\t ---------------- Obtained file from S3 with content:, " + input.toJson() + "\n");
        ApplicationContext spring =new ClassPathXmlApplicationContext("META-INF/spring/camel-context.xml");
        String output = new String();
        try {
        	SpringCamelContext camelContext = SpringCamelContext.springCamelContext(spring);
        	camelContext.start();
            ProducerTemplate producerTemplate = camelContext.createProducerTemplate();
            ConsumerTemplate consumerTemplate = camelContext.createConsumerTemplate();
	        Exchange exchange = consumerTemplate.receiveNoWait("direct:end");
	        context.getLogger().log("\nSending direct message to Camel...");
	        producerTemplate.sendBody("direct:start", input.toJson());
	        context.getLogger().log("\nMessage to Camel sent");
	        exchange = consumerTemplate.receiveNoWait("direct:end");
	        output = exchange.getIn().getBody().toString();
	        context.getLogger().log("\nResult is: " + output);
	        
	        context.getLogger().log("\nStopping Camel...");
	        camelContext.stop();
	        context.getLogger().log("\nCamel stopped");
	        context.getLogger().log("Remaining time at the end: " + context.getRemainingTimeInMillis() + " ms");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			context.getLogger().log("Error: " + e.getMessage());
		}

        return output;
    }

}
