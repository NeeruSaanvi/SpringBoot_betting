package com.injury.entity;
import java.util.Set;
import com.injury.dto.GamePlayed;

/**
*  @author SureshKoumar @Pinesucceed
*  Jun 28, 2019
*/

public class Player {
	
	private String id; 
	private String sportName;
	private String teamName;
	
	private String playerName;
	private String position; 
	//Player info
	private Set<GamePlayed> games;
	
	private String injury;				
	private String status;				
	
	private String overtime;			
	
	private boolean active = true;
	
	private String rpg;
	private String ppg;
	private String apg;
	private String per;
	
	//for ncaab 
	private String oRating;
	private String dRating;
	
	
	public Player() {
	}

	public Player(String sportName, String teamName, String playerName) {
		super();
		this.id = sportName.replace(" ", "")+teamName.replace(" ", "")+playerName.replace(" ", "");
		this.sportName = sportName;
		this.teamName = teamName;
		this.playerName = playerName;
	}

	@Override
	public boolean equals(Object obj) {
		Player player = (Player)obj;
		if(player.getId().equals(this.getId()))
			return true;
		else return false;
	}
	
	@Override
	public int hashCode() {
		int hash = this.getId().hashCode();
		return hash;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSportName() {
		return sportName;
	}

	public void setSportName(String sportName) {
		this.sportName = sportName;
	}

	public String getTeamName() {
		return teamName;
	}

	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}

	public String getPlayerName() {
		return playerName;
	}

	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}

	public String getInjury() {
		return injury;
	}

	public void setInjury(String injury) {
		this.injury = injury;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getOvertime() {
		return overtime;
	}

	public void setOvertime(String overtime) {
		this.overtime = overtime;
	}


	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public Set<GamePlayed> getGames() {
		return games;
	}

	public void setGames(Set<GamePlayed> games) {
		this.games = games;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getRpg() {
		return rpg;
	}

	public void setRpg(String rpg) {
		this.rpg = rpg;
	}

	public String getPpg() {
		return ppg;
	}

	public void setPpg(String ppg) {
		this.ppg = ppg;
	}

	public String getApg() {
		return apg;
	}

	public void setApg(String apg) {
		this.apg = apg;
	}

	public String getPer() {
		return per;
	}

	public void setPer(String per) {
		this.per = per;
	}

	public String getoRating() {
		return oRating;
	}

	public void setoRating(String oRating) {
		this.oRating = oRating;
	}

	public String getdRating() {
		return dRating;
	}

	public void setdRating(String dRating) {
		this.dRating = dRating;
	}

	@Override
	public String toString() {
		return "Player [id=" + id + ", sportName=" + sportName + ", teamName=" + teamName + ", playerName=" + playerName
				+ ", position=" + position + ", games=" + games + ", injury=" + injury + ", status=" + status
				+ ", overtime=" + overtime + ", active=" + active + ", rpg=" + rpg + ", ppg=" + ppg + ", apg=" + apg
				+ ", per=" + per + ", oRating=" + oRating + ", dRating=" + dRating + "]";
	}

}
