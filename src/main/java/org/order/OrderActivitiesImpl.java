package org.order;

import java.security.SecureRandom;
import java.util.Random;
import java.util.UUID;

public class OrderActivitiesImpl implements OrderActivities {

    @Override
    public int createOrder(int requestId) {
        System.out.printf("Creating order for Request ID: %d...\n", requestId);
        return new SecureRandom().nextInt();
    }

    @Override
    public void orderReview(int orderId) {
        System.out.printf("Order un review for orderId ID: %d...\n", orderId);
    }

    @Override
    public void orderManufacturing(int orderId) {
        System.out.printf("Manufacturing order '%d'...\n", orderId);
    }

    @Override
    public void orderPreparation(int orderId) {
        System.out.printf("Order preparation '%d'...\n", orderId);
    }

    @Override
    public void orderReadyForPickup(int orderId, int customerId) {
        System.out.printf("Notifying customer '%d' that order %d is ready...\n", customerId, orderId);
    }

    @Override
    public void orderComplete(int orderId) {
        System.out.printf("Order completed '%d'...\n", orderId);
    }
}
