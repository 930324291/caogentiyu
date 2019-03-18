/**
 * ID: SmartCallback.java
 * Copyright (c) 2002-2014 Luther Inc.
 * http://zuv.cc
 * All rights reserved.
 */
package com.football.net.http;


import com.football.net.http.reponse.Result;

/**
 * File Description
 *
 * @author			Kama Luther
 * @version			0.1
 *
 * <DT><B>修改历史记录</B>
 * <DD>
 * 
 * </DD>
 * </DT>
 */
public interface SmartCallback<T extends Result>
{

	/**
	 * @param statusCode
	 * @param result
	 */
	void onSuccess(int statusCode, T result);

	/**
	 * @param statusCode
	 * @param message
	 */
	void onFailure(int statusCode, String message);
	
}
