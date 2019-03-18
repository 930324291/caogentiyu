
package com.football.net.http.reponse.impl;

import com.football.net.bean.GameBean;
import com.football.net.bean.NewsBean;
import com.football.net.http.reponse.ListResult;

import java.util.List;

public class NewsBeanListResult extends ListResult
{
	private List<NewsBean> list;

	public List<NewsBean> getList() {
		return list;
	}

	public void setList(List<NewsBean> list) {
		this.list = list;
	}
}
