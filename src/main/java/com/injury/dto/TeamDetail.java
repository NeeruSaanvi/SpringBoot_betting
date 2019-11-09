package com.injury.dto;

import java.time.LocalDate;

public class TeamDetail {
	
	private LocalDate date;	
	private String opponent;
	private String opponentCode;
	private String result;	
	private String points;
	private String oppPoints;
	private String totalPoints;
	private String closingSpread;	
	private String atsResult;
	private String sideCoverMargin;	
	private String closingTotal;	
	private String overUnder;
	private String totalCoverMargin;	
	
	private String overtime;
	private String offRating;
	private String defRating;
	private String possessions;

	private String week; //nfl
	private String offensiveYPP;//nfl   //for team
	private String defensiveYPP;//nfl   //for team
	private String ATSResult;
	private String overunderResult;
	private String goals;//nhl
	private String oppGoals;//nhl
	private String shotsonGoal;//nhl
	private String oppShotsonGoal;//nhl
	private String closingML;//nhl
	private String puckLineSpread;//nhl
	private String startingPitcher;//mlb    //for team
	private String oppStartingPitcher;//mlb //for team
	private String runsScored;//mlb
	private String oppRuns;//mlb
	private String runLineSpread;//mlb
	private String closingRunLine;//mlb
	private String runLineResult;//mlb
	public LocalDate getDate() {
		return date;
	}
	public void setDate(LocalDate date) {
		this.date = date;
	}
	public String getOpponent() {
		return opponent;
	}
	public void setOpponent(String opponent) {
		this.opponent = opponent;
	}
	public String getOpponentCode() {
		return opponentCode;
	}
	public void setOpponentCode(String opponentCode) {
		this.opponentCode = opponentCode;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public String getPoints() {
		return points;
	}
	public void setPoints(String points) {
		this.points = points;
	}
	public String getOppPoints() {
		return oppPoints;
	}
	public void setOppPoints(String oppPoints) {
		this.oppPoints = oppPoints;
	}
	public String getTotalPoints() {
		return totalPoints;
	}
	public void setTotalPoints(String totalPoints) {
		this.totalPoints = totalPoints;
	}
	public String getClosingSpread() {
		return closingSpread;
	}
	public void setClosingSpread(String closingSpread) {
		this.closingSpread = closingSpread;
	}
	public String getAtsResult() {
		return atsResult;
	}
	public void setAtsResult(String atsResult) {
		this.atsResult = atsResult;
	}
	public String getSideCoverMargin() {
		return sideCoverMargin;
	}
	public void setSideCoverMargin(String sideCoverMargin) {
		this.sideCoverMargin = sideCoverMargin;
	}
	public String getClosingTotal() {
		return closingTotal;
	}
	public void setClosingTotal(String closingTotal) {
		this.closingTotal = closingTotal;
	}
	public String getOverUnder() {
		return overUnder;
	}
	public void setOverUnder(String overUnder) {
		this.overUnder = overUnder;
	}
	public String getTotalCoverMargin() {
		return totalCoverMargin;
	}
	public void setTotalCoverMargin(String totalCoverMargin) {
		this.totalCoverMargin = totalCoverMargin;
	}
	public String getOvertime() {
		return overtime;
	}
	public void setOvertime(String overtime) {
		this.overtime = overtime;
	}
	public String getOffRating() {
		return offRating;
	}
	public void setOffRating(String offRating) {
		this.offRating = offRating;
	}
	public String getDefRating() {
		return defRating;
	}
	public void setDefRating(String defRating) {
		this.defRating = defRating;
	}
	public String getPossessions() {
		return possessions;
	}
	public void setPossessions(String possessions) {
		this.possessions = possessions;
	}
	public String getWeek() {
		return week;
	}
	public void setWeek(String week) {
		this.week = week;
	}
	public String getOffensiveYPP() {
		return offensiveYPP;
	}
	public void setOffensiveYPP(String offensiveYPP) {
		this.offensiveYPP = offensiveYPP;
	}
	public String getDefensiveYPP() {
		return defensiveYPP;
	}
	public void setDefensiveYPP(String defensiveYPP) {
		this.defensiveYPP = defensiveYPP;
	}
	public String getATSResult() {
		return ATSResult;
	}
	public void setATSResult(String aTSResult) {
		ATSResult = aTSResult;
	}
	public String getOverunderResult() {
		return overunderResult;
	}
	public void setOverunderResult(String overunderResult) {
		this.overunderResult = overunderResult;
	}
	public String getGoals() {
		return goals;
	}
	public void setGoals(String goals) {
		this.goals = goals;
	}
	public String getOppGoals() {
		return oppGoals;
	}
	public void setOppGoals(String oppGoals) {
		this.oppGoals = oppGoals;
	}
	public String getShotsonGoal() {
		return shotsonGoal;
	}
	public void setShotsonGoal(String shotsonGoal) {
		this.shotsonGoal = shotsonGoal;
	}
	public String getOppShotsonGoal() {
		return oppShotsonGoal;
	}
	public void setOppShotsonGoal(String oppShotsonGoal) {
		this.oppShotsonGoal = oppShotsonGoal;
	}
	public String getClosingML() {
		return closingML;
	}
	public void setClosingML(String closingML) {
		this.closingML = closingML;
	}
	public String getPuckLineSpread() {
		return puckLineSpread;
	}
	public void setPuckLineSpread(String puckLineSpread) {
		this.puckLineSpread = puckLineSpread;
	}
	public String getStartingPitcher() {
		return startingPitcher;
	}
	public void setStartingPitcher(String startingPitcher) {
		this.startingPitcher = startingPitcher;
	}
	public String getOppStartingPitcher() {
		return oppStartingPitcher;
	}
	public void setOppStartingPitcher(String oppStartingPitcher) {
		this.oppStartingPitcher = oppStartingPitcher;
	}
	public String getRunsScored() {
		return runsScored;
	}
	public void setRunsScored(String runsScored) {
		this.runsScored = runsScored;
	}
	public String getOppRuns() {
		return oppRuns;
	}
	public void setOppRuns(String oppRuns) {
		this.oppRuns = oppRuns;
	}
	public String getRunLineSpread() {
		return runLineSpread;
	}
	public void setRunLineSpread(String runLineSpread) {
		this.runLineSpread = runLineSpread;
	}
	public String getClosingRunLine() {
		return closingRunLine;
	}
	public void setClosingRunLine(String closingRunLine) {
		this.closingRunLine = closingRunLine;
	}
	public String getRunLineResult() {
		return runLineResult;
	}
	public void setRunLineResult(String runLineResult) {
		this.runLineResult = runLineResult;
	}
	@Override
	public String toString() {
		return "TeamDetail [date=" + date + ", opponent=" + opponent + ", opponentCode=" + opponentCode + ", result="
				+ result + ", points=" + points + ", oppPoints=" + oppPoints + ", totalPoints=" + totalPoints
				+ ", closingSpread=" + closingSpread + ", atsResult=" + atsResult + ", sideCoverMargin="
				+ sideCoverMargin + ", closingTotal=" + closingTotal + ", overUnder=" + overUnder
				+ ", totalCoverMargin=" + totalCoverMargin + ", overtime=" + overtime + ", offRating=" + offRating
				+ ", defRating=" + defRating + ", possessions=" + possessions + ", week=" + week + ", offensiveYPP="
				+ offensiveYPP + ", defensiveYPP=" + defensiveYPP + ", ATSResult=" + ATSResult + ", overunderResult="
				+ overunderResult + ", goals=" + goals + ", oppGoals=" + oppGoals + ", shotsonGoal=" + shotsonGoal
				+ ", oppShotsonGoal=" + oppShotsonGoal + ", closingML=" + closingML + ", puckLineSpread="
				+ puckLineSpread + ", startingPitcher=" + startingPitcher + ", oppStartingPitcher=" + oppStartingPitcher
				+ ", runsScored=" + runsScored + ", oppRuns=" + oppRuns + ", runLineSpread=" + runLineSpread
				+ ", closingRunLine=" + closingRunLine + ", runLineResult=" + runLineResult + "]";
	}

	
	
}
