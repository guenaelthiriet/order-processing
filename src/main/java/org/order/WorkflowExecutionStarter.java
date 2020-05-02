package org.order;

import com.amazonaws.services.simpleworkflow.AmazonSimpleWorkflow;
import common.ConfigHelper;
import org.springframework.boot.autoconfigure.SpringBootApplication;

public class WorkflowExecutionStarter {
    private static AmazonSimpleWorkflow swfService;
    private static String domain;
    
    public static void main(String[] args) throws Exception {

    	// Load configuration
    	ConfigHelper configHelper = ConfigHelper.createConfig();
        
        // Create the client for Simple Workflow Service
        swfService = configHelper.createSWFClient();
        domain = configHelper.getDomain();
        
        // Start Workflow instance
        int requestId = Integer.parseInt(configHelper.getValueFromConfig(OrderConfigKeys.WORKFLOW_INPUT_REQUESTID_KEY));
        int customerId = Integer.parseInt(configHelper.getValueFromConfig(OrderConfigKeys.WORKFLOW_INPUT_CUSTOMERID_KEY));
        
        OrderWorkflowClientExternalFactory clientFactory = new OrderWorkflowClientExternalFactoryImpl(swfService, domain);
        OrderWorkflowClientExternal workflow = clientFactory.getClient();
        workflow.makeOrder(requestId, customerId);
        
        System.exit(0);
    }
}
