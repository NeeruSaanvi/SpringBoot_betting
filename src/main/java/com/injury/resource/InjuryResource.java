package com.injury.resource;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Function;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.injury.dto.GamePlayed;
import com.injury.dto.InjuryData;
import com.injury.dto.MovementData;
import com.injury.dto.TeamData;
import com.injury.dto.TeamDetail;
import com.injury.dto.TeamList;
import com.injury.entity.LineMovement;
import com.injury.entity.Player;
import com.injury.entity.Team;
import com.injury.entity.TeamCategory;
import com.injury.repository.LineMovementRepository;
import com.injury.repository.PlayerRepository;
import com.injury.repository.TeamCategoryRepository;
import com.injury.repository.TeamDataRepository;
import com.injury.repository.TeamsRepository;


/**
*  @author SureshKoumar @Pinesucceed
*  Jul 11, 2019
*/

@RestController
@RequestMapping("/info/v1")
public class InjuryResource {
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private TeamsRepository teamsRepo;
	
	@Autowired
	private TeamCategoryRepository teamsCatRepo;
	
	@Autowired
	private TeamCategoryRepository teamCatRepo;
	
	@Autowired
	private LineMovementRepository lineMovementRepo;
	
	@Autowired
	private PlayerRepository playersRepo;
	
	@Autowired
	private TeamDataRepository teamDataRepo;

	
	@GetMapping("/teams/{sport}")
	public List<Team> getTeams(@PathVariable("sport")String sport){
		logger.info("Entering getTeams");
		
		Sort sort = Sort.by(Direction.ASC, "name");
		
		List<Team> teams = teamsRepo.findBySport(sport, sort);
		
		logger.info("Exiting getTeams");
		return teams;
	}
	
	//http://localhost:9090/playersinfo/info/v1/teamplayers?team=Atlanta Hawks&sport=nba&game=game&line=spread&sportsbook=BOOKMAKER LINE MOVEMENTS

