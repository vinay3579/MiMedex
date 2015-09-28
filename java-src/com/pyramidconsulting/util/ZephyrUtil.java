/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pyramidconsulting.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import sun.misc.BASE64Encoder;


/**
 *
 * @author durgeshv
 */
/**
 * Helper class for calling ZAPI
 */
public class ZephyrUtil {

    /**
     * Status IDs
     */

	private static String credentials;
    public enum Status {

        PASS(1), FAIL(2), WIP(3), BLOCKED(4), UNEXECUTED(-1);
        private final int value;

        private Status(final int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }
    /**
     * URLS
     */
    private static final String BASE_URL = "http://jira";
    private static final String ZAPI_URL = BASE_URL + "/rest/zapi/latest/";
    
    // JIRA Rest URL
    private static final String JIRAREST_URL_ISSUE = BASE_URL + "/rest/api/2/issue/";
    
    /**
     * HTTP Proxy details
     */
    private static final boolean USE_PROXY = false;
    private static final String PROXY_IP = "xxx.xxx.xxx.xxx";
    private static final int PROXY_PORT = 8080;
    private static final HttpHost HTTP_HOST_PROXY = new HttpHost(PROXY_IP, PROXY_PORT);
    private static final Proxy PROXY = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(PROXY_IP,
            PROXY_PORT));
    private static Map<String,String> executionMap = new HashMap<String, String>();
    private static Map<Integer,String> testMap = new HashMap<Integer, String>();
    private static List<String> list = new ArrayList<String>();
    
    
   
    /**
     * JIRA credentials: format "username:password" or "" for none.
     */
//    private static final String CREDENTIALS = "akhare:Pyramid#123";
   
	// ================================================================================
    // ZAPI methods
    // ================================================================================
    /**
     * Gets the versionID for the project.
     *
     * @param versionName
     * @param projectId
     * @throws IOException, JSONException
     * @return the ID for the specified Version in the specified Project
     */
    public static String getVersionID(final String versionName, final String projectId)
            throws IOException, JSONException {
        // Get list of versions on the specified project
        final JSONObject projectJsonObj =
                httpGetJSONObject(ZAPI_URL + "util/versionBoard-list?projectId=" + projectId);
        if (null == projectJsonObj) {
            throw new IllegalStateException("JSONObject is null for projectId=" + projectId);
        }
        if(projectJsonObj.get("unreleasedVersions") != null) {
	        final JSONArray versionOptions = (JSONArray) projectJsonObj.get("unreleasedVersions");
	
	        // Iterate over versions
	        for (int i = 0; i < versionOptions.length(); i++) {
	
	            final JSONObject obj2 = versionOptions.getJSONObject(i);
	            // If label matches specified version name
	            if (obj2.getString("label").equals(versionName)) {
	                // Return the ID for this version
	                return obj2.getString("value");
	            }
	
	        }
        }
        throw new IllegalStateException("Version ID not found for versionName=" + versionName);
    }

    /**
     * Updates the specified test execution
     *
     * @param executionId the ID of the execution
     * @param status a ZAPI.Status value
     * @param comment a comment for the test execution
     * @throws IOException, JSONException updateTestExecution may throw IOException, JSONException
     */
    public static JSONObject updateTestExecution(final String executionId, final String status, final String comment) throws IOException, JSONException {
        // Construct JSON object
        final JSONObject obj = new JSONObject();
        obj.put("status", String.valueOf(Status.valueOf(status).getValue()));
        obj.put("comment", comment);

        return put(ZAPI_URL + "execution/" + executionId + "/execute", obj);
    }

