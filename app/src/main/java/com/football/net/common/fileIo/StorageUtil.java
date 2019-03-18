/**
 * ID: StorageUtil.java
 * Copyright (c) 2002-2014 Luther Inc.
 * http://zuv.cc
 * All rights reserved.
 */
package com.football.net.common.fileIo;

import android.os.Environment;
import android.os.StatFs;

import java.io.File;

/**
 * File Description
 *
 * @author			Kama Luther
 * @version			0.1
 * @since           0.1
 * @.createdate     2014年12月31日 下午1:52:30
 * @.modifydate     2014年12月31日 下午1:52:30
 * <DT><B>修改历史记录</B>
 * <DD>
 * 
 * </DD>
 * </DT>
 */
public class StorageUtil
{
	
	//--------------------------------------------------------------------
	
	public static boolean isExtExist()
	{
		return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
	}
	
	public static String getAvaRoot()
	{
		String folder = isExtExist()?getExtRoot():getIntRoot();
		return FileIoUtil.makeFolder(folder).getPath();
	}
	public static String getExtRoot()
	{
		return isExtExist()? Environment.getExternalStorageDirectory().getAbsolutePath():null;
	}
	public static String getIntRoot()
	{
		return Environment.getDataDirectory().getAbsolutePath();
	}
	
	//--------------------------------------------------------------------
	
	public static long getExtAvaliableSize()
	{
		return isExtExist()?getAvailableSize(getExtRoot()):-1;
	}
	public static long getExtTotalSize()
	{
		return isExtExist()?getTotalSize(getExtRoot()):-1;
	}
	public static long getIntAvaliableSize()
	{
		return getAvailableSize(getIntRoot());
	}
	public static long getIntTotalSize()
	{
		return getTotalSize(getIntRoot());
	}
	
	//--------------------------------------------------------------------
	
	public static long getAvailableSize(String path)
	{
		if(new File(path).exists())
		{
			StatFs stat = new StatFs(path);
            long blockSize = stat.getBlockSize();  
            long availableBlocks = stat.getAvailableBlocks();  
            return availableBlocks*blockSize; 
		}
		return -1;
	}
	public static long getTotalSize(String path)
	{
		if(new File(path).exists())
		{
	        StatFs stat = new StatFs(path);
	        long blockSize = stat.getBlockSize();  
	        long totalBlocks = stat.getBlockCount();  
	        return totalBlocks*blockSize; 
		}
		return -1;
	}
	
	//--------------------------------------------------------------------

}
