package com.injury.entity;

import java.util.HashSet;
import java.util.Set;

import org.springframework.data.annotation.Id;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
*  @author SureshKoumar @Pinesucceed
*  Jun 28, 2019
*/

@JsonAutoDetect
public class TeamCategory {
	
	@Id
	@JsonProperty
	private String id;
	private String sport;
	private String category;
	private String subcategory;
	private Set<Team> teams = new HashSet<>();
	
	public TeamCategory() {
	}

	
	public TeamCategory(String id, String sport, String category, String subcategory) {
		super();
		this.id = id;
		this.sport = sport;
		this.category = category;
		this.subcategory = subcategory;
	}

	public TeamCategory(String sport, String category, String subcategory) {
		super();
		this.id = category.replace(" ", "")+subcategory.replace(" ", "")+sport;
		this.sport = sport;
		this.category = category;
		this.subcategory = subcategory;
	}
	
	@Override
	public boolean equals(Object obj) {
		TeamCategory teamcat = (TeamCategory) obj;
		if(teamcat.getId().equals(this.getId()))
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

	public String getSport() {
		return sport;
	}

	public void setSport(String sport) {
		this.sport = sport;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getSubcategory() {
		return subcategory;
	}
	public void setSubcategory(String subcategory) {
		this.subcategory = subcategory;
	}

	public java.util.Set<Team> getTeams() {
		return teams;
	}


	public void setTeams(java.util.Set<Team> teams) {
		this.teams = teams;
	}


	@Override
	public String toString() {
		return "TeamCategory [id=" + id + ", sport=" + sport + ", category=" + category + ", subcategory=" + subcategory
				+ ", teams=" + teams + "]";
	}

}
