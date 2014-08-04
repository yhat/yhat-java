/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.yhathq.yhatclient;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;


/**
 *
 * @author eric
 */
public class YhatClient {

    /**
     * 
     */
    public enum Protocol {

        HTTP("http"),
        HTTPS("https");

        private final String text;

        private Protocol(final String text) {
            this.text = text;
        }

        @Override
        public String toString() {
            return text;
        }
    }
    private final String username;
    private final String apikey;
    private final String hostname;

    private final Protocol protocol;

    /**
     * Construct and instance of the Yhat client.
     * @param username Your ScienceOps username
     * @param apikey Your ScienceOps apikey
     * @param hostname The host name of your ScienceOps server 
     * @throws Exception 
     */
    public YhatClient(String username, String apikey, String hostname)
            throws Exception {
        this(username, apikey, hostname, Protocol.HTTP);
    }

    /**
     * Construct and instance of the Yhat client.
     * @param username Your ScienceOps username
     * @param apikey Your ScienceOps apikey
     * @param hostname The host name of your ScienceOps server 
     * @param protocol Protocol to send requests with.
     * @throws Exception 
     */
    public YhatClient(String username, String apikey, String hostname,
            Protocol protocol) throws Exception {
        assert username != null : "Username cannot be null";
        assert apikey != null : "Apikey cannot be null";
        assert hostname != null : "Hostname cannot be null";
        this.username = username;
        this.apikey = apikey;
        this.hostname = stripHostname(hostname, protocol);
        this.protocol = protocol;
        authenticate();
    }
    
    /**
     * Invoke a Yhat model.
     * Takes and returns raw strings rather than JSON objects.
     * @param jsonString String escaped JSON data to send to model.
     * @param modelname Name of model to query.
     * @return
     * @throws Exception 
     */
    public String predictRaw(String jsonString, String modelname)
            throws Exception {
        return predictRaw(jsonString, modelname, username);
    }
    
    /**
     * Invoke a shared Yhat model.
     * Takes and returns raw strings rather than JSON objects.
     * @param jsonString String escaped JSON data to send to model.
     * @param modelname Name of model to query.
     * @param modelowner Owner of the shared model.
     * @return
     * @throws Exception 
     */
    public String predictRaw(String jsonString, String modelname,
            String modelowner) throws Exception {
        String url = String.format("%s://%s/%s/models/%s/",
                protocol, hostname, modelowner, modelname);
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpPost postRequest = new HttpPost(url);
        String userCredentials = username + ":" + apikey;
        String basicAuth = "Basic " + new String(Base64.encodeBase64(
                userCredentials.getBytes()));
        postRequest.addHeader("Authorization", basicAuth);
        postRequest.addHeader("Content-Type", "application/json");
        // Set model query data as body of request
        HttpEntity entity = new ByteArrayEntity(
                jsonString.getBytes("UTF-8"));
        postRequest.setEntity(entity);

        HttpResponse response = httpclient.execute(postRequest);
        int statusCode = response.getStatusLine().getStatusCode();
        if (statusCode != HttpStatus.SC_OK) {
            throw new Exception(String.format(
                    "Response had bad status code: %d", statusCode));
        }
        String result = EntityUtils.toString(response.getEntity());
        try {
            // try to parse incoming data
            (new JSONParser()).parse(result);
        } catch (Exception e) {
            throw new Exception(String.format(
                    "Response from '%s' returned invalid JSON", url));
        }
        httpclient.close();
        return result;
    }
    
    /**
     * Check username and apikey validity.
     * @throws Exception 
     */
    private void authenticate() throws Exception {
        String url = String.format("%s://%s/verify?username=%s&apikey=%s",
                protocol, hostname, username, apikey);
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpPost postRequest = new HttpPost(url);
        postRequest.addHeader("Content-Type", "application/json");
        HttpResponse response = httpclient.execute(postRequest);
        int statusCode = response.getStatusLine().getStatusCode();
        if (statusCode != HttpStatus.SC_OK) {
            throw new Exception(String.format(
                    "Response had bad status code: %d", statusCode));
        }
        String result = EntityUtils.toString(response.getEntity());
        JSONObject rsp;
        try {
            // try to parse incoming data
            Object obj = (new JSONParser()).parse(result);
            assert (obj instanceof JSONObject);
            rsp = (JSONObject) obj;
        } catch (Exception e) {
            throw new Exception(String.format(
                    "Response from '%s' returned invalid JSON", url));
        }
        String errmsg = "Invalid username/apikey combination!";
        if (!(rsp.get("success") instanceof String)) {
            throw new Exception(errmsg);
        }
        String success = (String) rsp.get("success");
        if (!success.equals("true")) {
            throw new Exception(errmsg);
        }
    }
    
    /**
     * Remove protocol prefix and trailing slash from a URL.
     * @param hostname
     * @param protocol
     * @return 
     */
    private String stripHostname(String hostname, Protocol protocol) {
        hostname = hostname.replaceAll("^" + protocol + "://", "");
        hostname = hostname.replaceAll("/$", "");
        return hostname;
    }

    /**
     * Get the Yhat apikey of this client.
     * @return 
     */
    public String getApikey() {
        return apikey;
    }

    /**
     * Get the Yhat hostname of this client.
     * @return 
     */
    public String getHostname() {
        return hostname;
    }

    /**
     * Get the Yhat protocol this client uses.
     * @return 
     */
    public Protocol getProtocol() {
        return protocol;
    }

    /**
     * Get the Yhat username of this client.
     * @return 
     */
    public String getUsername() {
        return username;
    }

    /**
     * 
     * @return 
     */
    @Override
    public String toString() {
        return String.format("Yhat API connection to '%s'",
                protocol + "://" + hostname);
    }
}
