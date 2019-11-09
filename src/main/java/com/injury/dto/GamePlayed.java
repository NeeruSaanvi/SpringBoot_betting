package com.injury.dto;

import java.time.LocalDate;

/**
*  @author SureshKoumar @Pinesucceed
*  Jun 29, 2019
*/

public class GamePlayed {
	
	private LocalDate gameDate;
	private String minutesPlayed="0";     	
	private String minuitesLossed="0";		
	private String position;  
	private String team;
	private String opponent;
	private String points;
	private String result;
	private String ORating;     		
	private String DRating;
	private String status;
	private String mlb_pa;
	private String offensive_snap;
	private String defensive_snap;
	private String week;
	private String day;
	
	public GamePlayed() {
		super();
	}

	public GamePlayed(LocalDate gameDate, String minutesPlayed, String minuitesLossed, String position, String team,
			String opponent, String status, String points, String result, String oRating, String dRating) {
		super();
		this.gameDate = gameDate;
		this.minutesPlayed = minutesPlayed;
		this.minuitesLossed = minuitesLossed;
		this.position = position;
		this.team = team;
		this.opponent = opponent;
		this.status = status;
		this.points = points;
		this.result = result;
		ORating = oRating;
		DRating = dRating;
	}

	public LocalDate getGameDate() {
		return gameDate;
	}

	public void setGameDate(LocalDate gameDate) {
		this.gameDate = gameDate;
	}

	public String getMinutesPlayed() {
		return minutesPlayed;
	}

	public void setMinutesPlayed(String minutesPlayed) {
		this.minutesPlayed = minutesPlayed;
	}

	public String getMinuitesLossed() {
		return minuitesLossed;
	}

	public void setMinuitesLossed(String minuitesLossed) {
		this.minuitesLossed = minuitesLossed;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getTeam() {
		return team;
	}

	public void setTeam(String team) {
		this.team = team;
	}

	public String getOpponent() {
		return opponent;
	}

	public void setOpponent(String opponent) {
		this.opponent = opponent;
	}

	public String getPoints() {
		return points;
	}

	public void setPoints(String points) {
		this.points = points;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getORating() {
		return ORating;
	}

	public void setORating(String oRating) {
		ORating = oRating;
	}

	public String getDRating() {
		return DRating;
	}

	public void setDRating(String dRating) {
		DRating = dRating;
	}
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMlb_pa() {
		return mlb_pa;
	}

	public void setMlb_pa(String mlb_pa) {
		this.mlb_pa = mlb_pa;
	}
	
	public String getOffensive_snap() {
		return offensive_snap;
	}

	public void setOffensive_snap(String offensive_snap) {
		this.offensive_snap = offensive_snap;
	}

	public String getDefensive_snap() {
		return defensive_snap;
	}

	public void setDefensive_snap(String defensive_snap) {
		this.defensive_snap = defensive_snap;
	}

	public String getWeek() {
		return week;
	}

	public void setWeek(String week) {
		this.week = week;
	}

	public String getDay() {
		return day;
	}

	public void setDay(String day) {
		this.day = day;
	}

	@Override
	public String toString() {
		return "GamePlayed [gameDate=" + gameDate + ", minutesPlayed=" + minutesPlayed + ", minuitesLossed="
				+ minuitesLossed + ", position=" + position + ", team=" + team + ", opponent=" + opponent + ", points="
				+ points + ", result=" + result + ", ORating=" + ORating + ", DRating=" + DRating + ", status=" + status
				+ ", mlb_pa=" + mlb_pa + ", offensive_snap=" + offensive_snap + ", defensive_snap=" + defensive_snap
				+ ", week=" + week + ", day=" + day + "]";
	}

}
