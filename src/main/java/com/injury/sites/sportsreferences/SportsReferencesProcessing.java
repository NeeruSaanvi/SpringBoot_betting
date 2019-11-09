package com.injury.sites.sportsreferences;

import java.text.Normalizer;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.function.Function;

import org.apache.http.NameValuePair;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;

import com.injury.dto.GamePlayed;
import com.injury.dto.TeamData;
import com.injury.entity.Player;
import com.injury.entity.Team;
import com.injury.entity.TeamCategory;
import com.injury.sites.SiteProcessor;

/**
* @author SureshKoumar @ Pinesucceed
* 28-Jun-2019
*/
public class SportsReferencesProcessing extends SiteProcessor{

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	protected static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
	public static SportsReferencesParser parser = null;
	
	private static String[][] links = {
			{"nfl", "https://www.pro-football-reference.com", "Football"},
			{"ncaaf", "https://www.sports-reference.com/cfb", "College_Football"},
			{"nba", "https://www.basketball-reference.com", "Basketball"},
			{"ncaab", "https://www.sports-reference.com/cbb/", "College_Basketball"},
			{"mlb", "https://www.baseball-reference.com", "Baseball"},
			{"nhl", "https://www.hockey-reference.com", "Hockey"},
	};
	
	public SportsReferencesProcessing() {
		parser = new SportsReferencesParser(httpClientWrapper);
	}
	
	@Override
	public void saveCurrentData() {
	}

	@Override
	public void updatePlayerData(String sport) {
		logger.info("Entering updatePlayerData");
		
		boolean update = false;
		
		//find the link of game
		String url = "";
		for(var i = 0; i<links.length;i++) {
			String sportName = links[i][0];
			if(sportName.equals(sport)) {
				url = links[i][1];
				break;
			}
		}
		
		if(sport.equals("nba") || sport.equals("nhl") || sport.equals("ncaab")) {
			nba_Ncaab_NhlProcessing(update, url, sport, "2019");
		} else if(sport.equals("nfl") || sport.equals("ncaaf") || sport.equals("mlb")) {
			String year = "2019";
			if(sport.equals("nfl") || sport.equals("ncaaf") )
				year = "2018";
			nfl_Ncaaf_MlbProcessing(update, url, sport, year);
		}
			logger.info("Exiting updatePlayerData");
	}
	
