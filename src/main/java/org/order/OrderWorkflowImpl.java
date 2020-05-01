package org.order;

import com.amazonaws.services.simpleworkflow.flow.core.Promise;

public class OrderWorkflowImpl implements OrderWorkflow {

    private final OrderActivitiesClient client = new OrderActivitiesClientImpl();

    @Override
    public void makeOrder(int requestID, int customerID) {
        Promise<Integer> orderId = client.createOrder(requestID);
        Promise<Void> orderReview = client.orderReview(orderId);
        Promise<Void> orderManufacturing = client.orderManufacturing(orderId, orderReview);
        Promise<Void> orderPreparation = client.orderPreparation(orderId, orderManufacturing);
        Promise<Void> orderReadyForPickup = client.orderReadyForPickup(orderId, Promise.asPromise(customerID), orderPreparation);
        client.orderComplete(orderId, orderReadyForPickup);
    }
    
}
