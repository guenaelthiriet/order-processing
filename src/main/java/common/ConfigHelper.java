package common;

import com.amazonaws.auth.InstanceProfileCredentialsProvider;
import com.amazonaws.services.simpleworkflow.AmazonSimpleWorkflow;
import com.amazonaws.services.simpleworkflow.AmazonSimpleWorkflowAsyncClientBuilder;
import com.google.common.io.Resources;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;


/**
 * Configuration Helper to used to create SWF clients
 */

public class ConfigHelper {
    private Properties sampleConfig;

    private String swfServiceUrl;
    private String swfAccessId;
    private String swfSecretKey;

    private String domain;
    private long domainRetentionPeriodInDays;

    private ConfigHelper(File propertiesFile) throws IOException {
        loadProperties(propertiesFile);
    }

    private void loadProperties(File propertiesFile) throws IOException {

        FileInputStream inputStream = new FileInputStream(propertiesFile);
        sampleConfig = new Properties();
        sampleConfig.load(inputStream);

        this.swfServiceUrl = sampleConfig.getProperty(ConfigKeys.SWF_SERVICE_URL_KEY);
//        this.swfAccessId = sampleConfig.getProperty(ConfigKeys.SWF_ACCESS_ID_KEY);
//        this.swfSecretKey = sampleConfig.getProperty(ConfigKeys.SWF_SECRET_KEY_KEY);

        this.domain = sampleConfig.getProperty(ConfigKeys.DOMAIN_KEY);
        this.domainRetentionPeriodInDays = Long.parseLong(sampleConfig.getProperty(ConfigKeys.DOMAIN_RETENTION_PERIOD_KEY));
    }

    public static ConfigHelper createConfig() throws IOException, IllegalArgumentException {

        BasicConfigurator.configure();
        Logger.getRootLogger().setLevel(Level.ERROR);

        ConfigHelper configHelper = null;

        boolean envVariableExists = false;
        //first check the existence of environment variable
        String sampleConfigPath = System.getenv(SampleConstants.ACCESS_PROPERTIES_ENVIRONMENT_VARIABLE);
        if (sampleConfigPath != null && sampleConfigPath.length() > 0) {
            envVariableExists = true;
        }
        File accessProperties = new File(System.getProperty(SampleConstants.HOME_DIRECTORY_PROPERTY), SampleConstants.HOME_DIRECTORY_FILENAME);

        if (accessProperties.exists()) {
            configHelper = new ConfigHelper(accessProperties);
        } else if (envVariableExists) {
            accessProperties = new File(sampleConfigPath, SampleConstants.ACCESS_PROPERTIES_FILENAME);
            configHelper = new ConfigHelper(accessProperties);
        } else {
            //try checking the existence of file in the resources
            try {
                accessProperties = new File(Resources.getResource(SampleConstants.ACCESS_PROPERTIES_FILENAME).getFile());
                configHelper = new ConfigHelper(accessProperties);
            } catch (Exception e) {
                throw new FileNotFoundException("Cannot find AWS_SWF_SAMPLES_CONFIG environment variable, Exiting!!!");
            }
        }

        return configHelper;
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
