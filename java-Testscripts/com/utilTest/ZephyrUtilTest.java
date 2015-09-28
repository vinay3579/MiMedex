package com.utilTest;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import com.pyramidconsulting.runner.Script;
import com.pyramidconsulting.util.ZephyrUtil;

public class ZephyrUtilTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
            //2014.03b
            
            String versionId = ZephyrUtil.getVersionID("2014.03b", "10500");
            System.out.println("Server response .... \n");
            System.out.println(versionId);
            System.out.println("-------------------------- \n\n");
            
            JSONObject updateStatusJSON = ZephyrUtil.updateTestExecution("744", "PASS", "");
            System.out.println("-------------------------- \n\n");
            System.out.println(updateStatusJSON.toString());
            
            
//            ZephyrUtil.addAttachment("E:\\Zephyr\\SBWEB-232_VerifyRecoveredTissueLandingPage_07_24_2014_12_17_30.html", "744");
//            For Ex:- E:\\Workspace\\MiMedx\\Reports\\RecoveredTissue\\SBWEB-232\\SBWEB-232_VerifyRecoveredTissueLandingPage_07_24_2014_17_36_09.html
        } catch (IOException ex) {
            Logger.getLogger(ZephyrUtilTest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JSONException ex) {
            Logger.getLogger(ZephyrUtilTest.class.getName()).log(Level.SEVERE, null, ex);
        }
	}

}
