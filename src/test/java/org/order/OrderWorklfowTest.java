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

    List<String> trace = new ArrayList<String>();

    private OrderWorkflowClientFactory workflowFactory = new OrderWorkflowClientFactoryImpl();

    @Before
    public void setUp() throws Exception {

        // Register activity implementation to be used during test run
        // In real life some mocking framework to be used
        OrderActivities activities = new OrderActivities() {

            @Override
            public void orderManufacturing(int customerId) {
                trace.add("orderManufacturing-" + customerId);
            }

            @Override
            public void orderPreparation(int requestId) {
                trace.add("orderPreparation-" + requestId);
            }

            @Override
            public void orderReadyForPickup(int orderId, int customerId) {
                trace.add("orderReadyForPickup-" + orderId + "-" + customerId);
            }

            @Override
            public void orderComplete(int requestId) {
                trace.add("orderComplete-" + requestId);
            }

            @Override
            public Integer createOrder(int requestId) {
                trace.add(1234 + "-createOrder-" + requestId);
                return 1234;
            }

            @Override
            public void orderReview(int orderId) {
                trace.add("orderReview-" + orderId);
            }
        };
        workflowTest.addActivitiesImplementation(activities);
        workflowTest.addWorkflowImplementationType(OrderWorkflowImpl.class);
    }

    @Test
    public void testOrder() {
        OrderWorkflowClient workflow = workflowFactory.getClient();
        Promise<Void> booked = workflow.makeOrder(123, 345);
        List<String> expected = new ArrayList<String>();
        expected.add("1234-createOrder-123");
        expected.add("orderReview-1234");
        expected.add("orderManufacturing-1234");
        expected.add("orderPreparation-1234");
        expected.add("orderReadyForPickup-1234-345");
        expected.add("orderComplete-1234");
        AsyncAssert.assertEqualsWaitFor(expected, trace, booked);
    }

}
