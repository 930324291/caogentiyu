/**
 * ID: IConfig.java
 * Copyright (c) 2002-2014 Luther Inc.
 * http://zuv.cc
 * All rights reserved.
 */
package com.football.net.common.constant;

public interface IConfig
{
	//------------------------------------------------------------------------------------

	//API错误码定义
	public static final int 	API_ERRCODE_SUCCESS 		= 1;  		//接口调用成功
	public static final String API_ERRMSG_SUCCESS 			= "操作成功";	//接口调用成功
	
	public static final int 	API_ERRCODE_TOKEN_INVALID 	= 1006;  	//Token失效
	public static final String API_ERRMSG_TOKEN_INVALID 	= "会话失效,正在重新登录,请稍后...";  //Token失效
	
	public static final int		ERRCODE_NETWORK_UNAVALLABLE	= 0x5001;
	public static final String ERRMSG_NETWORK_UNAVALLABLE	= "无可用网络,请检查网络设置后再试.";
	
	public static final int		ERRCODE_REQUEST_FAILURE		= 0x5002;
	public static final String ERRMSG_REQUEST_FAILURE		= "网络请求失败,请稍后重试.";
	
	public static final int		ERRCODE_FORMAT_INVALID		= 0x5003;
	public static final String ERRMSG_FORMAT_INVALID		= "JSON格式转换错误.";
	
	public static final int		ERRCODE_ENCODE_INVALID		= 0x5004;
	public static final String ERRMSG_ENCODE_INVALID		= "不支持的字符集.";
	
	public static final int		ERRCODE_REQUEST_TIMEOUT		= 0x5005;
	public static final String ERRMSG_REQUEST_TIMEOUT		= "网络不给力,请稍后再试.";

	public static final int		ERRCODE_REQUEST_FILE_NOTEXIT		= 0x5006;
	public static final String ERRMSG_REQUEST_FILE_NOTEXIT	= "文件不存在";

	public static final int		ERRCODE_REQUEST_FILE_Error		= 0x5007;
	public static final String ERRMSG_REQUEST_FILE_rror	= "文件错误";
	
	//------------------------------------------------------------------------------------
	
	//接口缓存参数定义
	public static final int 	MAX_PAGENUM_4_CACHE = 4;
	public static final String API_PAGENUM_TAG 	= "pagenum";
	
	//------------------------------------------------------------------------------------
	
	//系统目录定义
	public static final String HOME_PATH 	= "/zuv";
	public static final String CRASH_PATH 	= HOME_PATH + "/crash";		//缓存目录
	public static final String CACHE_PATH 	= HOME_PATH + "/cache";		//缓存目录
	public static final String USER_PATH 	= HOME_PATH + "/usr";		//用户目录
	public static final String DB_PATH 		= HOME_PATH + "/db";		//数据库路径
//	public static final String DB_PATH 		= "";		//数据库路径
	public static final String DB_FILE 		= "nake";					//数据库文件
	public static final int DB_VERSION 	= 1;					//数据库版本
	public static final String UPDATE_PATH	= HOME_PATH + "/update";	//升级软件下载路径
	
	
	//服务地址定义
	public static final String SHARE_PREF_FILENAME 		= "router";
	
//	public static final String SHARE_PREF_PARAM_EXCEPTIONFILE = "exceptionfile";
//	public static final String SHARE_PREF_PARAM_EXCEPTIONDONE = "exceptiondone";

	//------------------------------------------------------------------------------------

	//返回参数显示

	
}
