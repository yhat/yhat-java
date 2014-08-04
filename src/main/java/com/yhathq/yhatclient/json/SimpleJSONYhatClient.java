/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.yhathq.yhatclient.json;

import com.yhathq.yhatclient.YhatClient;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 *
 * @author eric
 */
public class SimpleJSONYhatClient extends YhatClient{
    
    private final JSONParser parser = new JSONParser();
    
    /**
     * 
     * @param username
     * @param apikey
     * @param hostname
     * @throws Exception 
     */
    public SimpleJSONYhatClient(String username, String apikey, String hostname) throws Exception {
        super(username, apikey, hostname);
    }

    /**
     * 
     * @param username
     * @param apikey
     * @param hostname
     * @param protocol
     * @throws Exception 
     */
    public SimpleJSONYhatClient(String username, String apikey, String hostname, Protocol protocol) throws Exception {
        super(username, apikey, hostname, protocol);
    }
    
    /**
     * 
     * @param data
     * @param modelname
     * @return
     * @throws Exception 
     */
    public JSONObject predict(JSONArray data, String modelname) throws Exception {
        String result = this.predictRaw(data.toJSONString(), modelname);
        Object obj = parser.parse(result);
        if (!(obj instanceof JSONObject)) {
            throw new Exception("Could not parse incoming json");
        }
        return (JSONObject) obj;
    }
    
    /**
     * 
     */
    public JSONObject predict(JSONObject data, String modelname) throws Exception {
        String result = this.predictRaw(data.toJSONString(), modelname);
        Object obj = parser.parse(result);
        if (!(obj instanceof JSONObject)) {
            throw new Exception("Could not parse incoming json");
        }
        return (JSONObject) obj;
    }
}