	@GetMapping("/teamplayers")
	public InjuryData getTeamsData(@RequestParam("team")String team, @RequestParam("sport")String sportType,
			@RequestParam("game")String gameType, @RequestParam("line")String lineType, 
			@RequestParam("sportsbook")String sportsbook){
		logger.info("Entering getTeamsData");
		
		Sort sort = Sort.by(Direction.ASC, "eventDateTime");

		//List<SummaryData> summaryDataList = summaryDataRepo.findByTeams(team, sportType, gameType, lineType, sportsbook, sort);
		List<LineMovement> lineMovements = lineMovementRepo.getTeamData(sportType, team, "spread", "game", "PINNACLESPORTS LINE MOVEMENTS", sort);
		List<LineMovement> lineMovements_total = lineMovementRepo.getTeamData(sportType, team, "total", "game", "PINNACLESPORTS LINE MOVEMENTS", sort);
		List<LineMovement> lineMovements_ml = lineMovementRepo.getTeamData(sportType, team, "moneyline", "game", "PINNACLESPORTS LINE MOVEMENTS", sort);
		
		//playersRepo.save(new Player());
		if(!sportType.equals("nba")) {
			List<Player> playersList = playersRepo.findPlayers(sportType);
			for(Player player : playersList){
				String teamName=player.getTeamName();
				if(teamName!=null && team.contains(teamName)) {
					team = teamName;
					break;
				}
			}
		}
			
		
		List<Player> players = playersRepo.findPlayerofTeam(sportType, team, Sort.by("playerName"));
		//System.out.println(players.get(0));
		List<TeamData> teamData = teamDataRepo.findTeamofSport(sportType, team, Sort.by("team"));
		InjuryData injuryData = new InjuryData();
		
		TreeMap<LocalDate, String> headerRow = new TreeMap<>(); 
		List<TeamDetail> teamDataList = new ArrayList<>();
		for(TeamData td : teamData ) {
			//String teamName = td.getTeam();

			String teamOne="", teamTwo="";
			String teamOneScore="0", teamTwoScore="0";
			Integer scoreOne = 0, scoreTwo = 0;
			String homeoraway = "";
			
			LineMovement spread_lm = null;
			LineMovement total_lm = null;
			LineMovement ml_lm = null;
			
			String close = "";

			List<TeamDetail> list = td.getDetail();
			for(TeamDetail tdetail : list) {
				LocalDate date = tdetail.getDate();
				System.out.println(date);
				//if(date.equals(LocalDate.of(2019, 3, 11)))
				//	System.out.println();
				if(date==null) continue;
				//if(spread_lm==null)
				spread_lm = findObject(lineMovements,date);
				if(spread_lm==null) continue;
				//if(total_lm==null)
				total_lm = findObject(lineMovements_total,date);
				if(total_lm==null) continue;
				
				ml_lm = findObject(lineMovements_ml,date);
				if(ml_lm==null) continue;
				
				if(spread_lm.getHometeam().equals(team)) {
					teamOne = spread_lm.getHometeam();
					teamOneScore = spread_lm.getHomeScore();

					teamTwo = spread_lm.getVisitorteam();
					teamTwoScore = spread_lm.getVisitorScore();
					
					homeoraway = "Away";
					tdetail.setOpponent(spread_lm.getVisitorteam());
				} else {
					teamOne = spread_lm.getVisitorteam();
					teamOneScore = spread_lm.getVisitorScore();

					teamTwo = spread_lm.getHometeam();
					teamTwoScore = spread_lm.getHomeScore();

					tdetail.setOpponent(spread_lm.getHometeam());
					homeoraway = "Home";
				}
				if(teamOneScore==null)teamOneScore="0";
				if(teamTwoScore==null)teamTwoScore="0";
				if(teamOneScore.length()==0)teamOneScore="0";
				if(teamTwoScore.length()==0)teamTwoScore="0";
				scoreOne = Integer.parseInt(teamOneScore);
				scoreTwo = Integer.parseInt(teamTwoScore);

				String lw1 = "", lw2 = "";
				if (scoreOne < scoreTwo) {
					lw1 = "L";
					lw2 = "W";
				} else {
					lw1 = "W";
					lw2 = "L";
				}
				tdetail.setResult(lw1 + scoreOne + " - " + lw2 + scoreTwo);
				
				try {
					if(!headerRow.containsKey(date)) {
						headerRow.put(date, lw1 + scoreOne + " - " + lw2 + scoreTwo);
					}else {
						continue;
					}
				}catch(Exception e) {
					System.out.println(date);
					System.out.println(lw1 + scoreOne + " - " + lw2 + scoreTwo);
					System.exit(0);
				}
				tdetail.setPoints(teamOneScore);
				tdetail.setOppPoints(teamTwoScore);
				tdetail.setOpponent(findOpponent(players,date));
				
				List<MovementData> md = spread_lm.getLinemovements();
				if(md.size()>0) {
					MovementData mdata = md.get(md.size() - 1);
					close = getOpenClose(mdata, homeoraway, "game", sportType);
					tdetail.setClosingSpread(close);
					TeamDetail set = setcovermarginData(spread_lm, homeoraway, close, total_lm,tdetail, sportType);
					
					List<MovementData> mlmd = ml_lm.getLinemovements();
					if(!sportType.equals("ncaab")) {
						MovementData linedata = mlmd.get(mlmd.size()-1);
						String ml_close = getMoneylineOpenClose(linedata, homeoraway);
						set.setClosingML(ml_close);
					}
					
					int i = total_lm.getLinemovements().size();
					set.setClosingTotal(getTotalOpenClose(total_lm.getLinemovements().get(i-1), homeoraway));
					teamDataList.add(set);
				}
			}
			
		}
		
		injuryData.setTopTableData(teamDataList);
		injuryData.setPlayersData(players);
		injuryData.setHeaderRow(headerRow);
		logger.info("Exiting getTeamsData");
		return injuryData;
	}
	
	private LineMovement findObject(List<LineMovement> lineMovements, LocalDate find) {
		LineMovement lineMovement = null;
		for(LineMovement lm : lineMovements) {
			LocalDateTime dateTime = lm.getEventDateTime();
			LocalDate date = LocalDate.of(dateTime.getYear(), dateTime.getMonthValue(), dateTime.getDayOfMonth()); 
			if(date.equals(find)) {
				lineMovement = lm;
				break;
			}
		}
		
		return lineMovement;
	} 
	
