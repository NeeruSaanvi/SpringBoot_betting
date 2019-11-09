package com.injury.sites.sportsreferences;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.injury.dto.EspnYPPObject;
import com.injury.dto.TeamData;
import com.injury.dto.TeamDetail;
import com.injury.entity.Team;
import com.injury.entity.TeamContainer;
import com.injury.errorhandling.BatchException;
import com.injury.sites.SiteProcessor;

@Service
public class InjuryService extends SiteProcessor {

	private Logger logger = LoggerFactory.getLogger(getClass());
	
	private static String[][] links = { { "nfl", "https://www.pro-football-reference.com", "Football" },
			{ "ncaaf", "https://www.sports-reference.com/cfb", "College_Football" },
			{ "nba", "https://www.basketball-reference.com", "Basketball" },
			{ "ncaab", "https://www.sports-reference.com/cbb/", "College_Basketball" },
			{ "mlb", "https://www.baseball-reference.com", "Baseball" },
			{ "nhl", "https://www.hockey-reference.com", "Hockey" }, };

	public List<Team> getTeamListWithDAndORating(String sportName) {
		List<Team> teamList = teamsRepo.findBySport(sportName, Sort.by("name"));
		// find the link of game
		String url = "";
		for (int i = 0; i < links.length; i++) {
			String sport = links[i][0];
			if (sportName.trim().equals(sport)) {
				url = links[i][1];
				break;
			}
		}
		JSONArray arryTop = new JSONArray();

		if (url != null && !url.isEmpty()) {
			for (Team team : teamList) {
				if (team.getTeamCode() != null && !team.getTeamCode().isEmpty()) {
					System.out.println("::::::::::::::::::::::::");
				}
			}
		}
		
		writeOnFile(arryTop);
		return teamList;
	}
	
