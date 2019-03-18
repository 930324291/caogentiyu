/**
 * ID: SmartHeadCallback.java
 * Copyright (c) 2002-2014 Luther Inc.
 * http://zuv.cc
 * All rights reserved.
 */
package com.football.net.http;

import org.apache.http.Header;


public interface SmartHeadCallback
{

	/**
	 * @param statusCode
	 * @param headers
	 * @param throwablemsg
	 */
	void onFailure(int statusCode, Header[] headers, String throwablemsg);

	/**
	 * @param statusCode
	 * @param headers
	 */
	void onSuccess(int statusCode, Header[] headers);

}