    /**
     * Get latest cycle the specified test execution
     *
     * @param executionId the ID of the execution
     * @param status a ZAPI.Status value
     * @param comment a comment for the test execution
     * @throws IOException, JSONException updateTestExecution may throw IOException, JSONException
     */
    public static JSONObject getLatestCycle(final Integer projectId, final String versionId) throws IOException, JSONException {
        // Construct JSON object
        final JSONObject obj = new JSONObject();
        obj.put("projectId", projectId);
        obj.put("versionId", versionId);
        obj.put("offset", 0);
        obj.put("expand", "executionSummaries");

        return get(ZAPI_URL + "cycle", obj);
    }
    
    
    /**
     * Get latest cycle the specified test execution
     *
     * @param executionId the ID of the execution
     * @param status a ZAPI.Status value
     * @param comment a comment for the test execution
     * @throws IOException, JSONException updateTestExecution may throw IOException, JSONException
     */
    public static JSONObject getExecutions(final Integer issueId) throws IOException, JSONException {
        // Construct JSON object
        final JSONObject obj = new JSONObject();
        obj.put("issueId", issueId);

        return get(ZAPI_URL + "execution", obj);
    }
    
    /**
     * Adds attachment to an execution.
     *
     * @param fileToUpload - the file to attach
     * @param executionId
     * @throws RuntimeException
     * @throws IOException, JSONException
     */
    public static void addAttachment(final String fileNames, final String executionId)
            throws RuntimeException, IOException {
        // set up proxy for http client
        final HttpClientBuilder clientBuilder = HttpClientBuilder.create();
        clientBuilder.useSystemProperties();
        if (USE_PROXY) {
            clientBuilder.setProxy(HTTP_HOST_PROXY);
        }
        final CloseableHttpClient httpClient = clientBuilder.build();

        final HttpPost httpPost = new HttpPost(ZAPI_URL + "attachment?entityId=" + executionId + "&entityType=EXECUTION");
        httpPost.setHeader("X-Atlassian-Token", "nocheck");

        if (!getCredentials().isEmpty()) {
            final String encoding = new BASE64Encoder().encode(getCredentials().getBytes());
            httpPost.setHeader("Authorization", "Basic " + encoding);
        }

        final MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        if(fileNames != null && fileNames.length() > 0){
            String [] fileNameArr = fileNames.split(",");
            for (int i = 0; i < fileNameArr.length; i++) {
                File fileToUpload = new File(fileNameArr[i]);
                builder.addBinaryBody("file", fileToUpload, ContentType.APPLICATION_OCTET_STREAM, fileToUpload.getName());
            }
        }        
        final HttpEntity multipart = builder.build();
        httpPost.setEntity(multipart);

        final CloseableHttpResponse response = httpClient.execute(httpPost);
        final HttpEntity responseEntity = response.getEntity();
        if (null != responseEntity) {
            EntityUtils.consume(responseEntity);
        }

        // ensure file was uploaded correctly
        if (response.getStatusLine().getStatusCode() != 200) {
            throw new RuntimeException("Error uploading file");
        }
    }

    /**
     * Deletes all of the attachments on the specified execution
     *
     * @param executionId the id of the execution
     * @throws IOException, JSONException delete may throw IOException, JSONException
     */
    public static List<JSONObject> deleteAttachments(final String executionId) throws IOException, JSONException {
        final ArrayList<String> fileIds = new ArrayList<String>();
        // Note the IDs for the files currently attached to the execution
        final JSONObject obj =
                httpGetJSONObject(ZAPI_URL + "attachment/attachmentsByEntity?entityId="
                + executionId + "&entityType=EXECUTION");
        if (null == obj) {
            throw new IllegalStateException("Response is null");
        }

        final JSONArray data = (JSONArray) obj.get("data");
        for (int i = 0; i < data.length(); i++) {
            final JSONObject fileData = data.getJSONObject(i);
            fileIds.add(fileData.getString("fileId"));
        }

        // Iterate over attachments
        final ArrayList<JSONObject> responses = new ArrayList<JSONObject>(data.length());
        for (final String fileId : fileIds) {
            responses.add(delete(ZAPI_URL + "attachment/" + fileId));
        }
        return responses;
    }    
        
