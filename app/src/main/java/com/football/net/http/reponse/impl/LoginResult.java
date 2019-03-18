
package com.football.net.http.reponse.impl;

import com.football.net.bean.PlayerLikeBean;
import com.football.net.bean.TeamLikeBean;
import com.football.net.bean.UserBean;
import com.football.net.http.reponse.Result;

import java.util.List;

public class LoginResult extends Result
{
	private UserBean user;

	private List<TeamLikeBean> teamLikes;

	private List<PlayerLikeBean> playerLikes;

	private int like_player_max;  // 每天给球员点赞次数上限

	private int like_team_max;  // 每天给球队点赞次数上限

	private int dare_max;  // 球队每天约战的次数上限

	private int create_team_max; // 球员每天创建球队上限

	public int getLike_player_max() {
		return like_player_max;
	}

	public void setLike_player_max(int like_player_max) {
		this.like_player_max = like_player_max;
	}

	public int getLike_team_max() {
		return like_team_max;
	}

	public void setLike_team_max(int like_team_max) {
		this.like_team_max = like_team_max;
	}

	public int getDare_max() {
		return dare_max;
	}

	public void setDare_max(int dare_max) {
		this.dare_max = dare_max;
	}

	public int getCreate_team_max() {
		return create_team_max;
	}

	public void setCreate_team_max(int create_team_max) {
		this.create_team_max = create_team_max;
	}

	public UserBean getUser() {
		return user;
	}

	public void setUser(UserBean user) {
		this.user = user;
	}

	public List<TeamLikeBean> getTeamLikes() {
		return teamLikes;
	}

	public void setTeamLikes(List<TeamLikeBean> teamLikes) {
		this.teamLikes = teamLikes;
	}

	public List<PlayerLikeBean> getPlayerLikes() {
		return playerLikes;
	}

	public void setPlayerLikes(List<PlayerLikeBean> playerLikes) {
		this.playerLikes = playerLikes;
	}
}
