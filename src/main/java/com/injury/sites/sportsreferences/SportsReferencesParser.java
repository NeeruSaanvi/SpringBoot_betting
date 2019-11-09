package com.injury.sites.sportsreferences;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.text.Normalizer;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.Map.Entry;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.json.JSONArray;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.injury.dto.GamePlayed;
import com.injury.entity.Player;
import com.injury.entity.Team;
import com.injury.entity.TeamCategory;
import com.injury.entity.TeamContainer;
import com.injury.errorhandling.BatchException;
import com.injury.httpclient.HttpClientWrapper;
import com.injury.sites.BaseParser;
import com.injury.sites.InjuryDataProcessor;

/**
* @author SureshKoumar @ Pinesucceed
* 28-Jun-2019
*/
public class SportsReferencesParser extends BaseParser{
	private static Logger logger = LoggerFactory.getLogger("SportsReferencesParser");
	@SuppressWarnings("unused")
	private HttpClient client = null;
	
	public SportsReferencesParser(HttpClientWrapper httpClientWrapper) {
		client = httpClientWrapper.getClient();
	}
	
	
	public void setPosition(List<Player> playersList, String url, HttpClientWrapper httpClientWrapper,
			List<NameValuePair> headerValuePairs, String sport) throws BatchException {
		logger.debug("Players List: "+playersList);
		
		for(Player player : playersList) {
			String playerName = player.getPlayerName();
			boolean repeatsearch = true;
			int index = 0;
			String tempstr[] = playerName.split(" ");
			logger.debug("Searching for: "+ playerName);
			while(repeatsearch) {
				
				char ch = 'a';
				if(index+1<=tempstr.length)
					ch = tempstr[index++].charAt(0);
				else break;
				
				String uri = url+"/players/"+Character.toLowerCase(ch)+"/";
				String playersPage = httpClientWrapper.getSiteNoRedirect(uri, headerValuePairs);
				Document doc = parseXhtml(playersPage);
				Element table = doc.getElementById("players");
				Elements trs = table.select("tbody tr");
				for(Element tr : trs) {
					Elements tds = tr.children();
					String searchName = tds.get(0).text(); //Name
					if(searchName.equals(playerName)) {
						String to = tds.get(2).text(); //To
						int yr = Integer.parseInt(to);
						LocalDate dt = LocalDate.now();
						int currentyear = dt.getYear();
						if(yr<=currentyear && yr >= (currentyear-6)) {
							String pos = tds.get(3).text(); //pos
							player.setPosition(pos);
							repeatsearch= false ; break;
						}
					}
				}
			}//while loop
		}
	}
	
	public void updateNFLPlayersData(Map<String,String> links, String url, HttpClientWrapper httpClientWrapper,
			List<NameValuePair> headerValuePairs, String sport, String teamName, InjuryDataProcessor dataProcessor,String year, Team team) throws BatchException {
		for(Entry<String,String> playerlinks : links.entrySet()) {
			String playerName = playerlinks.getKey();
			String playerlink = playerlinks.getValue();
			
			if(playerName.length()==0) continue;
			
			String href = url+playerlink.substring(0,playerlink.length()-4)+"/gamelog/"+year+"/"; 
			String html = httpClientWrapper.getSiteNoRedirect(href, headerValuePairs);
			logger.debug("Players data-pages: "+html);
			Document document = parseXhtml(html);
				System.out.println(html);
			Player player = dataProcessor.getPlayer(sport, teamName, playerName);
			
			Set<GamePlayed> games = getNFLPlayerInfo(document, httpClientWrapper,headerValuePairs,sport,teamName,playerName,player.getGames(),team);

			logger.debug(games.toString()+"");
			dataProcessor.updatePlayerData(sport, teamName, playerName, games);

		}
	}
	
	public void updatePlayersData(Map<String,String> links, String url, HttpClientWrapper httpClientWrapper,
			List<NameValuePair> headerValuePairs, String sport, String conferenceName, InjuryDataProcessor dataProcessor) throws BatchException {
		
		TeamContainer tc = new TeamContainer("vegasinsider", sport);
		List<String> UITeams = tc.getTeamsList();
		Collections.sort(UITeams);
		
		for(Entry<String,String> playerlinks : links.entrySet()) {
			String teamName = playerlinks.getValue();
			String playerlink = playerlinks.getKey();
			
			if(teamName.length()==0) continue;
		
			String href = "";
			if(sport.equals("ncaaf"))
				href = url.substring(0,url.length()-4)+playerlink;
			else 
				href = url.substring(0,url.length()-5)+playerlink;
			String html = httpClientWrapper.getSiteNoRedirect(href, headerValuePairs);
			//System.out.println(html);
			if(sport.equals("ncaab")){
				Document document = parseXhtml(html);
				teamName = document.select("#info div div h1 span ").get(1).text();
				teamName = getMappingTeamName(teamName, sport, UITeams);
				System.out.println();
			}
			Map<String, String> playersLinks = extractPlayersName(html, sport);
			
			for(Entry<String, String> playerEntry : playersLinks.entrySet()) {
				String plink = playerEntry.getKey();
				String playerName = playerEntry.getValue();
				if(sport.equals("ncaaf"))
					href = url.substring(0, url.length()-4) + plink;
				else
					href = url.substring(0, url.length()-5) + plink;
				
				int index = href.lastIndexOf(".");
				href = href.substring(0, index);
				if(sport.equals("ncaaf"))
					href = href + "/gamelog/2018/";
				else
					href = href + "/gamelog/2019";
				html = httpClientWrapper.getSiteNoRedirect(href, headerValuePairs);
				logger.debug("Players data-pages: "+html);
				System.out.println(html);
				Document document = parseXhtml(html);
				Set<GamePlayed> games = getPlayerInfo(document, sport);
				
				if(!sport.equals("ncaaf") && !sport.equals("ncaab")) {
					href = href.replace("gamelogs", "gamelogs-advanced"); 
					html = httpClientWrapper.getSiteNoRedirect(href, headerValuePairs);
					logger.debug("Players advance-data-page: "+html);
					document = parseXhtml(html);
					setPlayerRating(document, games, sport);
				}
				dataProcessor.updatePlayerData(sport, teamName, playerName, games);
				
			}
		}
	}
	
	
	private Map<String, String> extractPlayersName(String html, String sport) throws BatchException{
		Map<String, String> map = new LinkedHashMap<>();
		Document doc = parseXhtml(html);
		Element table = doc.getElementById("per_game");
		Elements trs = null;
		if(sport.equals("ncaaf"))
			trs = getTrsOfTable(html);
		else
			trs = table.select("tbody tr");
		
		for(Element tr : trs) {
			Elements tds = tr.children();
			String name = tds.get(1).select("a").text();
			String link = tds.get(1).select("a").attr("href");
			
			map.put(link, name);
		}
		return map;
	}
	