	private void writeOnFile(JSONArray arrays) {
		String path = "C:/Users/psusr039/Desktop/html/InjuryFile.json";
		try {
			File file = new File(path);
			FileWriter fw = new FileWriter(path);
			fw.write(arrays.toString());
			fw.flush();
			fw.close();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public List<TeamData> getTeamData(String sportName) {
		
		List<Team> teamList = teamsRepo.findBySport(sportName, Sort.by("name"));
		// find the link of game
		String url = "";
		for (int i = 0; i < links.length; i++) {
			String sport = links[i][0];
			if (sportName.trim().equals(sport)) {
				url = links[i][1];
				break;
			}
		}
		
		List<TeamData> tmList = new ArrayList<>();
		
		if(sportName.equals("nhl")) {
			//List<TeamData> listOfTeam = getTeamListWithShots(sportName);
			if (url != null && !url.isEmpty()) {
				for (Team team : teamList) {
					if (team.getTeamCode() != null && !team.getTeamCode().isEmpty()) {
						//if(team.getName().equals("Chicago Blackhawks"))
							//System.out.println();
						List<TeamDetail> teamDtl = getTeamListWithShots(url, sportName, team.getTeamCode());
						TeamData tm = new TeamData();
						if (teamDtl.size() > 0) {
							String id = team.getName().replaceAll(" ", "") + sportName;
							if (teamDataRepo.existsById(id)) {
								tm = teamDataRepo.findById(id).orElse(null);
								if (tm == null)
									tm = new TeamData();
							}
							tm.setId(team.getName().replaceAll(" ", "") + sportName);
							tm.setSport(sportName);
							tm.setTeam(team.getName());
							tm.setDetail(teamDtl);
							tmList.add(tm);
							teamDataRepo.save(tm);
						}
					}
				}
			}
		} else if(sportName.equals("nba")) {
			
			if (url != null && !url.isEmpty()) {
				for (Team team : teamList) {
					if (team.getTeamCode() != null && !team.getTeamCode().isEmpty()) {
						List<TeamDetail> possessionsList = null;
						try {
							//if(team.getName().equals("Chicago Bulls"))
							possessionsList = getListWithPossessions(url, sportName, team.getTeamCode(),team.getName());
						} catch (BatchException e) {
							e.printStackTrace();
						}
						TeamData tm = new TeamData();
						if(possessionsList!=null)
						if (possessionsList.size() > 0) {
							String id = team.getName().replaceAll(" ", "") + sportName;
							if (teamDataRepo.existsById(id)) {
								tm = teamDataRepo.findById(id).orElse(null);// dById(id);
								if (tm == null)
									tm = new TeamData();
							}
							tm.setId(team.getName().replaceAll(" ", "") + sportName);
							tm.setSport(sportName);
							tm.setTeam(team.getName());
							tm.setDetail(possessionsList);
							tmList.add(tm);
							teamDataRepo.save(tm);
						}
					}
				}
			}
		}  else if(sportName.equals("mlb")) {
			if (url != null && !url.isEmpty()) {
				for (Team team : teamList) {
					if (team.getTeamCode() != null && !team.getTeamCode().isEmpty()) {
						List<TeamDetail> teamDtl = getTeamListWithPitcher(url, sportName, team.getTeamCode(), team.getName());
						TeamData tm = new TeamData();
						if (teamDtl.size() > 0) {
							String id = team.getName().replaceAll(" ", "") + sportName;
							if (teamDataRepo.existsById(id)) {
								tm = teamDataRepo.findById(id).orElse(null);// dById(id);
								if (tm == null)
									tm = new TeamData();
							}
							tm.setId(team.getName().replaceAll(" ", "") + sportName);
							tm.setSport(sportName);
							tm.setTeam(team.getName());
							tm.setDetail(teamDtl);
							tmList.add(tm);
							teamDataRepo.save(tm);
						}
					}
				}
			}
		} else if(sportName.equals("nfl")) {
			if (url != null && !url.isEmpty()) {
				System.out.println(teamList.size());
				for (Team team : teamList) {
					if (team.getTeamCode() != null && !team.getTeamCode().isEmpty()) {
						System.out.println(url);
						List<TeamDetail> otList = getoffensiveAndDefenciveYpp(url, sportName, team.getTeamCode(), team.getName(),"2018");

						TeamData tm = null;
						if (otList.size() > 0) {
							String id = team.getName().replaceAll(" ", "") + sportName;
							if (teamDataRepo.existsById(id)) {
								tm = teamDataRepo.findById(id).orElse(null);// dById(id);
								if (tm == null)
									tm = new TeamData();
							}
							else {
								tm = new TeamData();
							}
							tm.setId(id);
							tm.setSport(sportName);
							tm.setTeam(team.getName());
							tm.setDetail(otList);
							tmList.add(tm);
							System.out.println(tm);
							teamDataRepo.save(tm);
						}
					}
				}
			}
		} else if(sportName.equals("ncaab")) {
			
			String mainUrl="";
			String year = "2019";
			
			if(array[0][0].equals(sportName))
				mainUrl=array[0][1];
			if(array[1][0].equals(sportName))
				mainUrl=array[1][1];
			
			try {
				
				//all_confs_standings_E   //all_confs_standings_W
				Map<String,String> mapTeamCode = new HashMap<String,String>();
				String html = super.httpClientWrapper.getSiteNoRedirect(mainUrl, getHeader());

				if(sportName.equals("ncaab"))
					return processNCAAB(url,html, sportName,year);
				
				logger.debug("HTML: "+html);
				Document doc = Jsoup.parse(html);//parser.parseXhtml(html);
				//Document doc = connectionUrl(mainUrl);
				Element esternIdEle = doc.getElementById("all_confs_standings_E");
				Element wsternIdEle = doc.getElementById("all_confs_standings_W");
				iterateIdElements(esternIdEle, mapTeamCode);
				iterateIdElements(wsternIdEle, mapTeamCode);
				//------Call Second Step
				
				List<TeamData> listTeamData = new ArrayList<TeamData>();
				for(Entry<String, String> entry : mapTeamCode.entrySet()) {
					String key = entry.getKey();
					//String value = entry.getValue();
					String purl = url+"/teams/"+key+"/"+year+"/gamelog-advanced/";
					logger.debug("URL: "+purl);
					html = super.httpClientWrapper.getSiteNoRedirect(purl, getHeader());
					doc = Jsoup.parse(html);
					List<TeamDetail> teamDetailList = parseAdvancePage(url,key,doc);
					TeamData teamData = new TeamData();
					teamData.setId(mapTeamCode.get(key).replaceAll(" ", "")+sportName);
					teamData.setTeam(mapTeamCode.get(key));
					teamData.setSport(sportName);
					teamData.setDetail(teamDetailList);
					listTeamData.add(teamData);
				}
				return listTeamData;
			}catch(Exception e) {
				e.printStackTrace();
			}
			return null;
			
		}
		return tmList;
	}

	

	public List<TeamDetail> getoffensiveAndDefenciveYpp(String url, String sportName, String teamCode, String teamName,String year) {
		String urlOfGameLog = url + "/teams" + "/" + teamCode + "/" + year+".htm";
		String htmlData = "";
		List<TeamDetail> array = new ArrayList<>();
		
		try {
			
			htmlData = super.httpClientWrapper.getSiteNoRedirect(urlOfGameLog, getHeader());
			logger.debug("TeamDataHTML: "+htmlData);
			Map<String,EspnYPPObject> map = getYPPData(year);
			Document teamdoc = Jsoup.parse(htmlData);
			Element table = teamdoc.getElementById("games");
			
			//System.out.println(table);
			Elements trs = table.select("tbody tr");
 			LocalDate previousDate = null;
			for(Element tr : trs) {
				TeamDetail tm = new TeamDetail();
				
				Elements tds = tr.children();
				
				for (Element td : tds) {
					
					if(td.attr("data-stat").equalsIgnoreCase("week_num")) {
						String week = "Week-"+td.text();
						tm.setWeek(week);
					}
					
					if ((td.attr("data-stat") != null && td.attr("data-stat").equalsIgnoreCase("game_date"))) {
				       String dt = td.attr("csk");
				       LocalDate localDate = null;
				       
				       if(dt.length()>0)
				    	   localDate = LocalDate.parse(dt);
				       else 
				    	   localDate = previousDate.plusDays(1);
				       
				       tm.setDate(localDate);
				       previousDate = localDate;
				       array.add(tm);
				      // break;
					}
					
				}
			}
			
			for(TeamDetail teamDetail : array) {
				String str = teamName.replace(" ", "")+teamDetail.getDate();
				EspnYPPObject espnYPPobject = map.get(str);
				if(espnYPPobject!=null) {
				teamDetail.setOffensiveYPP(espnYPPobject.getYpp1());
				teamDetail.setDefensiveYPP(espnYPPobject.getYpp2());
				} else {
					System.out.println(teamDetail.getWeek()+str);
					System.exit(0);
				}
			}
			
			System.out.println("DDD"+array);
			
		} catch (BatchException e1) {
			e1.printStackTrace();
		}
		
		return array;
		/*
		urlOfGameLog = url + "/teams" + "/" + teamCode + "/" + year + "/gamelog/";
		System.out.println("urlOfGameLog" + urlOfGameLog);
		List<TeamDetail> offensiveAndDefensiveYPP = new ArrayList<TeamDetail>();
		try {
			htmlData = super.httpClientWrapper.getSiteNoRedirect(urlOfGameLog, getHeader());
			if (htmlData != null && !htmlData.isEmpty()) {
				if (sportName != null && sportName.equalsIgnoreCase("nfl")) {
					//offensiveAndDefensiveYPP = parseDefensiveAndDefensiveYpp_FootBall(url,htmlData,teamName,year);
				}
				return offensiveAndDefensiveYPP;
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return offensiveAndDefensiveYPP;
		*/
	}
	
	
	private Map<String,EspnYPPObject> getYPPData(String year) throws BatchException {
		
		Map<String,EspnYPPObject> map = new LinkedHashMap<>();
		
		String espnurl = "https://www.espn.in/nfl/scoreboard/_/year/"+year+"/seasontype/2/week/";
		try {
			for(int i = 1; i<18; i++) {
				String espnhtml = super.httpClientWrapper.getSiteNoRedirect(espnurl, getHeader());
				
				String scoreboardData = "scoreboardData";
				int start = espnhtml.indexOf(scoreboardData);
				start = start+scoreboardData.length()+4;
				
				int end = espnhtml.indexOf(";window.espn.scoreboardSettings");
				if(end == -1) continue;
				String source = espnhtml.substring(start, end);
				JSONObject object = new JSONObject(source);
				JSONArray array = object.getJSONArray("events");
				
				for(int j=0; j<array.length(); j++) {
					EspnYPPObject yppobject = new EspnYPPObject();
					
					JSONObject event = (JSONObject) array.get(j);
					String date = (String) event.get("date");
					String str[] = date.split("T");
					date = str[0];
					LocalDate yppdate = LocalDate.parse(date);
					String id = (String) event.get("id");
					String shortName = (String) event.get("shortName");
					String name = (String) event.get("name");
					yppobject.setDate(yppdate);
					yppobject.setGameid(id);
					yppobject.setShortName(shortName);
					yppobject.setName(name);
					yppobject.setWeek("Week-"+i);
					JSONArray arr = (JSONArray) event.get("competitions");
					JSONObject obj = (JSONObject)arr.get(0);
					JSONArray competitor = (JSONArray) obj.get("competitors");
					
					String yppurl = "https://www.espn.in/nfl/matchup?gameId="+id;
					String ypphtml = super.httpClientWrapper.getSiteNoRedirect(yppurl, getHeader()); //sendGET(yppurl);
					Document yppdoc = Jsoup.parse(ypphtml);
					Elements ypptrs = yppdoc.getElementsByClass("mod-data").select("tbody tr");
					for(Element ypptr : ypptrs) {
						String yard = ypptr.attr("data-stat-attr");
						if(yard.equals("yardsPerPlay")) {
							Elements tds = ypptr.children();
							String ypp1 = tds.get(1).text();
							String ypp2 = tds.get(2).text();
							yppobject.setYpp1(ypp1);
							yppobject.setYpp2(ypp2);
						}
					}
					
					String idName = "";
					for(int k=0; k<2; k++) {
						JSONObject team = competitor.getJSONObject(k);
						String homeoraway = (String) team.get("homeAway");
						String score = (String) team.get("score");
						JSONObject info = team.getJSONObject("team");
						String displayName = (String) info.get("displayName");
						
						System.out.println("Week-"+i+" Date "+date+" displayName"+displayName);
						logger.debug("Week-"+i+" Date "+date+" displayName"+displayName);
						
						String abbrivation = (String) info.get("abbreviation");
						String shortDisplayName = (String) info.get("shortDisplayName");

						if(k==0) {
							idName = displayName.replace(" ", "");
							yppobject.setHomeoraway1(homeoraway);
							yppobject.setScore1(score);
							yppobject.setDisplayName1(displayName);
							yppobject.setAbbrivation1(abbrivation);
							yppobject.setShortDisplayName1(shortDisplayName);
						} else {
							yppobject.setHomeoraway2(homeoraway);
							yppobject.setScore2(score);
							yppobject.setDisplayName2(displayName);
							yppobject.setAbbrivation2(abbrivation);
							yppobject.setShortDisplayName2(shortDisplayName);
						}
					}
					map.put(idName+str[0],yppobject);					
				}
				System.out.println("Week-"+i+map);
				logger.debug("Week-"+i+map);
				//break;
			}
		} catch (Exception e) {
			logger.error(e+"");
			e.printStackTrace();
		}
		
		return map;
	}
	
	/*
	 * private List<TeamDetail> parseDefensiveAndDefensiveYpp_FootBall(String url,
	 * String htmlData, String teamName, String year) throws IOException,
	 * BatchException { Document doc = Jsoup.parse(htmlData);
	 * //System.out.println(htmlData); System.out.println(htmlData);
	 * List<TeamDetail> array = new ArrayList<>(); Elements trs =
	 * doc.select("table#gamelog"+year+" tbody tr");
	 * 
	 * for (Element tr : trs) { boolean findsuffixUrl = false; TeamDetail tm = new
	 * TeamDetail(); String suffixUrl = ""; if ((tr.select("th").attr("data-stat")
	 * != null && tr.select("th").attr("data-stat").equalsIgnoreCase("week_num"))) {
	 * String weekNo = tr.select("th").html(); tm.setWeek("Week-"+weekNo);
	 * 
	 * } for (Element td : tr.select("td")) { if ((td.attr("data-stat") != null &&
	 * td.attr("data-stat").equalsIgnoreCase("game_date"))) { LocalDate localDate =
	 * LocalDate.parse(td.attr("csk")); tm.setDate(localDate); findsuffixUrl = true;
	 * 
	 * }
	 * 
	 * if ( findsuffixUrl && (td.attr("data-stat") != null &&
	 * td.attr("data-stat").equalsIgnoreCase("boxscore_word")) &&
	 * (td.select("a").size() > 0 && td.select("a").get(0) != null)) { Element a =
	 * td.select("a").get(0); suffixUrl = a.attr("href");
	 * //parseOffensiveAndDefensiveData(url,tm,suffixUrl, teamName); array.add(tm);
	 * } }
	 * 
	 * } return array;
	 * 
	 * }
	 * 
	 * 
	 * private void parseOffensiveAndDefensiveData(String url,TeamDetail tm, String
	 * suffixUrl, String teamName) throws IOException, BatchException { if
	 * ((suffixUrl != null && !suffixUrl.isEmpty())) { String fullUrl = url +
	 * suffixUrl; System.out.println(fullUrl); String htmlData1 =
	 * super.httpClientWrapper.getSiteNoRedirect(fullUrl,
	 * getHeader());//sendGET(fullUrl); if (htmlData1 != null &&
	 * !htmlData1.isEmpty()) { String htmlTable = searchCommentTable(htmlData1,
	 * "expected_points"); if ((htmlTable != null && !htmlTable.isEmpty())) {
	 * Document doc = Jsoup.parse(htmlTable); Elements expectedtrs =
	 * doc.select("table#expected_points tbody tr");
	 * 
	 * if(expectedtrs.size()==2) { Element tr0 = expectedtrs.get(0); Element tr1 =
	 * expectedtrs.get(1);
	 * 
	 * String team1 = tr0.children().first().text(); String team2 =
	 * tr1.children().first().text();
	 * 
	 * if(teamName.contains(team1)) { //System.out.println("Team1"+tr0);
	 * 
	 * for(Element td : tr0.children()) {
	 * 
	 * String offtot = td.attr("data-stat");
	 * if(offtot.equals("pbp_exp_points_off_tot")) { String offensiveYPP =
	 * td.text(); tm.setOffensiveYPP(offensiveYPP);
	 * System.out.println("offenceive set"+offensiveYPP); }
	 * 
	 * String deftot = td.attr("data-stat");
	 * if(deftot.equals("pbp_exp_points_def_tot")) { String defensiveYPP =
	 * td.text(); tm.setDefensiveYPP(defensiveYPP);
	 * System.out.println("defensive set"+defensiveYPP); }
	 * 
	 * } System.out.println(); } else if(teamName.contains(team2)) {
	 * //System.out.println("Team2"+tr1);
	 * 
	 * for(Element td : tr1.children()) {
	 * 
	 * String offtot = td.attr("data-stat");
	 * if(offtot.equals("pbp_exp_points_off_tot")) { String offensiveYPP =
	 * td.text(); tm.setOffensiveYPP(offensiveYPP);
	 * System.out.println("offenceive set"+offensiveYPP); }
	 * 
	 * String deftot = td.attr("data-stat");
	 * if(deftot.equals("pbp_exp_points_def_tot")) { String defensiveYPP =
	 * td.text(); tm.setDefensiveYPP(defensiveYPP);
	 * System.out.println("defensive set"+defensiveYPP); }
	 * 
	 * } } //System.out.println("value set");
	 * 
	 * }
	 * 
	 * } } } }
	 * 
	 */
	
	public List<TeamDetail> getTeamListWithPitcher(String url, String sportName, String teamCode, String teamName) {
		//teamDataRepo.deleteBySport(sportName);
		//this.url = url;
		String year = "2019";
		//https://www.baseball-reference.com/teams/tgl.cgi?team=NYY&t=b&year=2019
		String urlOfGameLog = url + "/teams" + "/tgl.cgi?team=" + teamCode + "&t=b&year=" + year ;
		System.out.println("urlOfGameLog" + urlOfGameLog);
		List<TeamDetail> pitcherList = new ArrayList<TeamDetail>();
		try {
			String htmlData = super.httpClientWrapper.getSiteNoRedirect(urlOfGameLog, getHeader());//sendGET(urlOfGameLog);
			if (htmlData != null && !htmlData.isEmpty()) {

				if (sportName != null && sportName.equalsIgnoreCase("mlb")) {
					pitcherList = parsePichersforMLB(url, htmlData, teamName);
					//System.out.println(pitcherList);
					//System.out.println();
				}
				logger.info("list for nba->>>>>" + pitcherList.size());
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (BatchException e) {
			e.printStackTrace();
		}
		return pitcherList;

	}

	private List<TeamDetail> parsePichersforMLB(String url, String htmlData, String teamName) throws IOException {
		//OutputSettings settings = new OutputSettings();
		Document doc = Jsoup.parse(htmlData);
		System.out.println(htmlData);
		Elements trs = doc.select("table#team_batting_gamelogs tbody tr");
		//settings.escapeMode(EscapeMode.xhtml);
		// System.out.println(trs);
        List<TeamDetail> teamDtl = new ArrayList<>();
		for (Element tr : trs) {
			TeamDetail tm = new TeamDetail();
			String suffixUrl = "";
			boolean findOfSuffix = false;
			Elements tds = tr.select("td");
			for (Element td : tds) {
				if ((td.attr("data-stat") != null && td.attr("data-stat").equalsIgnoreCase("date_game"))
						&& (td.select("a").size() > 0)) {
					Element a = td.select("a").get(0);
					String date = td.attr("csk").trim().substring(0, td.attr("csk").trim().lastIndexOf("."));
					System.out.println(date);
					LocalDate localDate = LocalDate.parse(date);
					
					String text = tds.get(tds.indexOf(td)+1).text();
					String str[] = a.attr("href").split("/");
					tm.setOpponent(text+str[2]);
					
					tm.setDate(localDate);
					suffixUrl = "";
					suffixUrl = a.attr("href");
					findOfSuffix = true;
					break;
				}
			}

			if (findOfSuffix && (suffixUrl != null && !suffixUrl.isEmpty())) {
				String fullUrl = url + suffixUrl;
				System.out.println(fullUrl);
				String htmlData1="";
				try {
					htmlData1 = super.httpClientWrapper.getSiteNoRedirect(fullUrl, getHeader());
				} catch (BatchException e) {
					e.printStackTrace();
				}//sendGET(fullUrl);
				if (htmlData1 != null && !htmlData1.isEmpty()) {
					
					//String htmlTable = searchCommentTable(htmlData1, "top_plays");
					//if ((htmlTable != null && !htmlTable.isEmpty())) {
					//	doc = Jsoup.parse(htmlTable);
					//	System.out.println(htmlData1);
						/**
						 * New code
						 */
						String test =  searchCommentClassTable(htmlData1, "table_wrapper");
						Document tempdoc = Jsoup.parse(test);
						Elements pitchertables = tempdoc.select("table");
						
						Element table1 = pitchertables.get(0);
						Element table2 = pitchertables.get(1);
						String firstpitcher = "", secondpitcher ="";
						
						if(table1.toString().contains(teamName)) {
							firstpitcher = parsePitcher(table1);
							secondpitcher = parsePitcher(table2);
						}else if(table2.toString().contains(teamName)) {
							System.out.println(" OPP team");
							firstpitcher = parsePitcher(table2);
							secondpitcher = parsePitcher(table1);
						}
						tm.setStartingPitcher(firstpitcher);
						tm.setOppStartingPitcher(secondpitcher);
						
						//Elements tdsPichers = doc.select("table#top_plays tbody tr td");
						/*
						for (Element tdPicher : tdsPichers) {
							if (tdPicher.hasAttr("data-stat")
									&& tdPicher.attr("data-stat").equalsIgnoreCase("pitcher")) {
								//String startingPitcher = Jsoup.clean(tdPicher.html(), "", Whitelist.relaxed(),
									//	settings);
								String startingPitcher = tdPicher.text();//tdPicher.html().replaceAll("&nbsp;", " ");
								tm.setStartingPitcher(startingPitcher);
								break;
							}
						}
						*/
				//	}

					//htmlTable = searchCommentTable(htmlData1, "play_by_play");
					/*
					if ((htmlTable != null && !htmlTable.isEmpty())) {
						doc = Jsoup.parse(htmlTable);
						Elements tdsPichers = doc.select("table#play_by_play tbody tr td");
						for (Element tdPicher : tdsPichers) {
							if (tdPicher.hasAttr("data-stat")
									&& tdPicher.attr("data-stat").equalsIgnoreCase("pitcher")) {
								String oppStartingPitcher = tdPicher.text();//.replace("&nbsp;", " ");
								tm.setOppStartingPitcher(oppStartingPitcher);
								break;
							}
						}
					} 
					*/
				}
				teamDtl.add(tm);
			}
		}
		return teamDtl;
	}
	
	private static String parsePitcher(Element table) {
		Element firsttrtd = table.select("tbody tr").get(0).children().first();
		String pitcher = firsttrtd.select("a").text();
		return pitcher;
	}
	public List<TeamDetail> getTeamListWithShots(String url, String sportName, String teamCode) {
		String year = "2019";
         new JSONArray();
		String urlOfGameLog = url + "/teams" + "/" + teamCode + "/" + year + "_gamelog.html";
		System.out.println("urlOfGameLog" + urlOfGameLog);
		List<TeamDetail> teamDtl = new ArrayList<TeamDetail>();
		try {
			String htmlData = super.httpClientWrapper.getSiteNoRedirect(urlOfGameLog, getHeader());
			if (htmlData != null && !htmlData.isEmpty()) {
				if (sportName != null && sportName.equalsIgnoreCase("nhl")) {
					teamDtl = parseShotsOnGoalForNhl(htmlData);
				}
				logger.info("list for nhl->>>>>" + teamDtl.size());
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (BatchException e) {
			e.printStackTrace();
		}
		return teamDtl;
	}
	
	private List<TeamDetail> parseShotsOnGoalForNhl(String htmlData) throws IOException {

		Document doc = Jsoup.parse(htmlData);
		List<TeamDetail> array = new ArrayList<>();
		Elements trs = doc.select("table#tm_gamelog_rs tbody tr");

		boolean find = false;
		for (Element tr : trs) {
			TeamDetail obj = new TeamDetail();
			Elements tds = tr.select("td");
			for (Element td : tds) {
				//System.out.println(td);
				if ((td.attr("data-stat") != null && td.attr("data-stat").equalsIgnoreCase("date_game"))
						&& (td.select("a").size() > 0)) {
					Element a = td.select("a").get(0);
					LocalDate localDate = LocalDate.parse(a.html().trim());
					obj.setDate(localDate);
				}

				if ((td.attr("data-stat") != null && td.attr("data-stat").equalsIgnoreCase("opp_name"))
						&& (td.select("a").size() > 0)) {
					Element a = td.select("a").get(0);
					
					String text = tds.get(tds.indexOf(td)-1).text();
					String str[] = a.attr("href").split("/");
					obj.setOpponent(text+str[2]);
				}

				if (td.attr("data-stat") != null && td.attr("data-stat").equalsIgnoreCase("shots")) {
					obj.setShotsonGoal(td.html().trim());
				}

				if (td.attr("data-stat") != null && td.attr("data-stat").equalsIgnoreCase("goals")) {
					obj.setGoals(td.html().trim());
				}
				if (td.attr("data-stat") != null && td.attr("data-stat").equalsIgnoreCase("opp_goals")) {
					obj.setOppGoals(td.html().trim());
				}
				if (td.attr("data-stat") != null && td.attr("data-stat").equalsIgnoreCase("shots_against")) {
					obj.setOppShotsonGoal(td.html().trim());
					find = true;
				}
				
				//break;

			}

			if ((find && obj.getDate() != null) && (obj.getOpponent() != null && !obj.getOpponent().isEmpty()))
				array.add(obj);

		}
		return array;

	}
	public List<TeamDetail> getListWithPossessions(String url, String sportName, String teamCode, String teamName) throws BatchException {
		
		String year = "2019";

		String urlOfGameLog = url + "/teams" + "/" + teamCode + "/" + year + "/gamelog/";
		System.out.println("urlOfGameLog" + urlOfGameLog);
		List<TeamDetail> possessionsList = new ArrayList<TeamDetail>();
		try {
			String htmlData = super.httpClientWrapper.getSiteNoRedirect(urlOfGameLog, getHeader());
			
			if (htmlData != null && !htmlData.isEmpty()) {

				if (sportName != null && sportName.equalsIgnoreCase("nba")) {
					possessionsList = parsePossessionsForNBA_BasketBall(url,htmlData,teamCode,teamName);
				}
				logger.info("list for nba->>>>>" + possessionsList.size());

			}
		} catch (IOException e) {

			e.printStackTrace();
		}
		return possessionsList;
	}
	
	
	private List<TeamDetail> parsePossessionsForNBA_BasketBall(String url, String htmlData, String teamCode, String teamName) throws IOException, BatchException {
		String year = "2019";
		Document doc = Jsoup.parse(htmlData);
		List<TeamDetail> array = new ArrayList<>();
		Elements trs = doc.select("table#tgl_basic tbody tr");
		LocalDate localDate = null;
		for (Element tr : trs) {
			TeamDetail tm = new TeamDetail();
			String suffixUrl = "";
			boolean findOfSuffix = false;
			
			
			for (Element td : tr.select("td")) {

				if ((td.attr("data-stat") != null && td.attr("data-stat").equalsIgnoreCase("date_game"))
						&& (td.select("a").size() > 0)) {
					
					Element a = td.select("a").get(0);
					suffixUrl = "";
					suffixUrl = a.attr("href");
					findOfSuffix = true;
					String date = a.html();
					 localDate = LocalDate.parse(date);
					 String UrlOfAdvance = url+"/teams/"+teamCode+"/"+year+"/gamelog-advanced/";
					// String html = sendGET(UrlOfAdvance);
					 String html = super.httpClientWrapper.getSiteNoRedirect(UrlOfAdvance, getHeader());
					 //doc = connectionUrl(url);
					  Document doc1 = Jsoup.parse(html);
					  tm = getDAndORating(tm,html,doc1,date);

				}
				if (findOfSuffix) {
					break;
				}

			}

			if (findOfSuffix && (suffixUrl != null && !suffixUrl.isEmpty())) {
				String fullUrl = url + suffixUrl;
				System.out.println(fullUrl);
				String html = super.httpClientWrapper.getSiteNoRedirect(fullUrl, getHeader());
				//doc = Jsoup.connect(fullUrl).get();
				doc = Jsoup.parse(html);
				String htmlTable = searchCommentTable(html, "four_factors");

				if ((htmlTable != null && !htmlTable.isEmpty())) {
					doc = Jsoup.parse(htmlTable);
					if (doc != null) {
						Elements trsTable = doc.select("table#four_factors tbody tr");

						for (Element singleTr : trsTable) {
							
							String key = "";
							if ((singleTr.select("th") != null && singleTr.select("th").size() > 0)
									&& (singleTr.select("th").select("a") != null
											&& singleTr.select("th").select("a").size() > 0)) {
								key = singleTr.select("th").select("a").html();

							}
							if (key != null && !key.isEmpty()) {
								for (Element singleTd : singleTr.select("td")) {
									if (singleTd.attr("data-stat") != null && singleTd.attr("data-stat").equalsIgnoreCase("pace")) {
										tm.setDate(localDate);
										tm.setPossessions(singleTd.text());
										parseOverTimeData(url,suffixUrl,tm);
										array.add(tm);
										System.out.println(tm);
										//System.out.println(tm);
										break;
									}
									if(array.size()>0) {
										break;
									}
								}
								if(array.size()>0) {
									break;
								}
							}
						}
					}
				}
			}
		}
		return array;
	}

	
	private TeamDetail getDAndORating(TeamDetail detail, String html, Document doc1, String date1) {
		Element idEle = doc1.getElementById("all_tgl_advanced");
		Elements tableEles = idEle.select("table tbody tr");
		boolean findDAndOffRating = false;
		for (Element rowEle : tableEles) {
			Elements tdsEle = rowEle.select("td");
			//Elements selectedTr = null;
			for (Element tdEle : tdsEle) {
				String dataStatAttr = tdEle.attr("data-stat");
				if (dataStatAttr.equals("date_game")) {
					String date = tdEle.child(0).ownText();
					LocalDate ld = LocalDate.parse(date);
					if (date1.equals(date)) {
						detail.setDate(ld);
						findDAndOffRating = true;

						break;
					}

				}
			}
			if (findDAndOffRating) {
				for (Element tdEle : tdsEle) {
					String dataStatAttr = tdEle.attr("data-stat");
					if(dataStatAttr.equals("off_rtg")) {
						String offense = tdEle.ownText();
						detail.setOffRating(offense);
					}
					if (dataStatAttr.equals("def_rtg")) {
						String dRtg = tdEle.ownText();
						detail.setDefRating(dRtg);
					}
					if(detail.getOffRating()!=null && detail.getDefRating()!=null) {
						break;
					}
				}
               break;
			}
		}
		
		return detail;

	}
	private void parseOverTimeData(String url,String suffixUrl, TeamDetail tm) throws IOException {
		String overTimeUrl = suffixUrl.substring(suffixUrl.lastIndexOf("/"), suffixUrl.length());

		if ((overTimeUrl != null && !overTimeUrl.isEmpty())) {
			String fullUrl = url + "/boxscores/pbp" + overTimeUrl;
			System.out.println(fullUrl);
			//Document doc = Jsoup.connect(fullUrl).get();
			String html="";
			try {
				html = super.httpClientWrapper.getSiteNoRedirect(fullUrl, getHeader());
			} catch (BatchException e) {
				e.printStackTrace();
			}
			Document doc = Jsoup.parse(html);
			//boolean otFindOrNot = false;
			//String otTrId = "";
		
			Elements as = doc.select(".section_heading_text ul li a");
			String overtime="0:0";
			for(Element el : as) {
				String id = el.attr("href").replaceAll("[-+.^:,#]", "");
				if(id.contains("q")) {
					Element tr = doc.getElementById(id);
					String text = tr.text();
					//System.out.println(text);
					if(text.contains("OT") || text.contains("ot")) {
						Element nexttr = tr.nextElementSibling().nextElementSibling().nextElementSibling().nextElementSibling();
						Elements childrens = nexttr.children();
						//System.out.println(nexttr);
						String temp = childrens.get(0).text();
						overtime = sumOT(overtime,temp);
						//System.out.println();
						//System.out.println();
					}
					System.out.println();
				}
			}
			tm.setOvertime(overtime);
		}

	}
	
	private TeamDetail parseOverTimeDataNCAAB(String url, TeamDetail tm) throws IOException {

		if ((url != null && !url.isEmpty())) {
			String fullUrl = url.replace("/boxscores/", "/boxscores/pbp/");
			if(fullUrl.contains("2019-02-26-21-indiana"))
			System.out.println(fullUrl);
			String html="";
			try {
				html = super.httpClientWrapper.getSiteNoRedirect(fullUrl, getHeader());
				System.out.println(html);
			} catch (BatchException e) {
				e.printStackTrace();
			}
			Document doc = Jsoup.parse(html);
			Elements as = doc.select(".section_heading_text ul li a");
			String overtime="0:0";
			for(Element el : as) {
				String id = el.attr("href").replaceAll("[-+.^:,#]", "");
				String str =  el.text();
				if(id.contains("p") && str.toLowerCase().contains("ot")) {
					Element tr = doc.getElementById(id);
					Element nexttr = tr.nextElementSibling().nextElementSibling().nextElementSibling().nextElementSibling();
					Elements childrens = nexttr.children();
					String temp = childrens.get(0).text();
					overtime = sumOT(overtime,temp);
				}
			}
			tm.setOvertime(overtime);
		}
		return tm;
	}
	
	private String sumOT(String overtime, String temp) {
		int newminutes = 0;
		int newseconds = 0;
		if(temp!=null)
			if(temp.length()>0) {
				if(!(temp.equals("Time")) && !(overtime.equals("Time"))) {
					int minutes = getMinuites(temp);
					int seconds = getSeconds(temp);
					
					newminutes = getMinuites(overtime)+minutes;
					int number = getSeconds(overtime)+seconds;
					//int min = 0;
					if(number>60) {
						while(number>60) {
							number = number-60;
							newminutes++;
						}
					}
					newseconds = number;
				}
			}
				
		return newminutes+":"+newseconds;
	}
	
	private int getMinuites(String temp) {
		String arr[]=temp.split(":");
		int minutes = Integer.parseInt(arr[0]);
		return minutes;
	}
	
	private int getSeconds(String temp) {
		String arr[]=temp.split(":");
		String str[] = arr[1].split("\\.");
		int seconds = Integer.parseInt(str[0]);
		return seconds;
	}
	
	private static String getOverTimeValue(Element tr) {
		boolean recusivecheck = true;
		String overTimeValue = "";
		if (tr.nextElementSibling() != null && tr.nextElementSibling().select("td") != null) {
			for (Element td : tr.nextElementSibling().select("td")) {
				if (td.html().contains("overtime")) {
					recusivecheck = false;
					overTimeValue = td.previousElementSibling().html();
				}
			}
		}
		if (recusivecheck) {
			return getOverTimeValue(tr.nextElementSibling());
		}
		return overTimeValue;
	}
	
	public String searchCommentTable(String html, String search) {
		logger.info("Entering searchCommentTable");
		String newHtml = "";
		while (true) {
			int start = html.indexOf("<!--");
			int end = html.indexOf("-->");
			String temp = html.substring(start + 4, end);
			if (temp.contains(search)) {
				newHtml = temp;
				break;
			} else {
				temp = html.substring(start, end + 3);
				html = html.replace(temp, "");
			}
		}

		logger.info("Exiting searchCommentTable");
		return newHtml;
	}
	
	public String searchCommentClassTable(String html, String search) {
		logger.info("Entering searchCommentTable");
		String newHtml = "";
		
		while (true) {
			int start = html.indexOf("<!--");
			int end = html.indexOf("-->");
			if(end==-1) break;
			String temp = html.substring(start + 4, end);
			if (temp.contains(search)) {
				newHtml += temp;
				temp = html.substring(start, end + 3);
				html = html.replace(temp, "");
				if(html.length()<20)
					break;
			} else {
				temp = html.substring(start, end + 3);
				html = html.replace(temp, "");
				if(html.length()<10)
					break;
			}
		}
		logger.info("Exiting searchCommentTable");
		return newHtml;
	}

	String array[][]= {
			{"nba","https://www.basketball-reference.com/leagues/NBA_2019.html"},
			{"ncaab","https://www.sports-reference.com/cbb/seasons/2019.html"},
	};
	
	public List<TeamData> processNCAAB(String baseurl, String html, String sport,String year) throws BatchException {
		//List<TeamData> list = new ArrayList<>();
		Document doc = Jsoup.parse(html);
		Element conferences = doc.getElementById("conference-summary");
		
		TeamContainer tc = new TeamContainer("vegasinsider", sport);
		List<String> UITeams = tc.getTeamsList();
		Collections.sort(UITeams);
		
		Map<String,String> mapTeamCode = iterateIdElements(conferences);
		//System.out.println(teamUrl);
		//------Call Second Step
		Set<String> keySet = mapTeamCode.keySet();
		JSONArray resArry = new JSONArray();
		for(String key : keySet) {
			String url = baseurl+"conferences/"+key+"/"+year+".html";
			//doc = connectionUrl(url);
			html = super.httpClientWrapper.getSiteNoRedirect(url, getHeader());
			doc = Jsoup.parse(html);
			System.out.println(html);
			Element standingEle = doc.getElementById("standings");
			Map<String,String> standingStepMap = iterateForStanding(standingEle);
			Set<String> standKeySet = standingStepMap.keySet();
			
			List<TeamData> listTeamData = new ArrayList<TeamData>();
			for(String stndKey : standKeySet) {
				String link = baseurl.replace("/cbb/","");
				url=link+standingStepMap.get(stndKey);
				///cbb/schools/michigan-state/2019-gamelogs-advanced.html
				//doc = connectionUrl(url);
				html = super.httpClientWrapper.getSiteNoRedirect(url, getHeader());
				
				TeamData teamData = new TeamData();
				List<TeamDetail> teamDetail = parseAdvancePage(baseurl, stndKey,html);
				teamData.setSport(sport);
				teamData.setTeam(getMappingTeamName(stndKey, sport, UITeams));
				teamData.setDetail(teamDetail);
				teamData.setId(stndKey.replaceAll(" ", "")+"ncaab");
				 System.out.println("Team "+stndKey+", "+teamDetail);
				listTeamData.add(teamData);
				
				  JSONObject urlObj = new JSONObject(); urlObj.put("team_code", key);
				  urlObj.put("team_name", stndKey);
				  urlObj.put(stndKey, teamDetail); 
				  resArry.put(urlObj);
				 
			}
			teamDataRepo.saveAll(listTeamData);
		}
		return null;
		
	}
	
private String getMappingTeamName(String oldName, String sport,List<String> UITeams) {
		
		for(String smteam : UITeams) {
			String chkstr="";
			String temp[] = smteam.split(",");
			if(sport.equals("ncaab") || sport.equals("ncaaf")) {
				try{chkstr = temp[2];}catch(Exception e) {
					//System.out.println("Error: "+smteam);
				}
			}
			if(oldName.equals(chkstr))
				return temp[0];
		}
		
		return "";
	}

	public List<TeamDetail> parseAdvancePage(String url, String teamCode, String html) {
		Document doc = Jsoup.parse(html);
		System.out.println(html);
		//JSONArray arry = new JSONArray();
		List<TeamDetail> list = new ArrayList<>();
		Element idEle = doc.getElementById("sgl-advanced");
		Elements tableEles = idEle.select("tbody tr");
		for(Element rowEle : tableEles) {
			Elements tdsEle = rowEle.select("td");
			JSONObject rowObj = new JSONObject();
			TeamDetail teamDetail = new TeamDetail();
			
			for(Element tdEle : tdsEle) {
				
				String dataStatAttr = tdEle.attr("data-stat");
				if(dataStatAttr.equals("date_game")) {
					String date = tdEle.child(0).ownText();
					String link = tdEle.child(0).attr("href");
					String otlink = url.replace("/cbb/","")+link;
					int index = otlink.indexOf("");
					try {
						 teamDetail = parseOverTimeDataNCAAB(otlink, teamDetail);
					} catch (IOException e) {
						e.printStackTrace();
					}
					rowObj.put("date", date);
					teamDetail.setDate(parseDate(date));
				}
				if(dataStatAttr.equals("off_rtg")) {
					String oRtg=tdEle.ownText();
					rowObj.put("oRtg", oRtg);
					teamDetail.setOffRating(oRtg);
				}
				if(dataStatAttr.equals("def_rtg")) {
					String dRtg = tdEle.ownText();
					rowObj.put("dRtg", dRtg);
					teamDetail.setDefRating(dRtg);
				}
				
				if(dataStatAttr.equals("pace")) {
					String possessions = tdEle.ownText();
					teamDetail.setPossessions(possessions);
				}
			}
			
			list.add(teamDetail);
			//arry.put(rowObj);
		}
		return list;
	}

	private LocalDate parseDate(String str) {
		LocalDate date = null;
		
		String arr[]= str.split("-");
		date = LocalDate.of(Integer.parseInt(arr[0]), Integer.parseInt(arr[1]), Integer.parseInt(arr[2]));
		return date;
	}
	public Map<String,String> iterateForStanding(Element idEles) {
		Map<String,String> mapTeamCode = new HashMap<String,String>();
		Elements elsTable = idEles.select("tbody tr td a");  
		for(Element ele : elsTable) {
			if(ele.attr("href")!=null) {
				String hrefStr = ele.attr("href");
				if(hrefStr.startsWith("/cbb/schools/")) {
					//int index = hrefStr
					String teamFullName = ele.ownText();  //-gamelogs-advanced
					hrefStr = hrefStr.replace(".", "-gamelogs-advanced.");
					mapTeamCode.put(teamFullName,hrefStr);
				}
				
			}
			
		}
		return mapTeamCode;
	}

	public Map<String,String> iterateIdElements(Element idEles) {
		Map<String,String> mapTeamCode = new HashMap<String,String>();
		Elements elsTable = idEles.select("tbody tr td a");
		for(Element ele : elsTable) {
			if(ele.attr("href")!=null) {
				String hrefStr = ele.attr("href");
				if(hrefStr.startsWith("/cbb/conferences/")) {
					String teamFullName = ele.ownText();
					String split[]=hrefStr.split("/");
					mapTeamCode.put(split[3],teamFullName);
				}
			}
		}
		return mapTeamCode;
	}

	
	public List<TeamDetail> parseAdvancePage(String url, String teamCode,Document doc) {

		List<TeamDetail> detailList = new ArrayList<TeamDetail>();
		Element idEle = doc.getElementById("all_tgl_advanced");
		Elements tableEles = idEle.select("table tbody tr");
		for(Element rowEle : tableEles) {
			Elements tdsEle = rowEle.select("td");
			TeamDetail detail = new TeamDetail();
			for(Element tdEle : tdsEle) {
				String dataStatAttr = tdEle.attr("data-stat");
				if(dataStatAttr.equals("date_game")) {
					String date = tdEle.child(0).ownText();
					LocalDate ld = LocalDate.parse(date);
					detail.setDate(ld);
				}
				if(dataStatAttr.equals("off_rtg")) {
					String oRtg=tdEle.ownText();
					detail.setOffRating(oRtg);
				}
				if(dataStatAttr.equals("def_rtg")) {
					String dRtg = tdEle.ownText();
					detail.setDefRating(dRtg);
				}
			}
			detailList.add(detail);
		}
		return detailList;
	}
	
/*	private Document connectionUrl(String url) throws IOException {
		Connection conn = Jsoup.connect(url);
		String html = conn.execute().body(); //all_confs_standings_E   //all_confs_standings_W
		Document doc = Jsoup.parse(html);
		return doc;
	}*/
	
	
	public void iterateIdElements(Element idEles,Map<String,String> mapTeamCode) {
		Elements elsTable = idEles.select("table tbody tr th a");
		for(Element ele : elsTable) {
			if(ele.attr("href")!=null) {
				String hrefStr = ele.attr("href");
				String teamFullName = ele.ownText();
				String split[]=hrefStr.split("/");
				mapTeamCode.put(split[2],teamFullName);
			}
		}
	}
	
	
	@Override
	public void updatePlayerData(String sport) {
	}

	@Override
	public void saveCurrentData() {
	}

	
}
