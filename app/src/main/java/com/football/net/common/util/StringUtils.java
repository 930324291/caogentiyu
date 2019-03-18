package com.football.net.common.util;

import android.app.Activity;
import android.content.Context;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.CharacterStyle;
import android.text.style.ForegroundColorSpan;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *  字符串工具类
 */
public class StringUtils {
	public final static String UTF_8 = "utf-8";

	/** 判断字符串是否有值，如果为null或者是空字符串或者只有空格或者为"null"字符串，则返回true，否则则返回false */
	public static boolean isEmpty(String value) {
		if (value != null && !"".equalsIgnoreCase(value.trim()) && !"null".equalsIgnoreCase(value.trim())) {
			return false;
		} else {
			return true;
		}
	}

	/** 判断多个字符串是否相等，如果其中有一个为空字符串或者null，则返回false，只有全相等才返回true */
	public static boolean isEquals(String... agrs) {
		String last = null;
		for (int i = 0; i < agrs.length; i++) {
			String str = agrs[i];
			if (isEmpty(str)) {
				return false;
			}
			if (last != null && !str.equalsIgnoreCase(last)) {
				return false;
			}
			last = str;
		}
		return true;
	}

	/**
	 * 返回一个高亮spannable
	 * @param content 文本内容
	 * @param color   高亮颜色
	 * @param start   起始位置
	 * @param end     结束位置
	 * @return 高亮spannable
	 */
	public static CharSequence getHighLightText(String content, int color, int start, int end) {
		if (TextUtils.isEmpty(content)) {
			return "";
		}
		start = start >= 0 ? start : 0;
		end = end <= content.length() ? end : content.length();
		SpannableString spannable = new SpannableString(content);
		CharacterStyle span = new ForegroundColorSpan(color);
		spannable.setSpan(span, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		return spannable;
	}

	/**
	 * 获取链接样式的字符串，即字符串下面有下划线
	 * @param resId 文字资源
	 * @return 返回链接样式的字符串
	 */
	public static Spanned getHtmlStyleString(int resId) {
		StringBuilder sb = new StringBuilder();
		sb.append("<a href=\"\"><u><b>").append(UIUtils.getString(resId)).append(" </b></u></a>");
		return Html.fromHtml(sb.toString());
	}

	/** 格式化文件大小，不保留末尾的0 */
	public static String formatFileSize(long len) {
		return formatFileSize(len, false);
	}

	/** 格式化文件大小，保留末尾的0，达到长度一致 */
	public static String formatFileSize(long len, boolean keepZero) {
		String size;
		DecimalFormat formatKeepTwoZero = new DecimalFormat("#.00");
		DecimalFormat formatKeepOneZero = new DecimalFormat("#.0");
		if (len < 1024) {
			size = String.valueOf(len + "B");
		} else if (len < 10 * 1024) {
			// [0, 10KB)，保留两位小数
			size = String.valueOf(len * 100 / 1024 / (float) 100) + "KB";
		} else if (len < 100 * 1024) {
			// [10KB, 100KB)，保留一位小数
			size = String.valueOf(len * 10 / 1024 / (float) 10) + "KB";
		} else if (len < 1024 * 1024) {
			// [100KB, 1MB)，个位四舍五入
			size = String.valueOf(len / 1024) + "KB";
		} else if (len < 10 * 1024 * 1024) {
			// [1MB, 10MB)，保留两位小数
			if (keepZero) {
				size = String.valueOf(formatKeepTwoZero.format(len * 100 / 1024 / 1024 / (float) 100)) + "MB";
			} else {
				size = String.valueOf(len * 100 / 1024 / 1024 / (float) 100) + "MB";
			}
		} else if (len < 100 * 1024 * 1024) {
			// [10MB, 100MB)，保留一位小数
			if (keepZero) {
				size = String.valueOf(formatKeepOneZero.format(len * 10 / 1024 / 1024 / (float) 10)) + "MB";
			} else {
				size = String.valueOf(len * 10 / 1024 / 1024 / (float) 10) + "MB";
			}
		} else if (len < 1024 * 1024 * 1024) {
			// [100MB, 1GB)，个位四舍五入
			size = String.valueOf(len / 1024 / 1024) + "MB";
		} else {
			// [1GB, ...)，保留两位小数
			size = String.valueOf(len * 100 / 1024 / 1024 / 1024 / (float) 100) + "GB";
		}
		return size;
	}

	/**
	 * 将数据流转存对应字符集的字符串
	 * @param stream
	 * @param charSet
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String getString(InputStream stream, String charSet) throws UnsupportedEncodingException {
		StringBuilder sb = new StringBuilder();
		try {
			InputStreamReader reader = new InputStreamReader(stream,charSet);
			BufferedReader buffer = new BufferedReader(reader);
			String cur;
			while ((cur = buffer.readLine()) != null) {
				sb.append(cur);
				sb.append("\r\n");
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			stream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return sb.toString();
	}
	/**
	 * 字符串匹配，正则表达式
	 * @param s
	 * @param pattern
	 * @return
	 */
	public static boolean matcher(String s, String pattern) {
		if (s==null || pattern ==null)
			return false;
		Pattern p = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE
				+ Pattern.UNICODE_CASE);
		Matcher matcher = p.matcher(s);
		if (matcher.find()) {
			return true;
		} else {
			return false;
		}
	}
	/**
	 * 字符串匹配，正则表达式
	 * @param s
	 * @param pattern
	 * @return
	 */
	public static Matcher getMatcher(String s, String pattern) {
		if (s==null || pattern ==null)
			return null;
		Pattern p = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE
				+ Pattern.UNICODE_CASE);
		Matcher matcher = p.matcher(s);
		return matcher;
	}
	/**
	 * 字符串匹配，正则表达式
	 * @param s
	 * @param pattern
	 * @return
	 */
	public static Matcher getMatcher(String s, String pattern , int flags) {
		if (s==null || pattern ==null)
			return null;
		Pattern p = Pattern.compile(pattern, flags);
		Matcher matcher = p.matcher(s);
		return matcher;
	}
	/**
	 * 去掉字符串中的特殊字符
	 * @param src
	 * @return
	 */
	public static String removeXMLTag(String src){
//		String tmp = src.replaceAll(">", "&gt;").replaceAll("<", "&lt;").replaceAll("\"", "&quot;").replaceAll(",","&apos;").replaceAll("&", "&amp;");
		String tmp = src.replaceAll(",","&apos;").replaceAll("&", "&amp;");
		return tmp;
	}
	/**
	 * 将字符串转换成流
	 * @param str
	 * @return
	 */
	public static InputStream getInputStreamFromString(String str){
		str=removeXMLTag(str);
		InputStream is = new ByteArrayInputStream(str.getBytes());
		return is;
	}
	/**
	 * 分割字符串
	 * @param str
	 * @param split
	 * @return
	 */
	public static String[] SplitString(String str, String split){
		return str.split(split);
	}


	/**
	 * 字符串合并方法，返回一个合并后的字符串
	 * @param str
	 * @param args
	 * @return
	 */
	public static String format(String str, Object... args)
	{

		//这里用于验证数据有效性
		if(str==null||"".equals(str))
			return "";
		if(args.length==0)
		{
			return str;
		}

		/*
		 *如果用于生成SQL语句，这里用于在字符串前后加单引号
		for(int i=0;i<args.length;i++)
		{
			String type="java.lang.String";
			if(type.equals(args[i].getClass().getName()))
				args[i]="'"+args[i]+"'";
		}
		*/

		String result=str;

		//这里的作用是只匹配{}里面是数字的子字符串
		java.util.regex.Pattern p = java.util.regex.Pattern.compile("\\{(\\d+)\\}");
		java.util.regex.Matcher m = p.matcher(str);

		while(m.find())
		{
			//获取{}里面的数字作为匹配组的下标取值
			int index= Integer.parseInt(m.group(1));

			//这里得考虑数组越界问题，{1000}也能取到值么？？
			if(index<args.length)
			{

				//替换，以{}数字为下标，在参数数组中取值
				final Object obj = args[index];
				String data = obj==null?"":obj.toString();
				result=result.replace(m.group(),data);
			}
		}
		return result;
	}
	/**
	 * 去掉bom
	 * @param src
	 * @return
	 */
	public static String Utf8BomRemove(String src){
		if (src == null)
			return null;
		byte[] bs = src.getBytes();
		if (bs.length<3)
			return src;
		if (bs[0] == -17 && bs[1] == -69 && bs[2] == -65) {
			return Bytes2String(bs,3,null);
		}
		return src;
	}
	/**
	 * 字节转成字符串
	 * @param b
	 * @param encoding
	 * @return
	 */
	public static String Bytes2String(byte[] b, String encoding){
		String result = null;
		try {
			if (encoding!=null)
				result = new String(b,encoding);
			else
				result = new String(b,"UTF8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return result;
	}

	public static String Bytes2String(byte[] b, int offset, String encoding){
		String result = null;
		byte[] newbytes = new byte[b.length-offset];
		System.arraycopy(b, offset, newbytes, 0, newbytes.length);
		try {
			if (encoding!=null)
				result = new String(newbytes,encoding);
			else
				result = new String(newbytes,"UTF8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return result;
	}
	/**
	 * 将字符串转为字节
	 * @param src
	 * @param encoding
	 * @return
	 */
	public static byte[] String2Bytes(String src, String encoding){
		if (src== null)
			return new byte[0];
		try {
			if (encoding==null)
				return src.getBytes("UTF8");
			else
				return src.getBytes(encoding);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return new byte[0];
		}
	}
	/**
	 * 将字符串转为字节
	 * @param src
	 * @return
	 */
	public static byte[] String2Bytes(String src){
		return String2Bytes(src, null);
	}
	/**
	 * 获取随机字符串
	 * @param length
	 * @return
	 */
	public static String getRandomString(int length) { //length表示生成字符串的长度
		String base = "abcdefghijklmnopqrstuvwxyz0123456789";
		Random random = new Random();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < length; i++) {
			int number = random.nextInt(base.length());
			sb.append(base.charAt(number));
		}
		return sb.toString();
	}
	/**
	 * 获取随机字符串,以字母开头
	 * @param length
	 * @return
	 */
	public static String getCRandomString(int length) { //length表示生成字符串的长度
		String base = "abcdefghijklmnopqrstuvwxyz";
		Random random = new Random();
		int number = random.nextInt(base.length());
		StringBuffer sb = new StringBuffer();
		sb.append(base.charAt(number));
		sb.append(getRandomString(length-1));
		return sb.toString();
	}

	public static String removeLastChar(String result){
		if(TextUtils.isEmpty(result)){
			return "";
		}
		result= result.substring(0, result.length()-1);
		LogUtils.d("removeLastChar：" + result);
		return result;
	}

	public static void showRoundImg(Context mContext, String picUrl, int loadingImg, int faildImg, ImageView mShowImg) {
		if (mContext instanceof Activity) {
			Activity act = (Activity) mContext;
			if (act == null || act.isFinishing()) {
				return;
			}
		}
//        if (Protect.checkLoadImageStatus(mContext)) {
		Glide.with(mContext).load(picUrl).placeholder(loadingImg).error(faildImg).crossFade().into(mShowImg);
//        }
	}
}
