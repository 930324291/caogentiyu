/**
 * ID: PropParser.java
 * Copyright (c) 2002-2014 Luther Inc.
 * http://zuv.cc
 * All rights reserved.
 */
package com.football.net.common.fileIo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * File Description
 *
 * @author			Kama Luther
 * @version			0.1
 * @since           0.1
 * @.createdate     2015年1月3日 下午8:35:22
 * @.modifydate     2015年1月3日 下午8:35:22
 * <DT><B>修改历史记录</B>
 * <DD>
 * 
 * </DD>
 * </DT>
 */
public class PropParser
{
	
	private static final Logger logger = LoggerFactory.getLogger(PropParser.class);
	
	//-------------------------------------------------------------------------------
	
	private Properties props;
	
	public PropParser()
	{
		props = new Properties();
	}
	
	public boolean parse(InputStream in)
	{
        try
        {
            props.load(in);
            return true;
        } 
        catch (IOException e)
        {
        	logger.error("load properties file failure", e);
        	return false;
        }
	}
	
	public String getValue(String name)
	{
		return props.getProperty(name);
	}
	public String getValue(String name, String value)
	{
		return props.getProperty(name, value);
	}
	
	//-------------------------------------------------------------------------------

}
