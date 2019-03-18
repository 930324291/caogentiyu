
package com.football.net.http.reponse.impl;

import com.football.net.bean.GameBean;
import com.football.net.bean.GameLogBean;
import com.football.net.http.reponse.ListResult;

import java.util.List;

public class GameLogBeanListResult extends ListResult
{
	private List<GameLogBean> list;

	public List<GameLogBean> getList() {
		return list;
	}

	public void setList(List<GameLogBean> list) {
		this.list = list;
	}
}
