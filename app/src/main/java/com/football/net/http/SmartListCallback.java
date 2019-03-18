/**
 * ID: SmartCallback.java
 * Copyright (c) 2002-2014 Luther Inc.
 * http://zuv.cc
 * All rights reserved.
 */
package com.football.net.http;


import com.football.net.http.reponse.Result;

import java.util.ArrayList;

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
public interface SmartListCallback<T>
{

	/**
	 * @param statusCode
	 * @param result
	 */
	void onSuccess(int statusCode, ArrayList<T> result);

	/**
	 * @param statusCode
	 * @param message
	 */
	void onFailure(int statusCode, String message);
	
}