	private String findOpponent (List<Player> players, LocalDate date) {
		try {
		for(Player player : players) {
			Iterator<GamePlayed> itr = player.getGames().iterator();
			while(itr.hasNext()) {
				GamePlayed gp = itr.next();
				//System.out.println("player "+player.getPlayerName()+", date: "+gp.getGameDate());
				try {
				if(gp.getGameDate().equals(date))
					return gp.getOpponent();
				} catch(NullPointerException npe) {
					logger.error("Null Date: ");
				}
			}
		}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return "";
	}
	
	Function<List<Player>, TreeMap<LocalDate, String>> f = players ->{
		TreeMap<LocalDate, String> map = new TreeMap<>();
		for(Player player : players) {
			Iterator<GamePlayed> games = player.getGames().iterator();
			while(games.hasNext()) {
				GamePlayed gp = games.next();
				//LocalDate ld = LocalDate.of(2018, 10, 16);
				//if(ld.equals(gp.getGameDate()))
				//	System.out.println("Found "+gp.getGameDate());
				map.put(gp.getGameDate(), gp.getOpponent());
			}
		}
		return map;
	};
	
	private String getMoneylineOpenClose(MovementData linedata, String homeoraway) {
		String close = "0.0";
		
		double line1 = Double.parseDouble(linedata.getLineindicator1() + Math.abs(linedata.getLineone()));
		double line2 = Double.parseDouble(linedata.getLineindicator2() + Math.abs(linedata.getLinetwo()));
		String indicator1 = linedata.getJuiceindicator1();
		String indicator2 = linedata.getJuiceindicator2();
		double juice1 = Double.parseDouble(indicator1+Math.abs(linedata.getJuiceone()));
		double juice2 = Double.parseDouble(indicator2+Math.abs(linedata.getJuicetwo()));
		
		if(homeoraway.equals("Home")) {
			if(juice1>0)
				close = "+"+juice1;
			else
				close = juice1+"";
			/*
			 * if(linedata.getJuiceone()!=110) { close+="<sub> ("+juice1+")<sub>"; }
			 */
		}else if(homeoraway.equals("Away")) {
			if(juice2>0)
				close = "+"+juice2;
			else
				close = juice2+"";
			/*
			 * if(linedata.getJuicetwo()!=110) { close+="<sub> ("+juice2+")<sub>"; }
			 */
		}
		
		return close;
	}
	
	private String getTotalOpenClose(MovementData linedata, String homeoraway) {
		String close = "0.0";
		String lineindicator1 = linedata.getLineindicator1();
		String lineindicator2 = linedata.getLineindicator2();
		
		double line1 = linedata.getLineone();
		double line2 = linedata.getLinetwo();
		String indicator1 = linedata.getJuiceindicator1();
		String indicator2 = linedata.getJuiceindicator2();
		double juice1 = Double.parseDouble(indicator1+Math.abs(linedata.getJuiceone()));
		double juice2 = Double.parseDouble(indicator2+Math.abs(linedata.getJuicetwo()));
		
		if(homeoraway.equals("Home")) {
			close = line1+"";
			/*if(linedata.getJuiceone()!=110) {
				close+="<sub> ("+(int)juice1+")<sub>";
			}*/
		}else if(homeoraway.equals("Away")) {
			close = line2+"";
			/*if(linedata.getJuicetwo()!=110) {
				close+="<sub> ("+(int)juice2+")<sub>";
			}*/
		}
		
		return close;
	}
	
	private String getOpenClose(MovementData linedata, String homeoraway, String game, String sport) {
		String close = "0.0";
		
		double line1 = Double.parseDouble(linedata.getLineindicator1() + Math.abs(linedata.getLineone()));
		double line2 = Double.parseDouble(linedata.getLineindicator2() + Math.abs(linedata.getLinetwo()));
		String indicator1 = linedata.getJuiceindicator1();
		String indicator2 = linedata.getJuiceindicator2();
		double juice1 = Double.parseDouble(indicator1+Math.abs(linedata.getJuiceone()));
		double juice2 = Double.parseDouble(indicator2+Math.abs(linedata.getJuicetwo()));
		if(game.equals("first") || game.equals("second") || sport.equals("nhl") || sport.equals("mlb")) {
			if(homeoraway.equals("Home")) {
				close = juice1+"";
				/*
				 * if(linedata.getJuiceone()!=110) { close+="<sub> ("+juice1+")<sub>"; }
				 */
			}else if(homeoraway.equals("Away")) {
				close = juice2+"";
				/*
				 * if(linedata.getJuicetwo()!=110) { close+="<sub> ("+juice2+")<sub>"; }
				 */
			}
		}else {
			if(homeoraway.equals("Home")) {
				close = line1+"";
				//if(linedata.getJuiceone()!=110) {
				//	close+="<sub> ("+(int)juice1+")<sub>";
				//}
			}else if(homeoraway.equals("Away")) {
				close = line2+"";
				//if(linedata.getJuicetwo()!=110) {
					//close+="<sub> ("+(int)juice2+")<sub>";
				//}
			}
		}
		return close;
	}
	
	
	private TeamDetail setcovermarginData(LineMovement lm, String homeoraway, String close, LineMovement total_lm, TeamDetail object, String sport) {
		String hteam = lm.getHometeam();
		String vteam = lm.getVisitorteam();
		String sportsbook = lm.getSportsbook();
		//LocalDate date = eventDateTime.toLocalDate();
		//LocalDate eventDate = LocalDate.of(2018, 10, 1);
		//if((hteam.equals("Orlando Magic") || vteam.equals("Orlando Magic")) && date.equals(eventDate) && sportsbook.equals("BOOKMAKER LINE MOVEMENTS"))
		//	System.out.println();
		
		String homeScore = lm.getHomeScore();
		String visitorScore = lm.getVisitorScore();
		
		String homeScore1 = total_lm.getHomeScore();
		String visitorScore1 = total_lm.getVisitorScore();
		
		int i = total_lm.getLinemovements().size();
		String totalClose = getTotalOpenClose(total_lm.getLinemovements().get(i-1), homeoraway);
		
		int score=0;
		int opponentScore=0;
		  
		try {
		if(homeoraway.equals("Home")) {
			score = Integer.parseInt(homeScore.replaceAll(" ", ""));
			opponentScore = Integer.parseInt(visitorScore.replaceAll(" ", ""));
		}else if(homeoraway.equals("Away")) {
			score = Integer.parseInt(visitorScore.replaceAll(" ", ""));
			opponentScore = Integer.parseInt(homeScore.replaceAll(" ", ""));
		}
		}catch(NumberFormatException e) {}
		int gameMargin = score - opponentScore;
		int index = close.indexOf("<");
		double closeValue = 0.0;
		if(index!=-1) {
		String val = close.substring(0, index);
			closeValue = Double.parseDouble(val);
		}else {
			closeValue = Double.parseDouble(close);
		}
		double coverMargin = gameMargin+closeValue;
		object.setSideCoverMargin(coverMargin+"");
		
		String ats = "push";
		if(coverMargin<0)
			ats = "L";
		else if(coverMargin>0)
			ats = "W";
		
		object.setAtsResult(ats);
		if(sport.equals("nhl"))
			object.setRunLineResult(ats);
		
		int total_points = score + opponentScore;
		object.setTotalPoints(total_points+"");
		
		index = totalClose.indexOf("<");
		double totalcloseValue = 0.0;
		if(index!=-1) {
			String val = totalClose.substring(0, index);
		
			totalcloseValue = Double.parseDouble(val);
		}else {
			totalcloseValue = Double.parseDouble(totalClose);
		}
		
		double totalcover = total_points - totalcloseValue;
		object.setClosingTotal(totalcloseValue+"");
		
		String overunder ="";
		if(totalcover<0)
			overunder="U";
		else
			overunder="O";
		
		object.setOverUnder(overunder+"");
		object.setTotalCoverMargin(totalcover+"");
		//System.out.println(score+", "+opponentScore+", "+gameMargin+", "+coverMargin+", "+total_points+", "+totalcover+", "+overunder+ats);
		return object;
	}
	
	
	//http://localhost:9090/playersinfo/info/v1/categories/nba
	@GetMapping("/categories/{sport}")
	public List<TeamCategory> getSportCategories(@PathVariable("sport") String sport){
		logger.info("Entering getSportCategories");
		
		List<TeamCategory> list = teamsCatRepo.findBySport(sport, Sort.by("category"));
		
		
		logger.info("Exiting getSportCategories");
		
		return list;
	}
	
	@GetMapping("/getteamdata/{team}")
	public List<TeamCategory> getTeamsData(@PathVariable("sport") String sport){
		logger.info("Entering getTeamsData");
		
		List<TeamCategory> list = teamsCatRepo.findBySport(sport, Sort.by("category"));
		
		
		logger.info("Exiting getTeamsData");
		
		return list;
	}
	
	@GetMapping("/categorywise/{sport}")
	public Map<String, List<TeamList>> getCategorywiseTeams(@PathVariable("sport")String sport){
		logger.info("Entering getCategorywiseTeams");
		 Map<String, List<TeamList>> map = new HashMap<String, List<TeamList>>();
		Sort sort = Sort.by(Direction.ASC, "category");
		Sort sortbyname = Sort.by(Direction.ASC, "name");
		List<TeamCategory> categories = teamCatRepo.findBySport(sport, sort);
		
		for(TeamCategory teamCategory : categories) {
			String category = teamCategory.getCategory();
			List<Team> teams = teamsRepo.findBySportCategory(sport,category,sortbyname);
			List<TeamList> teamList = convrtInTeamList(teams);
			map.put(category, teamList);
		}
		
		logger.info("Exiting getCategorywiseTeams");
		return map;
	}
	
	private List<TeamList> convrtInTeamList(List<Team> teamList){
		List<TeamList> teamListRes = new ArrayList<TeamList>();
		teamList.forEach(team->{
			TeamList teamObj = new TeamList();
			teamObj.setName(team.getName());
			teamObj.setFrontName(team.getFrontName());
			teamObj.setSport(team.getSport());
			teamObj.setSubCategory(team.getSubCategory());
			teamObj.setTeamcategory(team.getTeamcategory());
			teamObj.setTeamCode(team.getTeamCode());
			teamObj.setTeamNo(team.getTeamNo());
			teamObj.setAncherTeam("<a class='link' href='javascript:loaddata(\""+team.getSport()+"\",\""+team.getName()+"\")'>"+team.getFrontName()+"</a>");
			teamListRes.add(teamObj);
		});
		return teamListRes;
		
	}
}
