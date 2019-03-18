
package com.football.net.http.reponse.impl;

import com.football.net.bean.GameBean;
import com.football.net.bean.ScoreListBean;
import com.football.net.http.reponse.ListResult;
import com.football.net.http.reponse.Result;

import java.util.List;

public class GameBeanListResult extends ListResult
{
	private List<GameBean> list;

	public List<GameBean> getList() {
		return list;
	}

	public void setList(List<GameBean> list) {
		this.list = list;
	}
}
