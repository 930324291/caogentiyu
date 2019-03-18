
package com.football.net.http.reponse.impl;

import com.football.net.bean.UserBean;
import com.football.net.http.reponse.Result;

import java.util.HashMap;

public class UpLoadPictureResult extends Result
{
	private String picPath;

	public String getPicPath() {
		return picPath;
	}

	public void setPicPath(String picPath) {
		this.picPath = picPath;
	}
}
