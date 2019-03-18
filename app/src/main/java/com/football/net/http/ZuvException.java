/**
 * ID: ZuvException.java
 * Copyright (c) 2002-2013 Luther Inc.
 * http://xluther.com
 * All rights reserved.
 */
package com.football.net.http;

import java.io.PrintStream;
import java.io.PrintWriter;

/**
 * 异常类
 *
 * @author			Kama Luther
 * @version			0.1
 * @since           0.1
 * @.createdate     2013-7-19 下午02:30:31
 * @.modifydate     2013-7-19 下午02:30:31
 * <DT><B>修改历史记录</B>
 * <DD>
 * 
 * </DD>
 * </DT>
 */
public class ZuvException extends RuntimeException
{

	private static final long serialVersionUID = -1206585806307452043L;
	
	private String errCode;
	private String subCode;
	
	private Throwable cause;
	
	public ZuvException(String msg)
	{
		super(msg);
	}
	
	public ZuvException(String msg, Throwable cause)
	{
		super(msg);
		this.cause = cause;
	}
	
	public ZuvException(String msg, String errcode, String subcode)
	{
		super(msg);
		this.errCode = errcode;
		this.subCode = subcode;
	}
	
	public ZuvException(String msg, String errcode, String subcode, Throwable cause)
	{
		super(msg);
		this.errCode = errcode;
		this.subCode = subcode;
		this.cause = cause;
	}
	
	public String getErrCode()
	{
		return errCode;
	}
	
	public String getSubCode()
	{
		return subCode;
	}
	
	public Throwable getCause()
	{
		return this.cause;
	}
	
	public String getMessage()
	{
		String msg = super.getMessage();
		if (this.cause == null)
		{
			return msg;
		}
		else
		{
			return "\r\tat " + msg + "; "
					+ this.cause.getClass().getName() + ": "
					+ this.cause.getMessage();
		}
	}
	
	public String getCurrentMessage()
	{
		return super.getMessage();
	}
	
	public void printStackTrace(PrintStream ps)
	{
		if (getCause() == null) 
		{
			super.printStackTrace(ps);
		}
		else 
		{
			ps.println(this);
			getCause().printStackTrace(ps);
		}
	}
	
	public void printStackTrace(PrintWriter pw)
	{
		if (getCause() == null)
		{
			super.printStackTrace(pw);
		}
		else
		{
			pw.println(this);
			getCause().printStackTrace(pw);
		}
	}

}
