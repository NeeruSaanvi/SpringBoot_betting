package com.injury.controller;

import java.util.Collections;
import java.util.List;

/**
*  @author SureshKoumar @Pinesucceed
*  Jun 28, 2019
*/

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.injury.dao.TeamDAO;
import com.injury.dto.TeamData;
import com.injury.entity.Player;
import com.injury.entity.Team;
import com.injury.errorhandling.AppException;
import com.injury.errorhandling.BatchException;
import com.injury.repository.PlayerRepository;
import com.injury.repository.TeamCategoryRepository;
import com.injury.repository.TeamDataRepository;
import com.injury.repository.TeamsRepository;
import com.injury.sites.InjuryDataProcessor;
import com.injury.sites.SiteProcessor;
import com.injury.sites.sportsreferences.InjuryService;
import com.injury.sites.sportsreferences.SportsReferencesProcessing;

@Controller
public class InjuryController {
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private TeamsRepository teamsRepo;
	
	@Autowired
	private TeamCategoryRepository teamsCatRepo;
	
	@Autowired
	private PlayerRepository playerRepo;
	
	@Autowired
	private ApplicationContext context;
	
	@Autowired
	private TeamDataRepository teamDataRepo;
	
	private InjuryDataProcessor dataProcessor = null;

	@GetMapping("/updateTeamData/{sport}")
	@ResponseBody
	public List<TeamData> updateTeamData(@PathVariable("sport")String sportName) throws BatchException {
		logger.info("Entering updateTeamData()");
		
		SiteProcessor siteProcessor = null;
		List<TeamData> listOfTeam = null;
		
		try {
			siteProcessor = getSiteProcessor("injuryService");
			siteProcessor.setTeamsCatRepo(teamsCatRepo);
			siteProcessor.setTeamsRepo(teamsRepo);
			dataProcessor = (InjuryDataProcessor)context.getBean("injuryDataProcessor");
			siteProcessor.setDataProcessor(dataProcessor);
			siteProcessor.setPlayerRepo(playerRepo);
			siteProcessor.setContext(context);
			siteProcessor.setTeamDataRepo(teamDataRepo);
			teamDataRepo.deleteBySport(sportName);
			listOfTeam = siteProcessor.getTeamData(sportName);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		logger.info("Exiting updateTeamData()");
		return listOfTeam;
	}
	
	@GetMapping("/updateplayer/{sport}")
	@ResponseBody
	public String updatePlayerData(@PathVariable("sport")String sport) {
		logger.info("Entering updatePlayerData()");
		SiteProcessor siteProcessor = null;

		try {
			siteProcessor = getSiteProcessor("sportsreferences");
			siteProcessor.setTeamsCatRepo(teamsCatRepo);
			siteProcessor.setTeamsRepo(teamsRepo);
			dataProcessor = (InjuryDataProcessor)context.getBean("injuryDataProcessor");
			siteProcessor.setDataProcessor(dataProcessor);
			siteProcessor.setPlayerRepo(playerRepo);
			siteProcessor.setContext(context);
			siteProcessor.setTeamDataRepo(teamDataRepo);
			playerRepo.deleteBySportName(sport);
			siteProcessor.updatePlayerData(sport);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		logger.info("Exiting updatePlayerData()");
		return sport+" data saved!";
	}
	
	private SiteProcessor getSiteProcessor(String siteName) throws BatchException {
		logger.info("Entering getAccountSite()");
		logger.debug("Input for Account type: "+siteName);
		SiteProcessor processSite = null;

		// Determine site type
		if ("sportsreferences".equals(siteName)) {
			processSite = new SportsReferencesProcessing();
			logger.error("sportsreferences");
		} else if ("sportsinsight".equals(siteName)) {
			logger.error("Sportinsight ");
		} else if ("injuryService".equals(siteName)) {
			processSite = new InjuryService();
		}
		else {
			throw new AppException("Invalid Site");
		}
		
		return processSite;
	}

	
	@GetMapping("/insert")
	@ResponseBody
	public List<Player> insertTeam() {
		logger.info("Entering insert()");

		List<Player> players = playerRepo.findPlayerofTeam("ncaab", "Big Ten Conference", Sort.by("playerName"));//, "Jake DeBrusk"
		System.out.println(players);
		//teamDataRepo.save(new TeamData());
		
		logger.info("Exiting insert()");
		return players;
	}
	
	@GetMapping("/test")
	@ResponseBody
	public List<Player> test() {
		logger.info("Entering test()");

		List<Player> players = playerRepo.findPlayerofTeam("ncaab", "Big Ten Conference", Sort.by("playerName"));//, "Jake DeBrusk"
		System.out.println(players);
		
		logger.info("Exiting updatePlayerData()");
		return players;
	}
	
	@GetMapping("/test1")
	@ResponseBody
	public Player test1() {
		logger.info("Entering test()");

		Player player = playerRepo.findPlayer("ncaaf", "Georgia", "D'Andre Swift"); 
		System.out.println(player);
		
		logger.info("Exiting updatePlayerData()");
		return player;
	}
	
	@GetMapping("/deleteplayers/{sport}")
	@ResponseBody
	public List<Player> playersdelete(@PathVariable("sport")String sport) {
		logger.info("Entering playersdelete()");
		playerRepo.deleteBySportName(sport);
		logger.info("Exiting playersdelete()");
		return null;
	}
}