    public static void zqlExecuteSearch(final String zqlQuery, final String findValue)
            throws IOException, JSONException {
        // Get list of versions on the specified project
        final JSONObject jsonObj = httpGetJSONObject(ZAPI_URL + "zql/executeSearch/?zqlQuery=" + zqlQuery);
        if (null == jsonObj) {
            throw new IllegalStateException("JSONObject is null for query request=" + zqlQuery);
        }
        if(jsonObj.get(findValue) != null) {
	        final JSONArray jsonArrayValue = (JSONArray) jsonObj.get(findValue);
	        if(jsonArrayValue != null) {
		     // Iterate over jsonArray
	        	String cycleName="",testCaseName = "",execId="";
		        for (int i = 0; i < jsonArrayValue.length(); i++) {
		            final JSONObject obj2 = jsonArrayValue.getJSONObject(i);
		            // If label matches specified version name
		            if (obj2 != null) {
		                cycleName = obj2.getString("cycleName");
		                testCaseName = obj2.getString("issueKey");
		                execId = obj2.getString("id");
		                getExecutionMap().put(cycleName+"~"+testCaseName, execId);
		        		list.add(testCaseName);
		        		Collections.sort(list);  
		            }
		        }
	        }
	        creatTestMap();	        
        }else{
        	throw new IllegalStateException("JSON Values are not found for " + findValue);
        }
        
    }
    
    public static void creatTestMap(){
    	//System.out.println("For Loop:");
    	for(int i=0;i<list.size();i++)
    	{
    		getTestMap().put(i, list.get(i));
    	}
        /*for (Map.Entry me : testMap.entrySet()) {
          System.out.println("Key: "+me.getKey() + " & Value: " + me.getValue());
          getTestMap().put(me.getKey(), me.getValue());
        }*/	
    }
    
    /**
     * Updates all the test executions
     *   
     * @param status a ZAPI.Status value    
     */
    public static boolean updateAllTestExecutions(final String status){
    	
    	boolean blnStatus = false;
    	
    	for(String executionId:getExecutionMap().values())
	    {    		
	    	//Construct JSON object
	        final JSONObject obj = new JSONObject();
	        try
	        {
	        	obj.put("status", String.valueOf(Status.valueOf(status).getValue()));	        	        
	        	put(ZAPI_URL + "execution/" + executionId + "/execute", obj);
	        }
	        catch(Exception e)
	        {
	        	blnStatus = false;
	        	break;
	        }
	        blnStatus = true;
    	}
    	
		return blnStatus;
    }
    
    /**
     * Adds a Bug with the specified details
     *
     * @param strProjID the Proj ID in which the bug needs to be updated 
     * @param strSummary Bug Summary value
     * @param strDescription Bug description value
     * @throws IOException, JSONException addIssue may throw IOException, JSONException
     */
    public static String addIssue(final String strProjID, final String strSummary, final String strDescription, final String strAssignee) throws IOException, JSONException {
        String strKey;
    	// Construct JSON object
        final JSONObject obj = new JSONObject();        
        obj.put("key", strProjID);
        final JSONObject obj1 = new JSONObject();
        obj1.put("project", obj);
        obj1.put("description", strDescription);
        obj1.put("summary", strSummary);
        if(!strAssignee.trim().isEmpty())
        {
        	final JSONObject objtemp = new JSONObject();
        	objtemp.put("name", strAssignee);
        	obj1.put("assignee", objtemp);
        }
        final JSONObject obj2 = new JSONObject();
        obj2.put("name", "Bug");
        obj1.put("issuetype", obj2);    
        final JSONObject obj3 = new JSONObject();
        obj3.put("fields", obj1);
        
        final JSONObject obj4 = post(JIRAREST_URL_ISSUE,obj3);
        strKey = obj4.get("key").toString();      
        
        return strKey;
    }	
	
