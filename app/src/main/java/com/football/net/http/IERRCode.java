/**
 * ID: IERRCode.java
 * Copyright (c) 2002-2013 Luther Inc.
 * http://xluther.com
 * All rights reserved.
 */
package com.football.net.http;

/**
 * File Description
 *
 * @author			Kama Luther
 * @version			0.1
 * @since           0.1
 * @.createdate     2014-2-11 下午02:19:48
 * @.modifydate     2014-2-11 下午02:19:48
 * <DT><B>修改历史记录</B>
 * <DD>
 * 
 * </DD>
 * </DT>
 */
public interface IERRCode
{
	
	/***************************************************************/
	/**  错误代码定义                                      
	/***************************************************************/
	
	public static final byte FALSE 	= 0x0;
	public static final byte TRUE 	= 0x1;
	
	public static final int ERRCODE_FAILURE = 0;
	public static final int ERRCODE_SUCCESS = 1;
	
	// 0x1000段位用户管理段
	public static final int ERRCODE_USER_ACT_INVALID  	= 0x1000; //非法操作
	public static final int ERRCODE_USER_ACT_FORBID   	= 0x1001; //限制權限操作
	public static final int ERRCODE_USER_NOT_EXISTS   	= 0x1010; //用户不存在
	public static final int ERRCODE_USER_BAD_AUTHEN   	= 0x1011; //密码错误
	public static final int ERRCODE_USER_HAS_LOCKED   	= 0x1012; //用户已锁定
	public static final int ERRCODE_USER_HAS_LOGINED  	= 0x1013; //用户已登陆
	
	// 0x1100段位节点管理段
	public static final int ERRCODE_NODE_ACT_INVALID  	= 0x1100; //非法操作
	public static final int ERRCODE_NODE_NOT_EXISTS   	= 0x1101; //节点不存在
	public static final int ERRCODE_NODE_HAS_HOLDED   	= 0x1102; //节点已控制
	
	// 0x1200段位应用管理段
	public static final int ERRCODE_APP_ACT_INVALID  	= 0x1200; //非法操作
	public static final int ERRCODE_APP_BAD_AUTHEN  	= 0x1201; //密码错误

}
