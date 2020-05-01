package common;

import com.amazonaws.services.simpleworkflow.flow.common.FlowConstants;

public final class RegistrationDefaults {
	private static final long SECONDS_IN_MINUTE = 60;
    //private static final long SECONDS_IN_HOUR = 60 * SECONDS_IN_MINUTE;
    //private static final long SECONDS_IN_DAY = 24 * SECONDS_IN_HOUR;

    public static final long ACTIVITY_REGISTRATION_OPTIONS_DEFAULT_TASK_SCHEDULE_TO_START_TIMEOUT_SECONDS = FlowConstants.NONE;
    public static final long ACTIVITY_REGISTRATION_OPTIONS_DEFAULT_TASK_HEARTBEAT_TIMEOUT_SECONDS = FlowConstants.NONE;
    public static final long ACTIVITY_REGISTRATION_OPTIONS_DEFAULT_TASK_SCHEDULE_TO_CLOSE_TIMEOUT_SECONDS = 5 * SECONDS_IN_MINUTE;
    public static final long ACTIVITY_REGISTRATION_OPTIONS_DEFAULT_TASK_START_TO_CLOSE_TIMEOUT_SECONDS =  5 * SECONDS_IN_MINUTE;
    
    public static final long WORKFLOW_REGISTRATION_OPTIONS_DEFAULT_EXECUTION_START_TO_CLOSE_TIMEOUT_SECONDS =  10 * SECONDS_IN_MINUTE;
    public static final long WORKFLOW_REGISTRATION_OPTIONS_DEFAULT_TASK_START_TO_CLOSE_TIMEOUT_SECONDS =  1 * SECONDS_IN_MINUTE;

}
