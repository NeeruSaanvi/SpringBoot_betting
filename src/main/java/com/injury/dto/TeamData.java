package com.injury.dto;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.annotation.Id;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
*  @author SureshKoumar @Pinesucceed
*  Aug 5, 2019
*/

@JsonAutoDetect
public class TeamData {

	@Id
	@JsonProperty
	private String id;
	private String sport;
	private String team;
	private List<TeamDetail> detail;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getSport() {
		return sport;
	}
	public void setSport(String sport) {
		this.sport = sport;
	}
	public String getTeam() {
		return team;
	}
	public void setTeam(String team) {
		this.team = team;
	}
	public List<TeamDetail> getDetail() {
		return detail;
	}
	public void setDetail(List<TeamDetail> detail) {
		this.detail = detail;
	}
	@Override
	public String toString() {
		return "TeamData [id=" + id + ", sport=" + sport + ", team=" + team + ", detail=" + detail + "]";
	}
	
}
