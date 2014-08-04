/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.yhathq.yhatclient;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author eric
 */
public class YhatClientTest {
    
    final static String username = System.getenv("USERNAME");
    final static String apikey = System.getenv("APIKEY");
    final static String modelname = System.getenv("MODELNAME");
    final static String hostname = "http://cloud.yhathq.com/";
    YhatClient yhat = null;
    
    public YhatClientTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
        String err = "Please set the USERNAME, APIKEY, and MODELNAME env vars";
        assert username != null : err;
        assert apikey != null : err;
        assert modelname != null : err;
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Test
    @Before
    public void setUp() throws Exception {
        yhat = new YhatClient(username, apikey, hostname);
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of PredictRaw method, of class YhatClient.
     */
    @Test
    public void testPredictRaw() throws Exception {
        System.out.println("PredictRaw");
        String jsonString = "{\"name\": \"Eric\"}";
        String result = yhat.predictRaw(jsonString, modelname);
        System.out.printf("Result: %s\n", result);
    }

    /**
     * Test of getApikey method, of class YhatClient.
     */
    @Test
    public void testGetApikey() {
        System.out.println("getApikey");
        String expResult = apikey;
        String result = yhat.getApikey();
        assertEquals(expResult, result);
    }

    /**
     * Test of getProtocol method, of class YhatClient.
     */
    @Test
    public void testGetProtocol() {
        System.out.println("getProtocol");
        YhatClient.Protocol expResult = YhatClient.Protocol.HTTP;
        YhatClient.Protocol result = yhat.getProtocol();
        assertEquals(expResult, result);

    }

    /**
     * Test of getUsername method, of class YhatClient.
     */
    @Test
    public void testGetUsername() {
        System.out.println("getUsername");
        String expResult = username;
        String result = yhat.getUsername();
        assertEquals(expResult, result);
    }
}
