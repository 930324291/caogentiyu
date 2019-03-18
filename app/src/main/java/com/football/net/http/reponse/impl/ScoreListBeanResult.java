
package com.football.net.http.reponse.impl;

import com.football.net.bean.ScoreListBean;
import com.football.net.bean.UserBean;
import com.football.net.http.reponse.ListResult;
import com.football.net.http.reponse.Result;

import java.util.List;

public class ScoreListBeanResult extends ListResult
{
	private List<ScoreListBean> data;

	public List<ScoreListBean> getData() {
		return data;
	}

	public void setData(List<ScoreListBean> data) {
		this.data = data;
	}
}
