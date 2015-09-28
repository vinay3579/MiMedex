package com.pyramidconsulting.report;

public class ReportingConstants {
    public static class TestReport {
	public static  String TEST_CASE_NAME = "__TEST_CASE_NAME__";
	public static  String TEST_ENVIRONMENT = "__TEST_ENVIRONMENT__";
	public static  String PROJECT_NAME = "__PROJECT_NAME__";
	public static  String TEST_HOST_NAME = "__TEST_HOST_NAME__";
	public static  String TEST_BUILD_VERSION = "__TEST_BUILD_VERSION__";
	public static  String TEST_BROWSER = "__TEST_BROWSER__";
	public static  String TEST_APPLICATION_URL = "__TEST_APPLICATION_URL__";
	public static  String TEST_STEP_REPORTS = "__TEST_STEP_REPORTS__";
	public static  String TEST_STEPS_PASS_COUNT = "__TEST_STEPS_PASS_COUNT__";
	public static  String TEST_STEPS_FAIL_COUNT = "__TEST_STEPS_FAIL_COUNT__";
	public static  String TEST_EXECUTION_TIMESTAMP = "__TEST_EXECUTION_TIMESTAMP__";
	public static  String TEST_MACHINE_NAME = "__TEST_MACHINE_NAME__";
	public static  String TEST_RUN_DURATION = "__TEST_RUN_DURATION__";
	public static  String TEST_ITERATION_COUNT = "__TEST_ITERATION_COUNT__";
    }

    public static class TestStepReport {
	public static  String TEST_STEP_NUMBER = "__TEST_STEP_NUMBER__";
	public static  String TEST_STEP_DESCRIPTION = "__TEST_STEP_DESCRIPTION__";
	public static  String TEST_STEP_EXPECTED_RESULT = "__TEST_STEP_EXPECTED_RESULT__";
	public static  String TEST_STEP_ACTUAL_RESULT = "__TEST_STEP_ACTUAL_RESULT__";
	public static  String TEST_STEP_STATUS = "__TEST_STEP_STATUS__";
	public static  String TEST_STEP_STATUS_CLASS = "__TEST_STEP_STATUS_CLASS__";
    }
}
