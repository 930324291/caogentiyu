/**
 * ID: SmartParams.java
 * Copyright (c) 2002-2014 Luther Inc.
 * http://zuv.cc
 * All rights reserved.
 */
package com.football.net.http;

import com.google.gson.Gson;
import com.loopj.android.http.RequestParams;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * File Description
 *
 * @author			Kama Luther
 * @version			0.1
 * @since           0.1
 * @.createdate     2015�?�?�?下午3:14:35
 * @.modifydate     2015�?�?�?下午3:14:35
 * <DT><B>修改历史记录</B>
 * <DD>
 * 
 * </DD>
 * </DT>
 */
public class SmartParams extends RequestParams
{

	private static final long serialVersionUID = 6740236382568203098L;
	
	
	public SmartParams with(String key, String value)
	{
		put(key, value);
		return this;
	}
	
	public SmartParams with(String key, File value) throws FileNotFoundException
	{
		put(key, value);
		return this;
	}
	
	public boolean contain(String key)
	{
		return urlParams.containsKey(key);
	}
	
	public String get(String key)
	{
		return urlParams.get(key);
	}
	
    public String value()
    {
        StringBuilder result = new StringBuilder();
        for (ConcurrentHashMap.Entry<String, String> entry : urlParams.entrySet())
        {
            if (result.length() > 0)
                result.append("&");

            result.append(entry.getKey());
            result.append("=");
            result.append(entry.getValue());  //TODO URLEncoder.encode(value, "UTF-8")
        }
        return result.toString();
    }
    
    public String json()
    {
    	Map<String, Object> map = new ConcurrentHashMap<String, Object>();
    	map.putAll(urlParams);
    	map.putAll(urlParamsWithObjects);
    	
    	Gson gson = new Gson();
    	return gson.toJson(map);
    }
    
    

}
