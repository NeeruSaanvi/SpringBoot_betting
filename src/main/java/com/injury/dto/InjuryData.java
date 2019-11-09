package com.injury.dto;
/**
*  @author SureshKoumar @Pinesucceed
*  Aug 5, 2019
*/

import java.time.LocalDate;
import java.util.List;
import java.util.TreeMap;

import com.injury.entity.Player;

public class InjuryData {
	private List<TeamDetail> topTableData;
	private List<Player> playersData;
	private TreeMap<LocalDate, String> headerRow;
	
	public List<TeamDetail> getTopTableData() {
		return topTableData;
	}
	public void setTopTableData(List<TeamDetail> topTableData) {
		this.topTableData = topTableData;
	}
	public List<Player> getPlayersData() {
		return playersData;
	}
	public void setPlayersData(List<Player> playersData) {
		this.playersData = playersData;
	}
	public TreeMap<LocalDate, String> getHeaderRow() {
		return headerRow;
	}
	public void setHeaderRow(TreeMap<LocalDate, String> headerRow) {
		this.headerRow = headerRow;
	}
	@Override
	public String toString() {
		return "InjuryData [topTableData=" + topTableData + ", playersData=" + playersData + ", headerRow=" + headerRow
				+ "]";
	}
	
}