	private Elements getTrsOfTable(String html) throws BatchException {
		Document doc = parseXhtml(html);
		
		String table = searchCommentTable(html, "rushing_and_receiving");
		html = doc.toString().replace(table, "");
		
		table += searchCommentTable(html, "returns");
		html = html.replace(table, "");
		
		table += searchCommentTable(html, "kicking");
		html = html.replace(table, "");
		
		table += searchCommentTable(html, "defense");
		html = html.replace(table, "");
		
		Document document = parseXhtml(table);				
		Elements trs = document.select("tbody tr");
		
		return trs;
	}
	
	public void getCategoryTeamsLins(Map<String, String> conferencesLink, List<TeamCategory> categoryList, 
			Map<String, Map<String,String>> confteamlinks, String sport, String url, HttpClientWrapper httpClientWrapper,
			List<NameValuePair> headerValuePairs) throws BatchException{
		
		
		for(Entry<String, String> entry : conferencesLink.entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue();
			
			if(key.length()==0) continue;
			
			TeamCategory teamCategory = new TeamCategory(sport, key, "");
			Set<Team> teams = teamCategory.getTeams();
			categoryList.add(teamCategory);
			
			String href = "";
			if(sport.equals("ncaaf"))
				href = url.substring(0,url.length()-4)+value;
			else
				href = url.substring(0,url.length()-5)+value;
					
			String html = httpClientWrapper.getSiteNoRedirect(href, headerValuePairs);
			logger.debug("Players list: "+html);
			Document doc = parseXhtml(html);
			Element table = doc.getElementById("standings");
			Map<String,String> links = getTeamsLink(table, sport);
			/**
			 * Below block updates the team category
			 */
			for(Entry<String, String> teamEntry : links.entrySet() ) {
				String teamName = teamEntry.getValue();
				if(teamName.length()==0) continue;
				Team team = new Team(teamName, sport);
				team.setTeamNo(0);
				team.setTeamcategory(key);
				team.setSubCategory("");
				
				if(teams!=null) {
					teams.add(team);
				}
			}
			confteamlinks.put(key, links);
		}
	}
	
	
	public List<Player> getPlayersList(Map<String,String>list1, String url, List<NameValuePair> headerValuePairs,
			String sport, HttpClientWrapper httpClientWrapper,String year) throws BatchException{
		List<Player> playersList = new ArrayList<>();
		
		TeamContainer tc = new TeamContainer("vegasinsider", sport);
		List<String> UITeams = tc.getTeamsList();
		Collections.sort(UITeams);
		
		//for adding players list
		String html = "";
		for(Entry<String, String> entry : list1.entrySet()) {
			String link = entry.getKey();
			String teamName = entry.getValue();
			
			if(link.length()==0 || teamName.length()==0) continue;
			
			String href = "";
			
			if(sport.equals("ncaab")) {
				href = url.substring(0,url.length()-5)+link;
			} else if(sport.equals("ncaaf")){
				href = url.substring(0,url.length()-4)+link;
			} else {
				href = url+link;
			}
			html = httpClientWrapper.getSiteNoRedirect(href, headerValuePairs);	
			logger.debug("Players list: "+html);
			System.out.println(html);
			//System.out.println(html);
			Document document = null;			
			if(sport.equals("nba")) {
				//String table = searchCommentTable(html, "Per Game Table");
				//document = parseXhtml(table);
				document = parseXhtml(html);
			} else if(sport.equals("mlb")){
				String table = searchCommentTable(html, "appearances");
				document = parseXhtml(table);
			} else if(sport.equals("ncaab")){
				document = parseXhtml(html);
				teamName = document.select("#info div div h1 span ").get(1).text();
				teamName = getMappingTeamName(teamName, sport, UITeams);
				logger.debug("TeamName "+teamName);
			} else {
				document = parseXhtml(html);
			}
			logger.debug(html);
			
			try {
				List<Player> players = null;//getPlayers(document, sport, teamName);
				
				if(sport.equals("mlb"))
					players = getMLBPlayers(document, sport, teamName);
				else {
					players = getPlayers(document, sport, teamName);
					logger.debug("Player: "+players+"");
		
					if(sport.equals("nba") || sport.equals("ncaab")) {
						String htmlTablle = searchCommentTable(html, "per_game");
						System.out.println(htmlTablle);
						Document docmt = parseXhtml(htmlTablle);
						players = parseRpgPpgAndApg(docmt, players, sport, teamName);
						
						String advancehtml = searchCommentTable(html, "div_advanced");
						players = parsePlayerEfficiencyRating(advancehtml, players, sport, teamName);
					}
					
					BiFunction<Player, Set<GamePlayed>,Player> setgames = (player,games) ->{ player.setGames(games); return player;};
					
					logger.debug("Before "+players);
					if(sport.equals("nfl")) {
						 Set<GamePlayed> games = parseNFLWeeks(html,year);
						 for(Player player : players) {
							 setgames.apply(player, games);
						 }
					}
					logger.debug("After "+players);
				}
				
				playersList.addAll(players);
			}catch(Exception e){
				logger.error("Error updating players list: "+e);
			}
		}
		return playersList;
	}
	
	
	private String getMappingTeamName(String oldName, String sport,List<String> UITeams) {
		
		for(String smteam : UITeams) {
			String chkstr="";
			String temp[] = smteam.split(",");
			if(sport.equals("ncaab") || sport.equals("ncaaf")) {
				try{chkstr = temp[1];}catch(Exception e) {
					//System.out.println("Error: "+smteam);
				}
			}
			if(oldName.equals(chkstr))
				return temp[0];
		}
		
		return "";
	}
	private Set<GamePlayed> parseNFLWeeks(String html, String year) {
		
		Document tempdoc = Jsoup.parse(html);
		Element schedules = tempdoc.getElementById("div_games");
		Elements trs = schedules.select("tr");
		Set<GamePlayed> games = new LinkedHashSet<GamePlayed>();
		
		GamePlayed previousgame=null;
		for(Element tr : trs) {
			Elements tds = tr.children();
			GamePlayed game = new GamePlayed();
			boolean valuset = false;
			
			for(Element td : tds) {
				String datastat = td.attr("data-stat");
				
				//week no
				if(datastat.equals("week_num")) {
					String week = td.text();
					try {
							//test line code - to break the extra looping
							int num = Integer.parseInt(week);
							game.setWeek("Week-"+week);
							valuset = true;
					}catch(NumberFormatException nfe) {
						break;
					}
				}
				
				//Day of week
				if(datastat.equals("game_day_of_week")) {
					String day = td.text();
					if(day.length()>0) {
						game.setDay(day);
					} else {
						game.setDay("Bye Week");
					}
				}
				
				//Game Date
				if(datastat.equals("game_date")) {
					//September 9
					String datestr = td.text();
					if(datestr.equals("Playoffs")) {
						game.setGameDate(previousgame.getGameDate().plusDays(7));
						break;
					}
					else {
						if(datestr.length()>0) {
							int yearNo = Integer.parseInt(year);
							LocalDate date = parseDate(datestr, yearNo);
							game.setGameDate(date);
						} else {
							game.setGameDate(previousgame.getGameDate().plusDays(1));
						}
					}
				}
			}
			if(valuset) {
				previousgame = game;
				games.add(game);
			}
		}
		
		return games;
	}
	
