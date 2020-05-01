package org.order;

import com.amazonaws.services.simpleworkflow.flow.annotations.Activities;
import com.amazonaws.services.simpleworkflow.flow.annotations.ActivityRegistrationOptions;
import com.amazonaws.services.simpleworkflow.flow.common.FlowConstants;

import java.util.UUID;

@Activities(version = "1.0")
@ActivityRegistrationOptions(
        defaultTaskHeartbeatTimeoutSeconds = FlowConstants.NONE,
        defaultTaskScheduleToCloseTimeoutSeconds = 300,
        defaultTaskScheduleToStartTimeoutSeconds = 60,
        defaultTaskStartToCloseTimeoutSeconds = 60)
public interface OrderActivities {

    Integer createOrder(int requestId);

    void orderReview(int orderId);

    void orderManufacturing(int orderId);

    void orderPreparation(int orderId);

    void orderReadyForPickup(int orderId, int customerId);

    void orderComplete(int orderId);
}
