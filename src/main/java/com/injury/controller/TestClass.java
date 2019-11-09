package com.injury.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.injury.dto.EspnYPPObject;
import com.injury.dto.TeamDetail;

/**
*  @author SureshKoumar @Pinesucceed
*  Aug 21, 2019
*/

public class TestClass {
	static Logger logger = LoggerFactory.getLogger("TestClass");
	
	public static void main(String[] args) {
		String year = "2018";
		String htmlData = ""; 
		
	//	String url="https://www.pro-football-reference.com";
		String teamName = "Arizona Cardinals";

		Map<String,EspnYPPObject> map = getYPPData(year);
		Document teamdoc = Jsoup.parse(htmlData);
		Element table = teamdoc.getElementById("games");
		
		//System.out.println(table);
		List<TeamDetail> array = new ArrayList<>();
		
		Elements trs = table.select("tbody tr");
		LocalDate previousDate = null;
		for(Element tr : trs) {
			TeamDetail tm = new TeamDetail();

			String datastat = tr.attr("data-stat");
			String week = "Week-"+tr.text();
			if(datastat.equals("week_num"))
				tm.setWeek(week);
			for (Element td : tr.select("td")) {
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
			       break;
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
		
	}
	
	
	private static Map<String,EspnYPPObject> getYPPData(String year) {
		
		Map<String,EspnYPPObject> map = new LinkedHashMap<>();
		
		String espnurl = "https://www.espn.in/nfl/scoreboard/_/year/"+year+"/seasontype/2/week/";
		try {
			for(int i = 1; i<18; i++) {
				String espnhtml = sendGET(espnurl+i);
				
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
					String ypphtml = sendGET(yppurl);
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
						//System.out.println("Week-"+i+" Date "+date+" displayName"+displayName);
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
				//break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return map;
	}

	private static String sendGET(String GET_URL) throws IOException {
		
		final String USER_AGENT = "Mozilla/5.0";

		URL obj = new URL(GET_URL);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		con.setRequestMethod("GET");
		con.setRequestProperty("User-Agent", USER_AGENT);
		int responseCode = con.getResponseCode();
		StringBuffer response = new StringBuffer();

		if (responseCode == HttpURLConnection.HTTP_OK) { // success
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
		} else {
			System.out.println("GET request not worked"+GET_URL);
			response = new StringBuffer("");
		}
		return response.toString();
	}
	
	public static String searchCommentTable(String html, String search) {
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
}
