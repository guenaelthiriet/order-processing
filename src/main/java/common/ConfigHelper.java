package common;

import com.amazonaws.services.simpleworkflow.AmazonSimpleWorkflow;
import com.amazonaws.services.simpleworkflow.AmazonSimpleWorkflowAsyncClientBuilder;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import java.io.*;
import java.util.Properties;


/**
 * Configuration Helper to used to create SWF clients
 */

public class ConfigHelper {
    private Properties sampleConfig;
    private String swfServiceUrl;
    private String domain;
    private long domainRetentionPeriodInDays;

    private ConfigHelper(InputStream propertiesFile) throws IOException {
        loadProperties(propertiesFile);
    }

    private void loadProperties(InputStream propertiesFile) throws IOException {

        sampleConfig = new Properties();
        sampleConfig.load(propertiesFile);

        this.swfServiceUrl = sampleConfig.getProperty(ConfigKeys.SWF_SERVICE_URL_KEY);

        this.domain = sampleConfig.getProperty(ConfigKeys.DOMAIN_KEY);
        this.domainRetentionPeriodInDays = Long.parseLong(sampleConfig.getProperty(ConfigKeys.DOMAIN_RETENTION_PERIOD_KEY));
    }

    public static ConfigHelper createConfig() throws IOException, IllegalArgumentException {

        BasicConfigurator.configure();
        Logger.getRootLogger().setLevel(Level.ERROR);

        InputStream accessProperties = ConfigHelper.class.getClassLoader().getResourceAsStream("access.properties");
        return new ConfigHelper(accessProperties);
    }

    public AmazonSimpleWorkflow createSWFClient() {
        return AmazonSimpleWorkflowAsyncClientBuilder.standard()
//                .withCredentials(new InstanceProfileCredentialsProvider(false))
                .build();
    }

    public String getDomain() {
        return domain;
    }

    public long getDomainRetentionPeriodInDays() {
        return domainRetentionPeriodInDays;
    }

    public String getValueFromConfig(String key) {
        return sampleConfig.getProperty(key);
    }
}
