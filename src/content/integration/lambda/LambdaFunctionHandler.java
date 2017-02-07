package content.integration.lambda;

import org.apache.camel.CamelContext;
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
        context.getLogger().log("Input: " + input);
        context.getLogger().log("Remaining time: " + context.getRemainingTimeInMillis() + " ms");
        
        context.getLogger().log("\n\t\t\t ---------------- Hello, " + input.toJson() + "!");

        String filename = "META-INF/spring/camel-context.xml";
        ApplicationContext spring =
                new ClassPathXmlApplicationContext(filename);
        
        try {
        	context.getLogger().log("\nstarting Camel...");
        	SpringCamelContext camelContext = SpringCamelContext.springCamelContext(spring);
        	context.getLogger().log("\nCamel loaded context");

        	camelContext.start();
	        context.getLogger().log("\nSending direct message to Camel...");
            ProducerTemplate producerTemplate = camelContext.createProducerTemplate();
	        producerTemplate.sendBody("direct:start", "Starting manual route... Camel Rocks");
	        context.getLogger().log("\nSent message to Camel");

	        camelContext.stop();
	        context.getLogger().log("\nCamel stopped");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			context.getLogger().log("Error: " + e.getMessage());
			e.printStackTrace();
		}

        return "Lambda OK";
    }

}
