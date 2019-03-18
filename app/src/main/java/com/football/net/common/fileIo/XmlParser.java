/**
 * ID: XmlParser.java
 * Copyright (c) 2002-2014 Luther Inc.
 * http://zuv.cc
 * All rights reserved.
 */
package com.football.net.common.fileIo;

import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * File Description
 *
 * @author			Kama Luther
 * @version			0.1
 * @since           0.1
 * @.createdate     2015年1月3日 下午8:35:08
 * @.modifydate     2015年1月3日 下午8:35:08
 * <DT><B>修改历史记录</B>
 * <DD>
 * 
 * </DD>
 * </DT>
 */
public class XmlParser
{
	
	public XmlParser()
	{
		
	}
	
	public Map<String, XmlObject> parse(InputStream is)
		throws XmlPullParserException, IOException
	{
		Map<String, XmlObject> values = null;
		XmlObject value = null;
		
		XmlPullParser parser = Xml.newPullParser();
		parser.setInput(is, "UTF-8");
		
		int eventType = parser.getEventType();  
		while (eventType != XmlPullParser.END_DOCUMENT)
		{ 
			String name = parser.getName();
			switch (eventType) 
			{ 
				case XmlPullParser.START_DOCUMENT:
					values = new HashMap<String, XmlObject>();
					break;
				case XmlPullParser.END_TAG:
					if("list".equalsIgnoreCase(name))
					{
						values.put(value.getKey(), value);
						value = null;
					}
					break;
				case XmlPullParser.START_TAG:
					if("list".equalsIgnoreCase(name))
					{
						value = new XmlObject();
						value.setVerCode(Integer.parseInt(parser.getAttributeValue(null, "vercode")));
					}
					else if("vername".equalsIgnoreCase(name))
					{
						eventType = parser.next();
						value.setVerName(parser.getText());
					}
					break;
			}
			eventType = parser.next();
		}
		
		return values;
	}
	
	
	public String serialize(Map<String, XmlObject> values)
			throws IllegalArgumentException, IllegalStateException, IOException
	{
		XmlSerializer serializer = Xml.newSerializer();
		StringWriter writer = new StringWriter();
		serializer.setOutput(writer);
		serializer.startDocument("UTF-8", true);
		serializer.startTag("", "apps");

		Iterator<String> it = values.keySet().iterator();
		while (it.hasNext())
		{
			XmlObject value = values.get(it.next());

			serializer.startTag("", "list");
			serializer.attribute("", "vercode", String.valueOf(value.getVerCode()));

			serializer.startTag("", "vername");
			serializer.text(value.getVerName());
			serializer.endTag("", "vername");

			serializer.endTag("", "list");
		}

		serializer.endTag("", "apps");
		serializer.endDocument();
		return writer.toString();
	}
	
	public class XmlObject
	{
		private String key;
		public String getKey()
		{
			return key;
		}
		public void setKey(String _key)
		{
			key = _key;
		}
		
		private int verCode;
		public int getVerCode()
		{
			return verCode;
		}
		public void setVerCode(int verCode)
		{
			this.verCode = verCode;
		}

		private String verName;
		public String getVerName()
		{
			return verName;
		}
		public void setVerName(String verName)
		{
			this.verName = verName;
		}
	}

}
