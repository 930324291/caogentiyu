/**
 * ID: SmartFileCallback.java
 * Copyright (c) 2002-2014 Luther Inc.
 * http://zuv.cc
 * All rights reserved.
 */
package com.football.net.http;

import java.io.File;

/**
 * File Description
 *
 * @author			Kama Luther
 * @version			0.1
 * @since           0.1
 * <DT><B>修改历史记录</B>
 * <DD>
 * 
 * </DD>
 * </DT>
 */
public interface SmartFileCallback
{

	/**
	 * @param result
	 */
	void onSuccess(File result);

	/**
	 * @param message
	 */
	void onFailure(String message);
	
	/**
	 * @param total
	 */
	void onLength(long total);
	
	/**
	 * @param size
	 */
	void onProgress(long size);
	

}
