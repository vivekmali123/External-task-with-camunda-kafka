package com.camunda.rnw;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.camunda.bpm.client.ExternalTaskClient;
import org.camunda.bpm.client.topic.TopicSubscriptionBuilder;

public class RejectionNotificationExternalWorker {

	public static void main(String[] args) {
    
		// Camunda REST API  
		
		// bootstrap the client
	    ExternalTaskClient client = ExternalTaskClient.create()
        .baseUrl("http://localhost:8080/engine-rest")
	        .asyncResponseTimeout(20000)
        .lockDuration(10000)
        .maxTasks(1)
        .build();
		
	 // subscribe to the topic
	    TopicSubscriptionBuilder subscriptionBuilder = client
        .subscribe("notification");
	    
	 // handle job
	    subscriptionBuilder.handler((externalTask, externalTaskService) -> {
		      
	    	  String content = externalTask.getVariable("content"); // it was content for loan example
	          System.out.println("Sorry, your loan has been rejected: " + content); 
		     
	          // Write you business logic or call respective layers 
	          
	          Map<String, Object> variables = new HashMap<String, Object>();
		          variables.put("notficationTimestamp", new Date());
		          variables.put("isNotificationSent", true);
			   try {
				Thread.sleep(20000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}    
		          externalTaskService.complete(externalTask, variables);
	    });
	    subscriptionBuilder.open();
	}

	
	
	
	
}