	private void nfl_Ncaaf_MlbProcessing(boolean update, String url, String sport, String year) {
		//get header
		List<NameValuePair> headerValuePairs = getHeader();
		try {
			if(sport.equals("nfl")) {
				playerRepo.deleteBySportName(sport);
				String href = url+"/years/"+year+"/";
						
				String html = super.httpClientWrapper.getSiteNoRedirect(href, headerValuePairs);
	 			logger.debug("HTML: "+html);
				Document doc = parser.parseXhtml(html);
				
				Element american = doc.getElementById("AFC");
				Element national = doc.getElementById("NFC");
				/**
				 * code to update the team category
				 */
				if(update) {
					List<TeamCategory> category1 = parser.parseTable(american, sport);
					List<TeamCategory> category2 = parser.parseTable(national, sport);
					dataProcessor.updateCategory(category1, sport);
					dataProcessor.updateCategory(category2, sport);
				}
				
				//collect the teams link
				Map<String,String> list1 = parser.getTeamLinks(american, sport);
				Map<String,String> list2 = parser.getTeamLinks(national, sport);
				list1.putAll(list2);
			
				List<Team> teams = teamsRepo.findBySport(sport, Sort.by("teamName"));
				boolean changed = false;
				for(Entry<String, String> entry : list1.entrySet()) {
					String link = entry.getKey();
					String teamName = entry.getValue();
					Team team = findTeam(teams,teamName);
					String code[] = link.split("/");
					if(team!=null) { 
						team.setTeamCode(code[2]);
						changed = true;
					}
				}
				
				if(changed)
					teamsRepo.saveAll(teams);
				//code to update the players list
				//if(update) {
					List<Player> playersList = parser.getPlayersList(list1, url, headerValuePairs, sport, httpClientWrapper,year);
					playerRepo.saveAll(playersList);
					logger.info("---------------> Players Saved <----------------");
				//}
				
				//for updating players details
				for(Entry<String, String> entry : list1.entrySet()) {
					String link = entry.getKey();
					String teamName = entry.getValue();
					
					href = url+link;
					html = super.httpClientWrapper.getSiteNoRedirect(href, headerValuePairs);
					logger.debug("Players list: "+html);
					
					Document document =  parser.parseXhtml(html);
					Map<String, String> playersLink = parser.getPlayersLink(document, sport);
					
					Team team = findTeam(teams,teamName);
					//code for updating players details
					parser.updateNFLPlayersData(playersLink, url, httpClientWrapper, headerValuePairs, sport, teamName, dataProcessor,year,team);
					logger.debug("update...");
					///break;
				}
			} else if(sport.equals("ncaaf")){
				try {
					String href = "";
					if(sport.equals("ncaaf"))
						href = url+"/years/"+year+".html";
					
					String html = super.httpClientWrapper.getSiteNoRedirect(href, headerValuePairs);
		 			logger.debug("HTML: "+html);
					Document doc = parser.parseXhtml(html);
					
					Element conferences = doc.getElementById("conferences");
					Map<String, String> conferencesLink = parser.getConferences(conferences);
					List<TeamCategory> categoryList = new ArrayList<TeamCategory>();
					Map<String, Map<String,String>> confteamlinks = new LinkedHashMap<>();
					parser.getCategoryTeamsLins(conferencesLink, categoryList, confteamlinks, sport, url, httpClientWrapper, headerValuePairs);
					
					logger.debug("Category List: "+categoryList);
					logger.debug("Conference Links"+conferencesLink.toString());
					logger.debug("Conference Teams Link: "+confteamlinks.toString());
					// Update Category list
					if(update) {
						dataProcessor.updateCategory(categoryList, sport);
					}
					
					//code to update the players list
					for(Entry<String, Map<String,String>> entry : confteamlinks.entrySet()) {
						String conference = entry.getKey();
						
						//update players list
						Map<String,String> links = entry.getValue();
					//	if(update) {
							List<Player> playersList = parser.getPlayersList(links, url, headerValuePairs, sport, httpClientWrapper,year);
							logger.debug(playersList.toString());
							if(update)
							playerRepo.saveAll(playersList);
							logger.info("---------------> Players Saved <----------------");
					//	}
							
						//code for updating players details
						parser.updatePlayersData(links, url, httpClientWrapper, headerValuePairs, sport, conference, dataProcessor);
					}
					
					
				}catch(Exception e) {
					e.printStackTrace();
				}
			} else if(sport.equals("mlb")){
				
				String seasonUrl = url+"/leagues/"+sport.toUpperCase()+"/"+year+"-standings.shtml";
				
				String html = super.httpClientWrapper.getSiteNoRedirect(seasonUrl, headerValuePairs);
				logger.debug("HTML: "+html);
				
				if(update) {
					List<TeamCategory> categories = parser.getTeamCategoryList(html, sport);
					dataProcessor.updateCategory(categories, sport);
					logger.debug(categories.toString());
				}

				Map<String, String> teamLinks = parser.getTeamlinks(html,sport);
				
				//for updating players details
				for(Entry<String, String> entry : teamLinks.entrySet()) {
					String link = entry.getKey();
					String teamName = entry.getValue();
					
					String href = url+link;
					html = super.httpClientWrapper.getSiteNoRedirect(href, headerValuePairs);
					logger.debug("Players list: "+html);
					
					Map<String, String> playerLinks = parser.getPlayersLink(html);
					logger.debug(playerLinks.toString());
					//code to update the players list
					//if(update) {
						List<Player> playersList = parser.getPlayersList(teamLinks, url, headerValuePairs, sport, httpClientWrapper,year);
						playerRepo.saveAll(playersList);
						logger.info("---------------> Players Saved <----------------");
					//}
					
					//String year = "2018";
					for(Entry<String,String> links : playerLinks.entrySet()) {
						String playerlink = links.getKey();
						String  playerName = links.getValue();
						logger.debug("link : "+playerlink+" playerName: "+playerName);
						if(playerName.length()==0) continue;
						int index = playerlink.lastIndexOf("/");
						String pname = playerlink.substring(index+1, playerlink.length()-6);
						
						String types[] = {"b","p"};
						for(String t : types) {
						href = url+"/players/gl.fcgi?id="+pname+"&t="+t+"&year="+year;
						//https://www.baseball-reference.com/players/gl.fcgi?id=chapmar01&t=b&year=2019
						html = super.httpClientWrapper.getSiteNoRedirect(href, headerValuePairs);
						logger.debug("Players data-page: "+html);
							
							Set<GamePlayed> games=null;
							try {
									games = parser.getMLBPlayerInfo(html, sport, year,t);	
							}catch(Exception e) {
								logger.debug(e.toString());
								logger.debug("Error Game");
								logger.debug("html: "+html);
								logger.debug("link: "+href);
								continue;
							}
							
						if(games==null) {
							logger.debug("html: "+html);
							logger.debug("link: "+href);
							continue;
						}
							
						if(games.size()>0) {
							try {
								dataProcessor.updatePlayerData(sport, teamName, playerName, games);
							}catch(Exception e) {
								logger.error(e.getMessage());
							}
						}else {
							logger.debug("html: "+html);
							logger.debug("link: "+href);
							continue;
							//System.exit(0);
						}
							
					}//battinhg and pitching
					}
				}
			}
			
		}catch(Exception e) {
			e.printStackTrace();
			logger.error(e.toString());
			logger.error("Unable to process for sport: "+sport);
		}
	}
	
