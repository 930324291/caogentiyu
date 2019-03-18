/**
 * ID: SmartResponseHandler.java
 * Copyright (c) 2002-2014 Luther Inc.
 * http://zuv.cc
 * All rights reserved.
 */
package com.football.net.http;

import com.loopj.android.http.TextHttpResponseHandler;

import org.apache.http.Header;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * File Description
 *
 * @author			Kama Luther
 * @version			0.1
 * @since           0.1
 * @.createdate     2015??????下午5:32:37
 * @.modifydate     2015??????下午5:32:37
 * <DT><B>修改历史记录</B>
 * <DD>
 * 
 * </DD>
 * </DT>
 */
public class SmartResponseHandler<T> extends TextHttpResponseHandler
{
	
	private static final Logger logger = LoggerFactory.getLogger(SmartResponseHandler.class);
	
	//---------------------------------------------------------------
	
	@Override
    public void onStart() 
	{
		logger.info("start");
	}
	
	public void onFinish() 
	{
		logger.info("finish");
	}
	
    @Override
    public void onSuccess(int statusCode, Header[] headers, String responseString)
    {
    	logger.info("success");
    }
    @Override
    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable)
    {
    	logger.info("failure : " + throwable.getMessage());
    }
    
    //---------------------------------------------------------------

}
