package org.order;

import com.amazonaws.services.simpleworkflow.flow.annotations.Execute;
import com.amazonaws.services.simpleworkflow.flow.annotations.Workflow;
import com.amazonaws.services.simpleworkflow.flow.annotations.WorkflowRegistrationOptions;

@Workflow
@WorkflowRegistrationOptions(defaultExecutionStartToCloseTimeoutSeconds = 600, defaultTaskStartToCloseTimeoutSeconds = 10)
public interface OrderWorkflow {

    @Execute(name = "MakeOrder", version = "1.0")
    void makeOrder(int requestID, int customerID);

}