	private Team findTeam(List<Team> teams, String teamName) {
		
		for(Team team : teams) {
			if(team.getName().equals(teamName))
				return team;
		}
		
		return null;
	}
	
	private void nba_Ncaab_NhlProcessing(boolean update, String url, String sport, String year) {
		
		//get header
		List<NameValuePair> headerValuePairs = getHeader();
		try {
			String html = super.httpClientWrapper.getSiteNoRedirect(url, headerValuePairs);
			logger.debug("HTML: "+html);
			Document doc = parser.parseXhtml(html);
			
			/**
			 * Code block to update the teams list with their short-form
			 */
			if((sport.equals("nba") || sport.equals("nhl")) && update==true) {
				//read teams data
				Map<String, String> teamsMap = parser.parseTeamsList(doc);
				logger.debug(teamsMap.toString());
				//update teams data
				dataProcessor.updateTeams(teamsMap, sport);
			}
			
			String seasonUrl = "";
			if(sport.equals("ncaab")) {
				seasonUrl = url+"seasons/"+year+".html";	
			}else
				seasonUrl = url+"/leagues/"+sport.toUpperCase()+"_"+year+".html";
			
			html = super.httpClientWrapper.getSiteNoRedirect(seasonUrl, headerValuePairs);
			logger.debug("HTML: "+html);
			doc = parser.parseXhtml(html);
			String diva="", divb="";
			if(sport.equals("nhl") || sport.equals("nba")) {
				if(sport.equals("nhl")) {
					diva = "all_standings_EAS";
					divb = "all_standings_WES";
				} else {
					diva="divs_standings_E";
					divb="divs_standings_W";
				}
				
				Element estern = doc.getElementById(diva);
				Element western = doc.getElementById(divb);
				
				/**
				 * code to update the team category
				 */
				if(update) {
					List<TeamCategory> category1 = parser.parseTable(estern, sport);
					List<TeamCategory> category2 = parser.parseTable(western, sport);
					dataProcessor.updateCategory(category1, sport);
					dataProcessor.updateCategory(category2, sport);
				}
				
				//collect the teams link
				Map<String,String> list1 = parser.getTeamLinks(estern, sport);
				Map<String,String> list2 = parser.getTeamLinks(western, sport);
				list1.putAll(list2);
			
				//code to update the players list
					List<Player> playersList = parser.getPlayersList(list1, url, headerValuePairs, sport, httpClientWrapper,year);
					logger.debug(playersList.toString());
					playerRepo.saveAll(playersList);
					logger.info("---------------> Players Saved <----------------");
					
					//Orating and Drating of Team
				//for updating players details
				for(Entry<String, String> entry : list1.entrySet()) {
					String link = entry.getKey();
					String teamName = entry.getValue();
					//teamName = capitalize(teamName);
					String href = url+link;
					html = super.httpClientWrapper.getSiteNoRedirect(href, headerValuePairs);
					logger.debug("Players list: "+html);
					
					Document document = null;
					/*if(sport.equals("nba")) {
						String table = parser.searchCommentTable(html, "Per Game Table");
						document = parser.parseXhtml(table);
					}else {*/
						document = parser.parseXhtml(html);
					//}
					Map<String, String> playersLink = parser.getPlayersLink(document, sport);
					
					for(Entry<String,String> links : playersLink.entrySet()) {
						String playerName = links.getKey();
						String playerlink = links.getValue();
						//playerName = capitalize(playerName);
						
						if(playerName.length()==0) continue;
						
						href = url+playerlink;
						int index = href.lastIndexOf(".");
						href = href.substring(0, index);
						href = href + "/gamelog/"+year+"/"; 
						html = super.httpClientWrapper.getSiteNoRedirect(href, headerValuePairs);
						logger.debug("Players data-page: "+html);
						document = parser.parseXhtml(html);
						Set<GamePlayed> games = parser.getPlayerInfo(document, sport);
						if(games==null) {
							logger.error("Exception in parsing games "+html);
							throw new Exception("");
						}
						if(sport.equals("nba")) {
							href = href.replace("gamelog", "gamelog-advanced"); 
							html = super.httpClientWrapper.getSiteNoRedirect(href, headerValuePairs);
							logger.debug("Players advance-data-page: "+html);
							document = parser.parseXhtml(html);
							parser.setPlayerRating(document, games, sport);
						}
						
						logger.debug(games.toString()+"");
						//if((playerName.contains("Koekkoek")  ) && teamName.endsWith("Tampa Bay Lightning"))
						dataProcessor.updatePlayerData(sport, teamName, playerName, games);

					}
			}
				
		} else if(sport.equals("ncaab")) {
			Element conferences = doc.getElementById("conference-summary");
			Map<String, String> conferencesLink = parser.getConferences(conferences);
			List<TeamCategory> categoryList = new ArrayList<TeamCategory>();
			Map<String, Map<String,String>> confteamlinks = new LinkedHashMap<>();
			
			parser.getCategoryTeamsLins(conferencesLink, categoryList, confteamlinks, sport, url, httpClientWrapper, headerValuePairs);
			
			logger.debug("Category List: "+categoryList);
			logger.debug("Conference Links"+conferencesLink.toString());
			logger.debug("Conference Teams Link: "+confteamlinks.toString());
			
			// Update Category list
			if(update) {
				dataProcessor.updateCategory(categoryList, sport);
			}
			
			//code to update the players list
			for(Entry<String, Map<String,String>> entry : confteamlinks.entrySet()) {
				String conference = entry.getKey();
				
				//update players list
				Map<String,String> links = entry.getValue();
				//if(update) {
					List<Player> playersList = parser.getPlayersList(links, url, headerValuePairs, sport, httpClientWrapper,year);
					logger.debug(playersList.toString());
					//if(update)
					playerRepo.saveAll(playersList);
					System.out.println(playersList);
					logger.info("---------------> Players Saved <----------------");
				//}
				//code for updating players details
				parser.updatePlayersData(links, url, httpClientWrapper, headerValuePairs, sport, conference, dataProcessor);
				System.out.println();
			}
		
		}
	
	} catch(Exception e) {
		logger.error("error while parsing");
		e.printStackTrace();
	}
	}

	@Override
	public List<TeamData> getTeamData(String sportName) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
