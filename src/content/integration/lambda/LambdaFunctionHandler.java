package content.integration.lambda;

import java.io.IOException;

import org.apache.camel.ConsumerTemplate;
import org.apache.camel.Exchange;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.spring.SpringCamelContext;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import com.amazonaws.services.lambda.runtime.events.S3Event;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.util.IOUtils;

public class LambdaFunctionHandler implements RequestHandler<S3Event, String> {

    @Override
    public String handleRequest(S3Event input, Context context) {
        String fileContent = getS3File(input, context);
        
        ApplicationContext spring =new ClassPathXmlApplicationContext("META-INF/spring/camel-context.xml");
        String output = new String();
        SpringCamelContext camelContext = null;
        try {
        	camelContext = SpringCamelContext.springCamelContext(spring);
        	camelContext.start();
		} catch (Exception e) {
			context.getLogger().log("Error: " + e.getMessage());
			return "Error creating camel context";
		}
    	ProducerTemplate producerTemplate = camelContext.createProducerTemplate();
        ConsumerTemplate consumerTemplate = camelContext.createConsumerTemplate();
        output = consumerTemplate.receiveBodyNoWait("direct:end", String.class);
        context.getLogger().log("\nSending direct message to Camel...");
        producerTemplate.sendBody("direct:start", fileContent);
        context.getLogger().log("\nMessage to Camel sent");
        output = consumerTemplate.receiveBodyNoWait("direct:end", String.class);
        context.getLogger().log("\nResult is: " + camelReturn);
        context.getLogger().log("\nStopping Camel...");
        try {
			camelContext.stop();
		} catch (Exception e) {
			context.getLogger().log("Error: " + e.getMessage());
			return "Error createing camel context";
		}
        context.getLogger().log("\nCamel stopped");
        context.getLogger().log("Remaining time at the end: " + context.getRemainingTimeInMillis() + " ms");
        return output;
    }

	private String getS3File(S3Event input, Context context) {
        String s3Event = input.toJson();
		context.getLogger().log("\n\t\t\t ---------------- Obtained event from S3:, " + input.toJson() + "\n");
        String S3BucketName = input.getRecords().get(0).getS3().getBucket().getName();
		String s3ObjectKey = input.getRecords().get(0).getS3().getObject().getKey();
		String fileContent = "";
        AmazonS3 s3 = AmazonS3ClientBuilder.standard().build();
		try {
			fileContent = IOUtils.toString(s3.getObject(new GetObjectRequest(S3BucketName, s3ObjectKey)).getObjectContent());
		} catch (IOException e) {
			context.getLogger().log("Error: " + e.getMessage());
			return "Error getting S3 file";
		}
		return fileContent;
	}

}
