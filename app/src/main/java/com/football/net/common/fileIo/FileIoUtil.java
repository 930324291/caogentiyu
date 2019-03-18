/**
 * ID: FileIoUtil.java
 * Copyright (c) 2002-2014 Luther Inc.
 * http://zuv.cc
 * All rights reserved.
 */
package com.football.net.common.fileIo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * File Description
 *
 * @author			Kama Luther
 * @version			0.1
 * @since           0.1
 * @.createdate     2014年12月31日 下午1:56:36
 * @.modifydate     2014年12月31日 下午1:56:36
 * <DT><B>修改历史记录</B>
 * <DD>
 * 
 * </DD>
 * </DT>
 */
public class FileIoUtil
{
	
	private static final Logger logger = LoggerFactory.getLogger(FileIoUtil.class);
	
	//---------------------------------------------------------------
	
	public static byte[] readFile(String filePath) throws IOException
	{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		File file = new File(filePath);
		FileInputStream fis = new FileInputStream(file);
		byte[] buffer = new byte[1024];
		int len = 0;
		while ((len = fis.read(buffer)) != -1)
		{
			baos.write(buffer, 0, len);
		}
		byte[] content = baos.toByteArray();
		fis.close();
		baos.close();
		return content;
	}
	
	public static boolean writeFile(byte[] data, String path)
	{
		FileOutputStream fos;
		try
		{
			fos = new FileOutputStream(path);
			fos.write(data);
			fos.flush();
			fos.close();
			fos = null;
			return true;
		}
		catch (FileNotFoundException e)
		{
			logger.error("路径错误", e);
			return false;
		}
		catch (IOException e)
		{
			logger.error("读写错误", e);
			return false;
		}
	}

	
	public static boolean writeBmp(Bitmap bm, int quality, String filePath)
	{
		BufferedOutputStream bos = null;
		try
		{
			bos = new BufferedOutputStream(new FileOutputStream(filePath));
			// Bitmap scbm = bm.createScaledBitmap(bm, 600, 800, false);
			bm.compress(Bitmap.CompressFormat.JPEG, quality, bos);
			bos.flush();
			return true;
		}
		catch (IOException e)
		{
			logger.error("保存照片错误", e);
			return false;
		}
		finally
		{
			if (bos != null)
			{
				try
				{
					bos.close();
				}
				catch (IOException e)
				{
				}
			}
		}
	}
	
	public static Bitmap readBmp(String filePath)
	{
		if (!new File(filePath).exists())
			return null;

		Bitmap bitmap = null;
		FileInputStream fis = null;
		try
		{
			fis = new FileInputStream(filePath);
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = false;
			options.inSampleSize = 1;
			bitmap = BitmapFactory.decodeStream(fis, null, options);
		}
		catch (IOException e)
		{
			logger.error("读取照片错误", e);
			bitmap = null;
		}
		finally
		{
			if (fis != null)
			{
				try
				{
					fis.close();
				}
				catch (IOException e)
				{
				}
			}
		}
		return bitmap;
	}
	
	public static File makeFolder(String folder)
	{
		File file = new File(folder);
		if(!file.exists())
		{
			file.mkdirs();
		}
		return file;
	}
	
	public static boolean existFile(String file)
	{
		return new File(file).exists();
	}

}
