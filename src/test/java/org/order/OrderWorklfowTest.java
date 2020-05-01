package org.order;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.amazonaws.services.simpleworkflow.flow.core.Promise;
import com.amazonaws.services.simpleworkflow.flow.junit.AsyncAssert;
import com.amazonaws.services.simpleworkflow.flow.junit.FlowBlockJUnit4ClassRunner;
import com.amazonaws.services.simpleworkflow.flow.junit.WorkflowTest;

@RunWith(FlowBlockJUnit4ClassRunner.class)
public class OrderWorklfowTest {

    @Rule
    public WorkflowTest workflowTest = new WorkflowTest();

    List<String> trace;

    private OrderWorkflowClientFactory workflowFactory = new OrderWorkflowClientFactoryImpl();

    @Before
    public void setUp() throws Exception {
        trace = new ArrayList<String>();
        // Register activity implementation to be used during test run
        // In real life some mocking framework to be used
        OrderActivities activities = new OrderActivities() {

            @Override
            public void orderManufacturing(int customerId) {
                trace.add("sendConfirmation-" + customerId);
            }

            @Override
            public void orderPreparation(int requestId) {

            }

            @Override
            public void orderReadyForPickup(int orderId, int customerId) {

            }

            @Override
            public void orderComplete(int requestId) {

            }

            @Override
            public int createOrder(int requestId) {
                trace.add("reserveCar-" + requestId);
                return 1234;
            }

            @Override
            public void orderReview(int orderId) {
                trace.add("reserveAirline-" + orderId);
            }
        };
        workflowTest.addActivitiesImplementation(activities);
        workflowTest.addWorkflowImplementationType(OrderWorkflowImpl.class);
    }

    @After
    public void tearDown() throws Exception {
        trace = null;
    }

    @Test
    public void testReserveBoth() {
        OrderWorkflowClient workflow = workflowFactory.getClient();
        Promise<Void> booked = workflow.makeOrder(123, 345);
        List<String> expected = new ArrayList<String>();
        expected.add("reserveCar-123");
        expected.add("reserveAirline-123");
        expected.add("sendConfirmation-345");
        AsyncAssert.assertEqualsWaitFor(expected, trace, booked);
    }

    @Test
    public void testReserveAir() {
        OrderWorkflowClient workflow = workflowFactory.getClient();
        Promise<Void> booked = workflow.makeOrder(123, 345);
        List<String> expected = new ArrayList<String>();
        expected.add("reserveAirline-123");
        expected.add("sendConfirmation-345");
        AsyncAssert.assertEqualsWaitFor(expected, trace, booked);
    }

    @Test
    public void testReserveCar() {
        OrderWorkflowClient workflow = workflowFactory.getClient();
        Promise<Void> booked = workflow.makeOrder(123, 345);
        List<String> expected = new ArrayList<String>();
        expected.add("reserveCar-123");
        expected.add("sendConfirmation-345");
        AsyncAssert.assertEqualsWaitFor(expected, trace, booked);
    }

    @Test
    public void testReserveNone() {
        OrderWorkflowClient workflow = workflowFactory.getClient();
        Promise<Void> booked = workflow.makeOrder(123, 345);
        List<String> expected = new ArrayList<String>();
        expected.add("sendConfirmation-345");
        AsyncAssert.assertEqualsWaitFor(expected, trace, booked);
    }

}
