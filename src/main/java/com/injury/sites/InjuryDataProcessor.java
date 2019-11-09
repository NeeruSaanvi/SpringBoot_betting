package com.injury.sites;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import com.injury.dto.GamePlayed;
import com.injury.entity.Player;
import com.injury.entity.Team;
import com.injury.entity.TeamCategory;
import com.injury.repository.PlayerRepository;
import com.injury.repository.TeamCategoryRepository;
import com.injury.repository.TeamsRepository;

/**
* @author SureshKoumar @ Pinesucceed
* 28-Jun-2019
*/

@Service
public class InjuryDataProcessor {
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private TeamsRepository teamsRepo;
	
	@Autowired
	private TeamCategoryRepository teamsCatRepo;
	
	@Autowired
	private PlayerRepository playerRepo;
	
	public InjuryDataProcessor() {
	}
	
	
	public void updatePlayerPos(String sportName, String teamName, String playerName, String position) {
		logger.info("Entering updatePlayerpos");
		logger.debug("updating for : "+ sportName+ teamName + playerName);
		Player player = playerRepo.findPlayer(sportName, teamName, playerName);
		player.setPosition(position);
		playerRepo.save(player);
		
		logger.info("Exiting updatePlayerPos");
	}
	
	public void updatePlayerData(String sportName, String teamName, String playerName, Set<GamePlayed> games) {
		logger.info("Entering updatePlayerData");
		logger.debug("updating for : "+ sportName+ ", "+teamName +", "+ playerName);
		Player player = playerRepo.findPlayer(sportName, teamName, playerName);
		player.setGames(games);
		playerRepo.save(player);
		
		logger.info("Exiting updatePlayerData");
	}
	
	public Player getPlayer(String sportName, String teamName, String playerName) {
		logger.info("Entering getPlayer");
		Player player = playerRepo.findPlayer(sportName, teamName, playerName);
		logger.info("Exiting getPlayer");
		return player;
	}
	
	public void updateTeams(Map<String, String> teamsMap, String sport) {
		logger.info("Entering updateTeams");
		
		List<Team> teams = teamsRepo.findBySport(sport, Sort.by("teamName"));
		boolean update = false; 
		
		for(Entry<String, String> entry : teamsMap.entrySet()) {
			String teamCode = entry.getKey();
			String name = entry.getValue();
			Team team = new Team(name, sport);
			team.setTeamCode(teamCode);
			if(teams.contains(team)) {
				int index = teams.indexOf(team);
				Team updateTeam = teams.get(index) ;
				if(updateTeam.getTeamCode() == null) {
					update = true;
					updateTeam.setTeamCode(teamCode);
				}
			} else {
				update = true;
				teams.add(team);
			}
		}
		if(update) {
			logger.debug("Teams data update "+ teams);
			teamsRepo.saveAll(teams);
		}
		
		logger.info("Exiting updateTeams");
	}

	
	public void updateCategory(List<TeamCategory>teamCategory, String sport) {
		List<Team> teamsList = new ArrayList<>();
		for(TeamCategory teamCat : teamCategory) {
			String category = teamCat.getCategory();
			List<Team> teams = teamsRepo.findBySportCategory(sport, category, Sort.by("teamName"));
			System.out.println(teams);
			List<Team> tempteams = new ArrayList<>(teamCat.getTeams());
			for(Team team : tempteams) {
				int teamNo = team.getTeamNo();
				String searchFor = team.getName();
				
			//	if(category.equals("Atlantic Coast Conference") && searchFor.equals("Miami (FL)"))
				Team teamObject = null;
				for(Team tm : teams) {
					String tmName = tm.getName();
					if(tmName.contains(searchFor)){
						teamObject = tm; break;
					}
				}
				
				if(teamObject==null) {
					String temp[] = searchFor.split(" ");
					searchFor = temp[0];
					if(searchFor.contains("'")) {
						int index = searchFor.indexOf("'");
						searchFor = searchFor.substring(0, index);
					}
					if(searchFor.contains(".")) {
						int index = searchFor.indexOf(".");
						searchFor = searchFor.substring(0, index);
					}
					
					for(Team tm : teams) {
						String tmName = tm.getName();
						if(tmName.contains(searchFor)){
							teamObject = tm; break;
						}
					}
					System.out.println(teamObject);
					if(temp.length>=2) {
						if(teamObject==null) {
							searchFor = temp[1];
							if(searchFor.contains("'")) {
								int index = searchFor.indexOf("'");
								searchFor = searchFor.substring(0, index);
							}
							if(searchFor.contains(".")) {
								int index = searchFor.indexOf(".");
								searchFor = searchFor.substring(0, index);
							}
							for(Team tm : teams) {
								String tmName = tm.getName();
								if(tmName.contains(searchFor)){
									teamObject = tm; break;
								}
							}
						}
					}
					if(teamObject==null) {
						teamObject = team;
					}
				}
				
				String code = teamObject.getTeamCode();
				teamObject.setTeamNo(teamNo);
				System.out.println();
				team.setTeamCode(code);
			}
			teamsList.addAll(tempteams);
		}
		teamsRepo.saveAll(teamsList);
		//teamsCatRepo.saveAll(teamCategory);
	}
	
	public TeamsRepository getTeamsRepo() {
		return teamsRepo;
	}

	public void setTeamsRepo(TeamsRepository teamsRepo) {
		this.teamsRepo = teamsRepo;
	}

	public TeamCategoryRepository getTeamsCatRepo() {
		return teamsCatRepo;
	}

	public void setTeamsCatRepo(TeamCategoryRepository teamsCatRepo) {
		this.teamsCatRepo = teamsCatRepo;
	}
	
	
}
