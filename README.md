# order-processing
A SWF simple order processing application based on AWS samples

## Running

The workflow host

    java -cp target/order-processing-1.0-SNAPSHOT.jar -Dloader.main=org.order.WorkflowHost org.springframework.boot.loader.PropertiesLauncher

The activity host

    java -cp target/order-processing-1.0-SNAPSHOT.jar -Dloader.main=org.order.ActivityHost org.springframework.boot.loader.PropertiesLauncher

The workflow trigger

    java -cp target/order-processing-1.0-SNAPSHOT.jar -Dloader.main=org.order.WorkflowExecutionStarter org.springframework.boot.loader.PropertiesLauncher