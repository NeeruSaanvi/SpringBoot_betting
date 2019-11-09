package com.injury.dto;

import java.time.LocalDate;

/**
*  @author SureshKoumar @Pinesucceed
*  Aug 29, 2019
*/

public class EspnYPPObject {
	
    private LocalDate date;
    private String gameid;
    private String name;
    private String shortName;
    private String week;
    
    private String homeoraway1;
    private String displayName1;
    private String shortDisplayName1;
    private String abbrivation1;
    private String score1;
    private String ypp1;
    
    private String homeoraway2;
    private String displayName2;
    private String shortDisplayName2;
    private String abbrivation2;
    private String score2;
    private String ypp2;
    
    
	public LocalDate getDate() {
		return date;
	}
	public void setDate(LocalDate date) {
		this.date = date;
	}
	public String getGameid() {
		return gameid;
	}
	public void setGameid(String gameid) {
		this.gameid = gameid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getHomeoraway1() {
		return homeoraway1;
	}
	public void setHomeoraway1(String homeoraway1) {
		this.homeoraway1 = homeoraway1;
	}
	public String getDisplayName1() {
		return displayName1;
	}
	public void setDisplayName1(String displayName1) {
		this.displayName1 = displayName1;
	}
	
	public String getShortDisplayName1() {
		return shortDisplayName1;
	}
	public void setShortDisplayName1(String shortDisplayName1) {
		this.shortDisplayName1 = shortDisplayName1;
	}
	public String getAbbrivation1() {
		return abbrivation1;
	}
	public void setAbbrivation1(String abbrivation1) {
		this.abbrivation1 = abbrivation1;
	}
	public String getScore1() {
		return score1;
	}
	public void setScore1(String score1) {
		this.score1 = score1;
	}
	public String getYpp1() {
		return ypp1;
	}
	public void setYpp1(String ypp1) {
		this.ypp1 = ypp1;
	}
	public String getHomeoraway2() {
		return homeoraway2;
	}
	public void setHomeoraway2(String homeoraway2) {
		this.homeoraway2 = homeoraway2;
	}
	public String getDisplayName2() {
		return displayName2;
	}
	public void setDisplayName2(String displayName2) {
		this.displayName2 = displayName2;
	}
	
	public String getShortDisplayName2() {
		return shortDisplayName2;
	}
	public void setShortDisplayName2(String shortDisplayName2) {
		this.shortDisplayName2 = shortDisplayName2;
	}
	public String getAbbrivation2() {
		return abbrivation2;
	}
	public void setAbbrivation2(String abbrivation2) {
		this.abbrivation2 = abbrivation2;
	}
	public String getScore2() {
		return score2;
	}
	public void setScore2(String score2) {
		this.score2 = score2;
	}
	public String getYpp2() {
		return ypp2;
	}
	public void setYpp2(String ypp2) {
		this.ypp2 = ypp2;
	}
	public String getShortName() {
		return shortName;
	}
	public void setShortName(String shortName) {
		this.shortName = shortName;
	}
	
	public String getWeek() {
		return week;
	}
	public void setWeek(String week) {
		this.week = week;
	}

	@Override
	public String toString() {
		return "EspnYPPObject [date=" + date + ", gameid=" + gameid + ", name=" + name + ", shortName=" + shortName
				+ ", week=" + week + ", homeoraway1=" + homeoraway1 + ", displayName1=" + displayName1
				+ ", shortDisplayName1=" + shortDisplayName1 + ", abbrivation1=" + abbrivation1 + ", score1=" + score1
				+ ", ypp1=" + ypp1 + ", homeoraway2=" + homeoraway2 + ", displayName2=" + displayName2
				+ ", shortDisplayName2=" + shortDisplayName2 + ", abbrivation2=" + abbrivation2 + ", score2=" + score2
				+ ", ypp2=" + ypp2 + "]";
	}
}