	private LocalDate parseDate(String str, int year) {
		LocalDate date = null;
		try {
			String temp[] = str.split(" ");
			int month= getMonth(temp[0]);
			int day = Integer.parseInt(temp[1]);
			date = LocalDate.of(year, month, day);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return date;
	}
	
	
	private List<Player> parsePlayerEfficiencyRating(String htmlTablle,List<Player> playersList, String sportName, String teamName) throws BatchException {
		Document doc = parseXhtml(htmlTablle);
		List<Player> updateplayerList = new ArrayList<Player>();
		if (doc != null) {
			Elements trs = doc.select("table tbody tr");
			for (Element tr : trs) {
				String key = "";
				Player player = null;
				//new JSONArray();
				for (Element td : tr.select("td")) {
					if ((td.attr("data-stat") != null && td.attr("data-stat").equalsIgnoreCase("player"))) {
						if (td.select("a") != null) {
							key = td.select("a").html();
							player = findPlayer(playersList, key);
							//player = playerRepo.findPlayer(sportName, teamName, key);
							/*
							 * String id = (sportName + teamName + key).replaceAll(" ", "").trim(); if
							 * (player == null) { Player player1 = new Player(); player1.setId(id);
							 * player1.setSportName(sportName); player1.setTeamName(teamName);
							 * player1.setPlayerName(key); }
							 */
						}
					}

					if (player!=null && td.attr("data-stat").equalsIgnoreCase("per")) {
						player.setPer(td.html().trim());
					}
				}
				if (player != null) {
					updateplayerList.add(player);
				}
			}
		}
		return updateplayerList;
	}
	
	private List<Player> parseRpgPpgAndApg(Document doc, List<Player> playersList, String sportName, String teamName) {
		List<Player> updateList = new ArrayList<Player>();
		if (doc != null) {
			Elements trs = doc.select("tbody tr");
			for (Element tr : trs) {
				String key = "";
				Player player = null;
				for (Element td : tr.select("td")) {
					if ((td.attr("data-stat") != null && td.attr("data-stat").equalsIgnoreCase("player"))) {
						if (td.select("a") != null) {
							key = td.select("a").text();
							//if(key.contains("Dario "))
							player = findPlayer(playersList, key);
							//String id = (sportName+teamName+key).replaceAll(" ", "").trim();
							//if(player == null) {
							/*Player	player1 = new Player();
								player1.setId(id);
								player1.setSportName(sportName);
								player1.setTeamName(teamName);
								player1.setPlayerName(key);
								noFoundPlayers.add(player1);*/
								//noFoundPlayers.add(teamName);
							//	noFoundPlayers.add(key);
								 
							//}
						}
					}
					if (player!=null && td!=null && td.attr("data-stat").equalsIgnoreCase("trb_per_g")) {
						player.setRpg(td.html().trim());
						//JSONObject local = new JSONObject();
						//local.put("RPG", td.html());
						//array.put(local);
					}
					if (player!=null && td != null) {
						if (td.attr("data-stat").equalsIgnoreCase("ast_per_g")) {
							player.setApg(td.html().trim());
							/*JSONObject local = new JSONObject();
							local.put("APG", td.html());
							array.put(local);*/
						}
					}
					if (player!=null && td != null) {

						if (td.attr("data-stat").equalsIgnoreCase("pts_per_g")) {
							player.setPpg(td.html().trim());
							/*JSONObject local = new JSONObject();
							local.put("PPG", td.html());
							array.put(local);*/
						}
					}
				}
				if (player != null) {
					updateList.add(player);
				}
			}
		}
		return updateList;
	}

	private Player findPlayer(List<Player> playersList, String playerName) {
		Player player = null;
		
		//String asciiName = ascii.apply(playerName);
		for(Player p : playersList) {
			if(p.getPlayerName().equals(playerName))
				return p;
		}
		
		return player;
	}
	
	private List<Player> getMLBPlayers(Document document, String sport, String teamName) {
		
		List<Player> playersList = new ArrayList<>();
		
		Elements trs = document.select("tbody tr");
		for(Element tr : trs) {
			Elements tds = tr.children();
			String playerName = tds.get(0).text();
			//String pos = tds.get(tds.size()-1).text();
			Player player = new Player(sport, teamName, playerName);
			if(!playersList.contains(player))
				playersList.add(player);
		}
		
		return playersList;
	}
	
	public Map<String, String> getTeamsLink(Element table, String sport){
		logger.info("Entering getTeamLinks");
		
		Map<String,String> map = new LinkedHashMap<>();
		Elements trs = table.select("tbody tr");
		for(Element tr : trs) {
			Elements tds = tr.children();
			Elements a = null;
			if(sport.equals("ncaaf"))
				a = tds.get(0).getElementsByTag("a");
			else
				a = tds.get(1).getElementsByTag("a");
			String link = a.attr("href");
			String name = a.text();
			
			if(link.length() > 0 && name.length() > 0)
				map.put(link,name);
			
		}
		
		logger.info("Exiting getTeamLinks");
		return map;
	}
	
	public Map<String, String> getConferences(Element conferences){
		logger.info("Entering getConferences");
		
		Map<String,String> map = new LinkedHashMap<>();
		
		Elements trs = conferences.select("tbody tr");
		for(Element tr : trs) {
			Elements tds = tr.children();
			Element el = tds.get(1);
			String link = el.select("a").attr("href");
			String name = el.text();
			map.put(name, link);
		}
		
		logger.info("Exiting getConferences");
		return map;
	}
	
	public Set<GamePlayed>  setPlayerRating(Document doc, Set<GamePlayed> games, String sport) {
		logger.info("Entering getPlayerRating");
		
		Elements elements = doc.select(".table_outer_container table");
		Elements trs = elements.select("tbody tr");
		for(Element tr : trs) {
			String className = tr.attr("class");
			if(!className.equals("thead") && !className.contains("over_header")) {
				Elements columns = tr.children();
				
				String text  = "";
				Element column =null;
				if(sport.equals("ncaab")) {
					column = columns.get(1);
					text = column.text();
				}else {
					column = columns.get(2);
					text = column.text();
				}
				//date
				LocalDate gameDate = parseDate(text);
				GamePlayed gamePlayed = null;
				for(GamePlayed gm : games){
				
					LocalDate tempDate = gm.getGameDate();
					if(tempDate.equals(gameDate)) {
						gamePlayed = gm;
						break;
					}
				}
				
				int oIndex = 0;
				int dIndex = 0;
				
				if(sport.equals("ncaab")) {
					oIndex = 7;
					dIndex = 8;
				}else {
					oIndex = 20;
					dIndex = 21;
				}
				
				boolean allow = false;
				if(sport.equals("ncaab")) {
					allow = true;
				}else {
					allow = columns.size()>9;
				}
				
				if(allow) {
					column = columns.get(oIndex);
					String oRating = column.text();
					if(sport.equals("ncaab"))
						gamePlayed.setORating("");
					else 
						gamePlayed.setORating(oRating);
					
					column = columns.get(dIndex);
					String dRating = column.text();
					if(sport.equals("ncaab"))
						gamePlayed.setDRating("");
					else
						gamePlayed.setDRating(dRating);
					games.add(gamePlayed);
				}
			}
		}
		logger.info("Exiting getPlayerRating");
		return games;
	}
	
	public Set<GamePlayed>  getNFLPlayerInfo(Document doc, HttpClientWrapper httpClientWrapper,List<NameValuePair> headerValuePairs,
			String sport, String teamName, String playerName, Set<GamePlayed> games, Team teamObject) throws BatchException {
		logger.info("Entering getPlayerInfo");
		
		Element element = doc.getElementById("all_stats");//select(".table_outer_container table");
		Elements trs = element.select("tbody tr");
		String code = teamObject.getTeamCode();
		
		for(Element tr : trs) {
			
			//if(!className.equals("thead") && !className.contains("over_header")) {
				Elements columns = tr.children();
				int index = 1;
				Element column = columns.get(index);
				String text = column.text();
				String href = column.select("a").get(0).attr("href");
				
				//date
				LocalDate gameDate = parseDate(text);
				
				//team
				String team = "";
					column = columns.get(4);
					team = column.text();
				String teamlink = column.child(0).attr("href");	
				if(!teamlink.contains(code.toLowerCase())) continue;
				
				//opponent
				String opponent = "";
				column = columns.get(5);
				opponent = column.text();
				column = columns.get(6);
				opponent += column.text();
				
				//result
				column = columns.get(7);
				String result = column.text();
			
				GamePlayed game = findGame(games, gameDate);
				
				//GamePlayed game = new GamePlayed(gameDate, "0", "0", "", team, opponent, "0", "0", result, "", "");
				//LocalDate gameDate, String minutesPlayed, String minuitesLossed, String position, String team,
				//String opponent, String status, String points, String result, String oRating, String dRating
				if(game==null) { 
					System.out.println(tr+" teamName "+teamName+", playerName "+playerName);
					logger.error("GameDate: "+gameDate);
					System.exit(0);
				}
				game.setTeam(team);
				game.setOpponent(opponent);
				game.setResult(result);
				
				String snapUrl = "https://www.pro-football-reference.com" + href;
				String html = httpClientWrapper.getSiteNoRedirect(snapUrl, headerValuePairs);
				game = updateAllPlayerSnaps(game, html, teamName, playerName);
				System.out.println(game);
				//updateAllPlayerSnaps(game,headerValuePairs,httpClientWrapper,href,text,sport,teamName,playerName);
				games.add(game);
		}
		
		logger.info("Exiting getPlayerInfo");
		return games;
	}
	
    private  GamePlayed updateAllPlayerSnaps(GamePlayed game, String html, String teamName,
            String playerName) {

        String homehtml = searchCommentTable(html, "home_snap_counts");
        String visithtml = searchCommentTable(html, "vis_snap_counts");
        
        Document homedoc = Jsoup.parse(homehtml);
        Document visitdoc = Jsoup.parse(visithtml);
        boolean check = searchAndSet(homedoc, playerName, game);
        
        if(!check)
        	check = searchAndSet(visitdoc, playerName, game);
        
        return game;
    }
    
	private static boolean searchAndSet(Document doc,  String playerName, GamePlayed game) {
		 Elements trs = doc.select("tbody tr");
		 boolean valueset = false;
		 for (Element tr : trs) {

	            Element th = tr.select("th").get(0);
	            if (th.attr("data-stat").equalsIgnoreCase("player") && th.select("a").size() > 0) {
	                String playerNam = th.select("a").html();
	                if (playerNam.equalsIgnoreCase(playerName)) {

	                    Elements tdsEle = tr.select("td");
	                    for (Element tdEle : tdsEle) {
	                        String dataStatAttr = tdEle.attr("data-stat");
	                        if (dataStatAttr.equals("offense")) {
	                            String offense = tdEle.ownText();
	                            game.setOffensive_snap(offense);
	                            valueset = true;
	                        }
	                        if (dataStatAttr.equals("defense")) {
	                            String defense = tdEle.ownText();
	                            game.setDefensive_snap(defense);
	                            System.out.println(defense);
	                            valueset = true;
	                        }
	                    }
	                }
	            }
	        }
	        
	        return valueset;
	}
	
	private GamePlayed findGame(Set<GamePlayed> games, LocalDate date) {
		for(GamePlayed gp : games) {
			try {
				LocalDate gDate = gp.getGameDate();
				if(gDate!=null) {
					if(gDate.equals(date))
						return gp;
				} else {
					continue;
				}
			}catch(NullPointerException npe) {
				continue;
			}
		}
		return null;
	}
	

	
	// incorrect implementation for snap data...........
	/*
	 * private void updateAllPlayerSnaps(GamePlayed game, List<NameValuePair>
	 * headerValuePairs, HttpClientWrapper httpClientWrapper, String href, String
	 * text, String sport, String teamName, String playerName) throws BatchException
	 * {
	 * 
	 * String snapUrl = "https://www.pro-football-reference.com" + href;
	 * 
	 * String html = httpClientWrapper.getSiteNoRedirect(snapUrl, headerValuePairs);
	 * 
	 * html = searchCommentTable(html, "home_snap_counts"); Document doc =
	 * parseXhtml(html); Elements trs =
	 * doc.select("table#home_snap_counts tbody tr"); for (Element tr : trs) {
	 * 
	 * Element th = tr.select("th").get(0); if
	 * (th.attr("data-stat").equalsIgnoreCase("player") && th.select("a").size() >
	 * 0) { String playerNam = th.select("a").html(); if
	 * (playerNam.equalsIgnoreCase(playerName)) {
	 * 
	 * Elements tdsEle = tr.select("td"); for (Element tdEle : tdsEle) { String
	 * dataStatAttr = tdEle.attr("data-stat"); if (dataStatAttr.equals("offense")) {
	 * String offense = tdEle.ownText(); game.setOffensive_snap(offense);
	 * 
	 * } if (dataStatAttr.equals("defense")) { String defense = tdEle.ownText();
	 * game.setDefensive_snap(defense); logger.debug("defense "+defense); }
	 * 
	 * }
	 * 
	 * }
	 * 
	 * }
	 * 
	 * }
	 * 
	 * }
	 */

	public Set<GamePlayed>  getPlayerInfo(Document doc, String sport) {
		logger.info("Entering getPlayerInfo");
		
		Set<GamePlayed> games = new LinkedHashSet<>();
		
		Elements elements = doc.select(".table_outer_container table");
		Elements trs = elements.select("tbody tr");
		for(Element tr : trs) {
			String className = tr.attr("class");
			if(!className.equals("thead") && !className.contains("over_header")) {
				Elements columns = tr.children();
				int index = 0;
				if(sport.equals("nba"))
					index = 2;
				else
					index = 1;
				Element column = columns.get(index);
				String text = column.text();
				//date
				LocalDate gameDate = parseDate(text);
				
				//team
				String team = "";
				if(!sport.equals("ncaab") && !sport.equals("ncaaf")) {
					column = columns.get(4);
					team = column.text();
				}
				
				if(sport.equals("ncaab") || sport.equals("ncaaf") ) {
					column = columns.get(2);
					team = column.text();
				}
				
				//opponent
				String opponent = "";
				if(sport.equals("ncaab") || sport.equals("ncaaf")) {
					column = columns.get(3);
					opponent = column.text();
					column = columns.get(4);
					opponent += column.text();
					
				} else {
					column = columns.get(5);
					opponent = column.text();
					column = columns.get(6);
					opponent += column.text();
				}
				
				//result
				if(sport.equals("ncaab")) {
					column = columns.get(6);
				} else if(sport.equals("ncaaf")) {
					column = columns.get(5);
				} else
					column = columns.get(7);
				
				String result = column.text();
				
				String minutesPlayed = "0";
				String points = "0";
				String status = "";
				
				if(columns.size()>9 && columns.size()!=16) {
					//minutes played
					if(sport.equals("nba")) {
						column = columns.get(9);
						minutesPlayed = column.text();
					
						//points
						column = columns.get(27);
						points = column.text();
					} else if(sport.equals("ncaab")){
						column = columns.get(8);
						minutesPlayed = column.text();
						
						//points
					//	column = columns.get(5);
						//points = column.text();
					}else {
						if(!sport.equals("ncaaf")) {
							column = columns.get(23);
							minutesPlayed = column.text();
						
							//points
							column = columns.get(10);
							points = column.text();
						}
					}
					
				} else if(columns.size()==16){
					if(sport.equals("nba")) {
						column = columns.get(9);
						minutesPlayed = column.text();
					
						//points
						column = columns.get(27);
						points = column.text();
					} else {
						column = columns.get(15);
						minutesPlayed = column.text();
					}
				}else {
					column = columns.get(8);
					status = column.text();
				}
				
				GamePlayed game = new GamePlayed(gameDate, minutesPlayed, "0", "", team, opponent, status, points, result, "", "");
				games.add(game);
			}
		}
		
		logger.info("Exiting getPlayerInfo");
		return games;
	}
	
	public Set<GamePlayed>  getMLBPlayerInfo(String html, String sport, String year, String t) {
		logger.info("Entering getPlayerInfo");
		
		logger.debug("html "+html);
		Document doc = null;
		
		Set<GamePlayed> games = new LinkedHashSet<>();
		try {
			doc = parseXhtml(html);
			Elements elements = doc.select(".table_outer_container table");
			Elements trs = elements.select("tbody tr");
			for(Element tr : trs) {
				String className = tr.attr("class");
				if(!className.equals("thead") && !className.contains("over_header")) {
					Elements columns = tr.children();
					int index = 3;
					Element column = columns.get(index);
					String temp = column.toString().replaceAll("&nbsp;", " ");
					String text = Jsoup.parse(temp).text();
					
					if(text==null) continue;
					if(text.length()==0) continue;
					//date
					LocalDate gameDate = getDate(text, year);
					
					//team
					column = columns.get(4);
					String team = column.text();
					
					//opponent
					column = columns.get(5);
					String opponent = column.text();
					column = columns.get(6);
					opponent += column.text();
					
					//result
					column = columns.get(7);
					String result = column.text();
					
					String pa = "0";
					String position = "";
					
					if(!t.equals("p")) {
						column = columns.get(9);
						column.text();
						
						column = columns.get(columns.size()-1);
						position = column.text();
					}
					
					GamePlayed game = new GamePlayed(gameDate, "", "0", position, team, opponent, "", "", result, "", "");
					game.setMlb_pa(pa);
					games.add(game);
					
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.debug(""+e);
			//System.exit(0);
		}
		logger.info("Exiting getPlayerInfo");
		return games;
	}
	
	private static LocalDate getDate(String datetext, String yr) {
		LocalDate date = null;
		try {
			String array[] = datetext.split(" ");
			int year = Integer.parseInt(yr);
			int month = getMonth(array[0]);
			String temp = array[1];
			if(temp.contains("(")) {
				int start = temp.indexOf("(");
				int end = temp.indexOf(")");
				temp = temp.substring(0, start);
			}
				
			int day = Integer.parseInt(temp);
			date = LocalDate.of(year, month, day);
		} catch(Exception e) {
			logger.error("Error for date "+datetext);
			e.printStackTrace();
			System.exit(0);
		}
		return date;
	}
	
	private static int getMonth(String mon) {
		int month = 0;
		mon = mon.toLowerCase();
		
		if("january".contains(mon)){month = 1;}
		else if("february".contains(mon)){month = 2;}
		else if("march".contains(mon)){month = 3;}
		else if("april".contains(mon)){month = 4;}
		else if("may".contains(mon)){month = 5;}
		else if("june".contains(mon)){month = 6;}
		else if("july".contains(mon)){month = 7;}
		else if("august".contains(mon)){month = 8;}
		else if("september".contains(mon)){month = 9;}
		else if("october".contains(mon)){month = 10;}
		else if("november".contains(mon)){month = 11;}
		else if("december".contains(mon)){month = 12;}
		
		return month;
	}

	
	private LocalDate parseDate(String text) {
	
		LocalDate date = null;
		try {
			String str[] = text.split("-");
			int year = Integer.parseInt(str[0]);
			int month = Integer.parseInt(str[1]);
			int day = Integer.parseInt(str[2]);
			
			date = LocalDate.of(year, month, day);
		}catch(Exception e) {
			logger.error("Date parsing error");
		}
		return date;
	}
	
	public Map<String, String> getPlayersLink(Document doc, String sport) throws BatchException {
		logger.info("Entering getPlayersLink");
		
		Map<String,String> map = new LinkedHashMap<>();
		
		Elements trs = null;
		
		if(sport.equals("nfl")) {
			String table = null;
			String html = "";
			
			table = searchCommentTable(doc.toString(), "rushing_and_receiving");
			html = doc.toString().replace(table, "");
			
			table += searchCommentTable(html, "returns");
			html = html.replace(table, "");
			
			table += searchCommentTable(html, "kicking");
			html = html.replace(table, "");
			
			table += searchCommentTable(html, "defense");
			html = html.replace(table, "");
			
			Document document = parseXhtml(table);				
			trs = document.select("tbody tr");
			
		} else if(sport.equals("nhl")){
			Element element = doc.getElementById("roster");
			trs = element.select("tbody tr");
		} else {
			trs = doc.select("tbody tr");
		}
		
		//System.out.println(trs);
		
		for(Element tr : trs) {
			Elements tds = tr.children();
			
			String playerName = "";
			String link = "";
			for(Element td : tds) {
				String tag = td.tagName();
				
				if(tag.equals("td")) {
					String className = td.attr("class");
					if(className.trim().equals("left")) {
						Elements as = td.select("a");
						link = as.attr("href");
						playerName = td.select("a").text();
						if(playerName.length()>0 && link.length()>0) {
							map.put(playerName, link); break;
						}
					}
				}
			}

		}
		logger.info("Exiting getPlayersLink");
		
		return map;
	}
	
	public List<Player> getPlayers(Document doc, String sportName, String teamName) {
		logger.info("Entering getPlayers");
		
		List<Player> list = new ArrayList<>();
		Elements trs = null;
		
		try {
			if(sportName.equals("nhl")) {
				Element element = doc.getElementById("roster");
				trs = element.select("tbody tr"); 
			} else if(sportName.equals("nba")) {
				trs = doc.select("tbody tr");
			} else if(sportName.equals("ncaab")) {
				//Element table = doc.getElementById("per_game");
				Element table = doc.getElementById("roster");
				trs = table.select("tbody tr");
			} else if(sportName.equals("nfl") || sportName.equals("ncaaf")) {
				
				String table = null;
				String html = "";
				
				table = searchCommentTable(doc.toString(), "rushing_and_receiving");
				html = doc.toString().replace(table, "");
				
				table += searchCommentTable(html, "returns");
				html = html.replace(table, "");
				
				table += searchCommentTable(html, "kicking");
				html = html.replace(table, "");
				
				table += searchCommentTable(html, "defense");
				html = html.replace(table, "");
				
				Document document = parseXhtml(table);				
				trs = document.select("tbody tr");
			}
		}catch(Exception e) {
			logger.error("Error parsing table for "+teamName);
		}
		
		try {
			for(Element tr : trs) {
				Elements tds = tr.children();
				
				String playerName = "";
				String playerNo = "";
				int playerno = 0;
				for(Element td : tds) {
					String tag = td.tagName();
					/*
					if(tag.equals("th")) {
						try {
						playerNo = td.text();
						playerno = Integer.parseInt(playerNo);
						}catch(Exception e) {
							logger.error("No player no or error parsting player no "+td);
						}
					}else { */
						String className = td.attr("class");
						if(className.trim().equals("left")) {
							playerName = td.select("a").text();
						}
					//}
					if(playerName.length()!=0) break;
				}
				String pos = "";
	
				if(sportName.equals("ncaaf"))
					playerno=0;
				
				Player player = new Player(sportName, teamName, playerName);
				
				if(sportName.equals("nba")) {
					pos = tds.get(2).text();
					player.setPosition(pos);
				} else if(sportName.equals("nhl") || sportName.equals("ncaab") || sportName.equals("nfl")) {
					pos = tds.get(3).text();
					player.setPosition(pos);
				}
				if(!list.contains(player))
					list.add(player);
			}
			
			/////
			if(sportName.equals("ncaab"))
				list = updateORatingDrating(doc.toString(),list);
		}
		catch(Exception e) {
			logger.error("Problem for "+teamName);
		}
		logger.info("Exiting getPlayers");
		
		return list;
	}
	
	
	private List<Player> updateORatingDrating(String html,List<Player> list ){
		String oratdrat = searchCommentTable(html, "per_poss");
		//Player player = new Player("ncaab", "Michigan State Spartans", "Jack Hoiberg");
		Document doc = Jsoup.parse(oratdrat);
		Elements trs = doc.select("tbody tr");
		
		try {
			for(Element tr : trs) {
				Elements tds = tr.children();

				Player player = null;
				for(Element td : tds) {
					String datastat = td.attr("data-stat");
					
					//get player
					if(datastat.equals("player")) {
						String playerNm = td.children().first().text();
						logger.debug("oRating and dRating of "+playerNm);
						player = findPlayer(list, playerNm);
					}
	
					//get oRating and dRating
					if(datastat.equals("off_rtg")) {
						String oRating = td.text();
						player.setoRating(oRating);
					}
					if(datastat.equals("def_rtg")) {
						String dRating = td.text();
						player.setdRating(dRating);
					}
					
					
				}
			}
		} catch(Exception e){
			e.printStackTrace();
		}
		return list;
		
	}
	
	public Map<String, String> getPlayersLink(String html) throws BatchException{
		
		String table = searchCommentTable(html, "appearances");
		Document document = parseXhtml(table);
		Elements trs = document.select("tbody tr");
		
		Map<String, String> map = new LinkedHashMap<>();
		
		for(Element tr : trs) {
			Element td = tr.children().get(0);
			String href = td.select("a").attr("href");
			String playerName = td.select("a").text();
			map.put(href, playerName);
		}
		
		return map;
	}
	
	public String searchCommentTable(String html, String search) {
		logger.info("Entering searchCommentTable");
		StringBuffer sb = new StringBuffer();
		sb.append(html);
		
		StringBuffer newHtml = new StringBuffer("");
		while(true) {
			
			int start = sb.indexOf("<!--");
			int end = sb.indexOf("-->");
			StringBuffer temp = new StringBuffer(sb.substring(start+4, end));
			if(temp.indexOf(search) != -1) {
				newHtml = temp;
				break;
			} else {
				//temp = sb.substring(start, end+3);
				sb = sb.replace(start, end+3, "");
				//html = html.replace(temp, "");
			}
		}
		
		logger.info("Exiting searchCommentTable");
		return newHtml.toString();
	}
	
	public Map<String, String> getTeamLinks(Element conference, String sport) {
		Elements trs = conference.select("tbody tr");
		Map<String, String> urls = new LinkedHashMap<>();
		
		for(Element tr : trs) {
			String classname = tr.attr("class");
			
			if(!classname.equals("thead")) {
				Elements elements = tr.children();
				
				for(Element element : elements) {
					String className = element.attr("class").trim();
					if(className.equals("left")) {
						Elements as = element.select("a");
						String href = as.attr("href");
						String teamName = as.text();
						urls.put(href,teamName);
					}
				}
			}
		}
		return urls;
	}
	
	public Map<String, String> parseTeamsList(Document doc){
		logger.info("Entering parseTeamsList");
		
		Map<String, String> teamsMap = new LinkedHashMap<String, String>();
		
		try {
			Element element = doc.getElementById("player_roster");
			Elements options = element.getElementById("selector_0").children();
			for(Element option : options) {
				if(option.val().length()>0)
					teamsMap.put(option.val(), option.text());
			}
		}catch(Exception e) {
			logger.error("Error while parsing teams data @ parseTeamsList()");
		}
		logger.info("Exiting parseTeamsList");
		
		return teamsMap;
	}
	
	public List<TeamCategory>  getTeamCategoryList(String html, String sport) throws BatchException {
		Document doc = parseXhtml(html);
		
		List<TeamCategory> list = new ArrayList<>();
		
		Elements estandings = doc.select("#standings_E");
		Element americanLeague = estandings.get(0);
		Element nationalLeague = estandings.get(1);
		List<TeamCategory> acategoryList = getTeamCategory("American League", "East Division", americanLeague, sport);
		List<TeamCategory> ncategoryList = getTeamCategory("National League", "East Division", nationalLeague, sport);
		list.addAll(acategoryList);
		list.addAll(ncategoryList);
		
		Elements cstandings = doc.select("#standings_C");
		americanLeague = cstandings.get(0);
		nationalLeague = cstandings.get(1);
		acategoryList = getTeamCategory("American League", "Central Division", americanLeague, sport);
		ncategoryList = getTeamCategory("National League", "Central Division", nationalLeague, sport);
		list.addAll(acategoryList);
		list.addAll(ncategoryList);
		
		Elements wstandings = doc.select("#standings_W");
		americanLeague = wstandings.get(0);
		nationalLeague = wstandings.get(1);
		acategoryList = getTeamCategory("American League", "West Division", americanLeague, sport);
		ncategoryList = getTeamCategory("National League", "West Division", nationalLeague, sport);
		list.addAll(acategoryList);
		list.addAll(ncategoryList);
		
		return list;
	}
	
	private List<TeamCategory> getTeamCategory(String category, String subcategory, Element league, String sport) {
		
		List<TeamCategory> categoryList = new ArrayList<TeamCategory>();
		
		Elements trs = league.select("tbody tr");
		
		Set<Team> teams = null;
		TeamCategory teamCategory = new TeamCategory(sport, category, subcategory);
		teams = teamCategory.getTeams();
		categoryList.add(teamCategory);
		
		for(Element tr : trs) {
			String teamName = tr.children().get(0).text();
								
			Team team = new Team(teamName, sport);
			team.setTeamNo(0);
			team.setTeamcategory(category);
			team.setSubCategory(subcategory);
			
			if(teams!=null) {
				teams.add(team);
			}
		}
		
		if(teamCategory!=null)
			teamCategory.setTeams(teams);
		
		return categoryList;
	}
	
	
	public Map<String, String> getTeamlinks(String html, String sport) throws BatchException {
		Document doc = parseXhtml(html);
		Map<String, String> teamLinks = new LinkedHashMap<String, String>();
				
		Elements estandings = doc.select("#standings_E");
		Element americanLeague = estandings.get(0);
		Element nationalLeague = estandings.get(1);
		Map<String, String> acategoryList = getTeamLinks("American League", "East Division", americanLeague, sport);
		Map<String, String> ncategoryList = getTeamLinks("National League", "East Division", nationalLeague, sport);
		teamLinks.putAll(acategoryList);
		teamLinks.putAll(ncategoryList);
		
		Elements cstandings = doc.select("#standings_C");
		americanLeague = cstandings.get(0);
		nationalLeague = cstandings.get(1);
		acategoryList = getTeamLinks("American League", "Central Division", americanLeague, sport);
		ncategoryList = getTeamLinks("National League", "Central Division", nationalLeague, sport);
		teamLinks.putAll(acategoryList);
		teamLinks.putAll(ncategoryList);
		
		Elements wstandings = doc.select("#standings_W");
		americanLeague = wstandings.get(0);
		nationalLeague = wstandings.get(1);
		acategoryList = getTeamLinks("American League", "West Division", americanLeague, sport);
		ncategoryList = getTeamLinks("National League", "West Division", nationalLeague, sport);
		teamLinks.putAll(acategoryList);
		teamLinks.putAll(ncategoryList);
		
		return teamLinks;
	}
	
	private Map<String, String> getTeamLinks(String category, String subcategory, Element league, String sport) {
		
		Map<String, String> map = new LinkedHashMap<>();
		Elements trs = league.select("tbody tr");
		
		for(Element tr : trs) {
			String teamName = tr.children().get(0).text();
			String link = tr.children().get(0).select("a").attr("href");
			map.put(link, teamName);
		}
		
		return map;
	}
	
public List<TeamCategory> parseTable(Element conference, String sport) {
		
		List<TeamCategory> categoryList = new ArrayList<TeamCategory>();
		
		String category = "";
		if(sport.equals("nba"))
			category = conference.select("thead tr th").get(0).text();
		else if(sport.equals("nfl")) {
			category = conference.getElementsByTag("caption").text();
			if(category.contains("AFC"))
				category = "American Football Conference";
			else if(category.contains("AFC"))
				category = "National Football Conference";
		}
		else
			category = conference.select(".section_heading h2").text();
		
		Elements trs = conference.select("tbody tr");
		
		Set<Team> teams = null;
		TeamCategory teamCategory = null;
		String subcategory = "";
		
		for(Element tr : trs) {
			String classname = tr.attr("class");
			
			if(classname.contains("thead")) {
				subcategory = tr.text();
				if(subcategory.contains("East"))
					subcategory = "East Division";
				else if(subcategory.contains("West"))
					subcategory = "West Division";
				else if(subcategory.contains("North"))
					subcategory = "North Division";
				else if(subcategory.contains("South"))
					subcategory = "South Division";
				
				teamCategory = new TeamCategory(sport, category, subcategory);
				teams = teamCategory.getTeams();
				categoryList.add(teamCategory);
			}else {
				Elements elements = tr.children();
				
				for(Element element : elements) {
					String className = element.attr("class").trim();
					if(className.equals("left")) {
						Elements as = element.select("a");
						String teamName = as.text();
						int teamno = 0;
						
						if(sport.equals("nba")) {
							Element span = element.select("span").get(0);
							int index1 = span.toString().indexOf("(");
							int index2 = span.toString().indexOf(")");
							String spanStr = span.toString().substring(index1+1, index2);
							teamno = Integer.parseInt(spanStr);
							logger.trace(teamName+" - "+ teamno);
						}
						Team team = new Team(teamName, sport);
						team.setTeamNo(teamno);
						team.setTeamcategory(category);
						team.setSubCategory(subcategory);
						
						if(teams!=null) {
							teams.add(team);
						}
					}
				}
				if(teamCategory!=null)
					teamCategory.setTeams(teams);
			}
		}
		
		return categoryList;
	}
	
	@SuppressWarnings("unused")
	private StringBuilder readResponse(HttpResponse response) throws IOException {
		logger.info("Entering readResponse()");

		InputStreamReader is = new InputStreamReader(response.getEntity().getContent());
		BufferedReader bufReader = new BufferedReader(is);
		StringBuilder builder = new StringBuilder();
		String line;

		while ((line = bufReader.readLine()) != null) {
			builder.append(line);
			builder.append(System.lineSeparator());
		}

		if(is!=null) {
			is.close();
		}
		logger.info("Exiting readResponse()");
		return builder;
	}
	
}