	/**
     * Links the specified bug with the test execution
     *
     * @param strExecId the Exec ID in which the bug needs to be updated 
     * @param strDefectId the Defect Id of the created Bug      
     * @throws IOException, JSONException addIssue may throw IOException, JSONException
     */    
    public static JSONObject linkDefect(String strExecId, String strDefectId) throws IOException, JSONException {

    	// Construct JSON object
        final JSONObject obj = new JSONObject();      
        String[] executionIds = {strExecId} ;      
        String[] defectsIds = {strDefectId} ;
        obj.put("executions", executionIds);
        obj.put("defects", defectsIds);
        obj.put("detailedResponse", false);
        
        return put(ZAPI_URL + "execution/updateWithBulkDefects" , obj);
    }   	
	
	 /**
     * Adds attachment to a Bug.
     *
     * @param fileNames - the file to attach
     * @param bugId - The Bug Id to attach
     * @throws RuntimeException
     * @throws IOException, JSONException
     */
    public static void addAttachmenttoBug(final String fileNames, final String bugId)
            throws RuntimeException, IOException {
        // set up proxy for http client
        final HttpClientBuilder clientBuilder = HttpClientBuilder.create();
        clientBuilder.useSystemProperties();
        if (USE_PROXY) {
            clientBuilder.setProxy(HTTP_HOST_PROXY);
        }
        final CloseableHttpClient httpClient = clientBuilder.build();

        final HttpPost httpPost = new HttpPost(JIRAREST_URL_ISSUE + bugId + "/attachments");
        httpPost.setHeader("X-Atlassian-Token", "nocheck");

        if (!getCredentials().isEmpty()) {
            final String encoding = new BASE64Encoder().encode(getCredentials().getBytes());
            httpPost.setHeader("Authorization", "Basic " + encoding);
        }

        final MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        if(fileNames != null && fileNames.length() > 0){
            String [] fileNameArr = fileNames.split(",");
            for (int i = 0; i < fileNameArr.length; i++) {
                File fileToUpload = new File(fileNameArr[i]);
                builder.addBinaryBody("file", fileToUpload, ContentType.APPLICATION_OCTET_STREAM, fileToUpload.getName());
            }
        }        
        final HttpEntity multipart = builder.build();
        httpPost.setEntity(multipart);

        final CloseableHttpResponse response = httpClient.execute(httpPost);
        final HttpEntity responseEntity = response.getEntity();
        if (null != responseEntity) {
            EntityUtils.consume(responseEntity);
        }

        // ensure file was uploaded correctly
        if (response.getStatusLine().getStatusCode() != 200) {
            throw new RuntimeException("Error uploading file");
        }
    }
	
	
	/**
     * Gets the test Summary for the test.
     *
     * @param strExecId The Execution Id of the script 
     * @throws IOException, JSONException
     * @return the Test Summary of the test case
     */
    public static String getTestSummary(final String strExecId)
            throws IOException, JSONException {
        // Get list of versions on the specified project
        final JSONObject JsonObj1 =
                httpGetJSONObject(ZAPI_URL + "execution/" + strExecId);
        String strTestSumary; 
        
        if (JsonObj1 != null) {           
       
	        final JSONObject JsonObj2 = (JSONObject) JsonObj1.get("execution");
	
	        // Get the Test Summary
	        strTestSumary = (String) JsonObj2.get("summary");        
	
	        }
        else        
        	throw new IllegalStateException("JSONObject is null for exec Id=" + strExecId);
        
		return strTestSumary;
    }	
    
    
    // ================================================================================
    // HTTP request methods
    // ================================================================================
    /**
     * Send GET request to the specified URL
     *
     * @param url
     * @throws IOException, JSONException
     */
    private static JSONObject httpGetJSONObject(final String url) throws IOException, JSONException {
        return new JSONObject(httpGetJSONString(url));
    }

    /**
     * Send GET request to the specified URL
     *
     * @param url
     * @throws IOException, JSONException
     */
    private static JSONArray httpGetJSONArray(final String url) throws IOException, JSONException {
        return new JSONArray(httpGetJSONString(url));
    }

