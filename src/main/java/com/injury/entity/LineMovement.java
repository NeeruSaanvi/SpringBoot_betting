package com.injury.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.annotation.Id;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.injury.dto.MovementData;

/**
*  @author SureshKoumar @Pinesucceed
*  May 3, 2019
*/
@JsonAutoDetect
public class LineMovement {

	@Id
	@JsonProperty
	private String id;
	/**
	 * game date and time
	 */
	@JsonProperty
	private LocalDateTime eventDateTime;
	
	@JsonProperty
	private LocalDate eventDate;
	
	/**
	 * {1=nfl, 2=nba, 3=mlb, 4=nhl, 8=wnba, 11=ncaaf, 12=ncaab}
	 */
	@JsonProperty
	private int sportNumber; 
	
	/**
	 * {1=nfl, 2=nba, 3=mlb, 4=nhl, 8=wnba, 11=ncaaf, 12=ncaab}
	 */
	@JsonProperty
	private String sportType; 
	
	/**
	 * // Game - 0 // First Half - 1  // Second Half - 2
	 */
	@JsonProperty
	private int gameNumber;
	
	/**
	 * // Game - 0 // First Half - 1  // Second Half - 2
	 */
	@JsonProperty
	private String gameType;
	
	
	/**
	 * //{1=Spread, 2=Moneyline, 3=OverUnder}
	 */
	@JsonProperty
	private int lineNumber; 
	
	/**
	 * //{1=Spread, 2=Moneyline, 3=OverUnder}
	 */
	@JsonProperty
	private String lineType; 
	
	/**
	 * year value
	 */
	@JsonProperty
	private Integer year;
	
	/**
	 * name of the sportbook e.g Consensus, ATLANTIS LINE MOVEMENTS etc..
	 */
	@JsonProperty
	private String sportsbook;
	
	/**
	 * Team one name
	 */
	@JsonProperty
	private String visitorteam;  
	
	/**
	 * Team two name
	 */
	@JsonProperty
    private String hometeam;
	
	/**
	 * name of the site data associated with
	 */
	@JsonProperty
	private String siteName;
	
	private String homeScore;
	private String visitorScore;
	/**
	 * Line movement history
	 */
	@JsonProperty
	private List<MovementData> linemovements = new ArrayList<>();
//	@JsonProperty
	//private Map<String,List<MovementData>> linemovements = new HashMap<String, List<MovementData>>();

	@Override
	public boolean equals(Object obj) {
		LineMovement lm = (LineMovement) obj;
		if(lm.eventDate.equals(this.getEventDate()))
			return true;
		else 
			return false;
	}

	@Override
	public int hashCode() {
		int hash = (this.hometeam+this.getEventDate()).hashCode();
		return hash;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	/**
	 * getters and Setters
	 */
	
	public int getSportNumber() {
		return sportNumber;
	}

	public LocalDateTime getEventDateTime() {
		return eventDateTime;
	}

	public void setEventDateTime(LocalDateTime eventDateTime) {
		this.eventDateTime = eventDateTime;
	}

	public void setSportNumber(int sportNumber) {
		this.sportNumber = sportNumber;
	}

	public String getSportType() {
		return sportType;
	}

	public void setSportType(String sportType) {
		this.sportType = sportType;
	}

	public int getGameNumber() {
		return gameNumber;
	}

	public void setGameNumber(int gameNumber) {
		this.gameNumber = gameNumber;
	}

	public String getGameType() {
		return gameType;
	}

	public void setGameType(String gameType) {
		this.gameType = gameType;
	}

	public int getLineNumber() {
		return lineNumber;
	}

	public void setLineNumber(int lineNumber) {
		this.lineNumber = lineNumber;
	}

	public String getLineType() {
		return lineType;
	}

	public void setLineType(String lineType) {
		this.lineType = lineType;
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public String getSportsbook() {
		return sportsbook;
	}

	public void setSportsbook(String sportsbook) {
		this.sportsbook = sportsbook;
	}

	public String getVisitorteam() {
		return visitorteam;
	}

	public void setVisitorteam(String visitorteam) {
		this.visitorteam = visitorteam;
	}

	public String getHometeam() {
		return hometeam;
	}

	public void setHometeam(String hometeam) {
		this.hometeam = hometeam;
	}

	
	public List<MovementData> getLinemovements() {
		return linemovements;
	}

	public void setLinemovements(List<MovementData> linemovements) {
		this.linemovements = linemovements;
	}

	public LocalDate getEventDate() {
		return eventDate;
	}

	public void setEventDate(LocalDate eventDate) {
		this.eventDate = eventDate;
	}

	public String getSiteName() {
		return siteName;
	}

	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}

	public String getHomeScore() {
		return homeScore;
	}

	public void setHomeScore(String homeScore) {
		this.homeScore = homeScore;
	}

	public String getVisitorScore() {
		return visitorScore;
	}

	public void setVisitorScore(String visitorScore) {
		this.visitorScore = visitorScore;
	}

	@Override
	public String toString() {
		return "LineMovement [id=" + id + ", eventDateTime=" + eventDateTime + ", eventDate=" + eventDate
				+ ", sportNumber=" + sportNumber + ", sportType=" + sportType + ", gameNumber=" + gameNumber
				+ ", gameType=" + gameType + ", lineNumber=" + lineNumber + ", lineType=" + lineType + ", year=" + year
				+ ", sportsbook=" + sportsbook + ", visitorteam=" + visitorteam + ", hometeam=" + hometeam
				+ ", siteName=" + siteName + ", homeScore=" + homeScore + ", visitorScore=" + visitorScore
				+ ", linemovements=" + linemovements + "]";
	}

}
