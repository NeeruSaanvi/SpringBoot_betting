package com.injury.entity;
/**
*  @author SureshKoumar @Pinesucceed
*  Jun 28, 2019
*/

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonAutoDetect 
public class Team {
	
	@Id
	@JsonProperty
	private String id;
	
	private String name;
	
	private String sport;
	
	private String teamcategory;
	
	private String subCategory;
	
	private String teamCode;
	
	private int teamNo;
	
	private String frontName;
	
	public Team() {
		super();
	}

	public Team(String name, String sport) {
		super();
		this.id = name.replaceAll(" ", "")+sport;
		this.name = name;
		this.sport = sport;
	}

	@Override
	public boolean equals(Object obj) {
		Team home = (Team) obj;
		if(home.getId().equals(this.getId()))
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

	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSport() {
		return sport;
	}

	public void setSport(String sport) {
		this.sport = sport;
	}

	public String getTeamcategory() {
		return teamcategory;
	}

	public void setTeamcategory(String teamcategory) {
		this.teamcategory = teamcategory;
	}

	public String getSubCategory() {
		return subCategory;
	}

	public void setSubCategory(String subCategory) {
		this.subCategory = subCategory;
	}


	public String getTeamCode() {
		return teamCode;
	}

	public void setTeamCode(String teamCode) {
		this.teamCode = teamCode;
	}

	public int getTeamNo() {
		return teamNo;
	}

	public void setTeamNo(int teamNo) {
		this.teamNo = teamNo;
	}

	public String getFrontName() {
		return frontName;
	}

	public void setFrontName(String frontName) {
		this.frontName = frontName;
	}

	@Override
	public String toString() {
		return "Team [id=" + id + ", name=" + name + ", sport=" + sport + ", teamcategory=" + teamcategory
				+ ", subCategory=" + subCategory + ", teamCode=" + teamCode + ", teamNo=" + teamNo + ", frontName="
				+ frontName + "]";
	}


}