    /**
     * Get a string from a url.
     *
     * @param url the URL to perform the GET method on
     * @return a String representing the body of the http response
     * @throws IOException, JSONException
     */
    private static String httpGetJSONString(final String url) throws IOException {
        final HttpURLConnection httpCon = createHttpCon(url, "GET");
        final BufferedReader br =
                new BufferedReader(new InputStreamReader(httpCon.getInputStream()));

        final StringBuffer httpResponse = new StringBuffer();
        String line = "";
        while (null != (line = br.readLine())) {
            httpResponse.append(line);
        }

        return httpResponse.toString();
    }

    /**
     * Send a request with JSON content with the specified method
     *
     * @param url - the URL to send the request to
     * @param obj - the JSON content to send
     * @param method - e.g. PUT
     * @throws IOException, JSONException
     */
    private static JSONObject sendRequest(final String url, final JSONObject obj,
            final String method) throws IOException, JSONException {
        final HttpURLConnection httpCon = createHttpCon(url, method);

        if (null != obj) {
            final OutputStreamWriter out = new OutputStreamWriter(httpCon.getOutputStream());
            out.write(obj.toString());
            out.close();
        }

        final BufferedReader rd = new BufferedReader(new InputStreamReader(httpCon.getInputStream()));
        final StringBuffer result = new StringBuffer();
        String line = "";
        while (null != (line = rd.readLine())) {
            result.append(line);
        }
        return new JSONObject(result.toString());
    }

    /**
     * Send GET request to the specified URL
     *
     * @param url - the URL to send the request to     
     * @throws IOException, JSONException
     */
    private static JSONObject get(final String url) throws IOException, JSONException {
        return sendRequest(url, null, "GET");
    }
    
    /**
     * Send GET request to the specified URL
     *
     * @param url - the URL to send the request to
     * @param obj - the JSON content to send
     * @throws IOException, JSONException
     */
    private static JSONObject get(final String url, final JSONObject obj) throws IOException, JSONException {
        return sendRequest(url, obj, "GET");
    }
    
    /**
     * Send PUT request to the specified URL
     *
     * @param url - the URL to send the request to
     * @param obj - the JSON content to send
     * @throws IOException, JSONException
     */
    private static JSONObject put(final String url, final JSONObject obj) throws IOException, JSONException {
        return sendRequest(url, obj, "PUT");
    }

    /**
     * Send POST request to the specified URL
     *
     * @param url - the URL to send the request to
     * @param obj - the JSON content to send
     * @throws IOException, JSONException
     */
    private static JSONObject post(final String url, final JSONObject obj) throws IOException, JSONException {
        return sendRequest(url, obj, "POST");
    }

    /**
     * Send DELETE request to the specified URL
     *
     * @param url - the URL to send the request to
     * @throws IOException, JSONException
     */
    private static JSONObject delete(final String url) throws IOException, JSONException {
        return sendRequest(url, null, "DELETE");
    }

    /**
     * Return a HttpURLConnection object for the specified URL and request
     * method
     *
     * @param url the URL to connect to
     * @param method - e.g. GET
     */
    private static HttpURLConnection createHttpCon(final String url, final String method) throws IOException {
        final HttpURLConnection httpCon;
        if (USE_PROXY) {
            httpCon = (HttpURLConnection) new URL(url).openConnection(PROXY);
        } else {
            httpCon = (HttpURLConnection) new URL(url).openConnection();
        }

        httpCon.setDoOutput(true);
        httpCon.setRequestMethod(method);

        if (!getCredentials().isEmpty()) {
        	final String encoding = new BASE64Encoder().encode(getCredentials().getBytes());
            httpCon.setRequestProperty("Authorization", "Basic " + encoding);
        }

        httpCon.setRequestProperty("Content-type", "application/json");

        return httpCon;
    }

	public static String getCredentials() {
		return credentials;
	}

	public static void setCredentials(String credentials) {
		ZephyrUtil.credentials = credentials;
	}

	public static Map<String, String> getExecutionMap() {
		return executionMap;
	}	
	
	public static Map<Integer, String> getTestMap() {
		return testMap;
	}
}
