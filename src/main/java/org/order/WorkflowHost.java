package org.order;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import com.amazonaws.services.simpleworkflow.AmazonSimpleWorkflow;
import com.amazonaws.services.simpleworkflow.flow.WorkflowWorker;
import common.ConfigHelper;

public class WorkflowHost {
    private static AmazonSimpleWorkflow swfService;
    private static String domain;
    private static long domainRetentionPeriodInDays;
    private static WorkflowWorker worker;
    private static WorkflowHost host;

    // Factory method for Workflow Host
    public synchronized static WorkflowHost getWorkflowHost() {
        if (host == null) {
            host = new WorkflowHost();
        }
        return host;
    }

    public static void main(String[] args) throws Exception {
    	ConfigHelper configHelper = loadConfiguration();
    	
        getWorkflowHost().startWorkflowWorker(configHelper);

        // Add a Shutdown hook to close WorkflowWorker
        addShutDownHook();
        
        System.out.println("Please press any key to terminate service.");
        try {
            System.in.read();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        System.exit(0);
    }
        
    private void startWorkflowWorker(ConfigHelper configHelper) throws Exception {
        System.out.println("Starting Workflow Host Service...");

        String taskList = configHelper.getValueFromConfig(OrderConfigKeys.WORKFLOW_WORKER_TASKLIST);
        worker = new WorkflowWorker(swfService, domain, taskList);
        worker.setDomainRetentionPeriodInDays(domainRetentionPeriodInDays);
        worker.setRegisterDomain(true);
        worker.addWorkflowImplementationType(OrderWorkflowImpl.class);
        // Start the worker threads
        worker.start();

        System.out.println("Workflow Host Service Started...");
    }

    private void stopHost() throws InterruptedException {
        System.out.println("Stopping Workflow Host Service...");
        worker.shutdownNow();
        swfService.shutdown();
        worker.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);
        System.out.println("Workflow Host Service Stopped...");
    }
    
    static ConfigHelper loadConfiguration() throws IllegalArgumentException, IOException{
        ConfigHelper configHelper = ConfigHelper.createConfig();

        // Create the client for Simple Workflow Service
        swfService = configHelper.createSWFClient();
        domain = configHelper.getDomain();
        domainRetentionPeriodInDays = configHelper.getDomainRetentionPeriodInDays();

        return configHelper;
    }
    
    static void addShutDownHook(){
  	  Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {

            public void run() {
                try {
                    getWorkflowHost().stopHost();
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }));    	
  }
}
