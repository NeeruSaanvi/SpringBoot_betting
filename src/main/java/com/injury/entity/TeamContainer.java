package com.injury.entity;
/**
*  @author SureshKoumar @Pinesucceed
*  Mar 26, 2019
*/

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.regex.Pattern;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TeamContainer {
	private static Logger logger = LoggerFactory.getLogger("TeamContainer");
	private String siteName;
	private static String sport;
	private static List<TeamContainer> sites;
	
	
	
	public TeamContainer() {
		super();
	}

	public TeamContainer(String siteName, String sport) {
		this.siteName = siteName;
		this.sport = sport;
		try {
			final Properties prop = new Properties();
			final InputStream in = TeamContainer.class.getClassLoader().getResourceAsStream(sport+"playerteams.properties");
			if(in!=null) {
				prop.load(in);
				in.close();
			}
			sites = getPropertyList(prop,sport+".team");
		}catch(Exception e) {
			logger.error("cannot read the teams file");
		}
	}

	public static void main(String[] args) {
		//System.out.println(getTeamsList());
	}
	
	public List<String> getTeamsList(){
		logger.info("Entering getBetSitesList()");
		final List<String> siteNames = new ArrayList<>();
		sites.forEach(obj->{
			String siteName = obj.getSiteName();
			siteNames.add(siteName);
		});
		
		logger.info("Exiting getBetSitesList()");
		return siteNames;
	}
	
	public static List<TeamContainer> getPropertyList(Properties properties, String name){
		logger.info("Entering getPropertyList()");
		List<TeamContainer> result = new ArrayList<>();
	
		for(Entry<Object, Object> entry : properties.entrySet()) {
			if (((String)entry.getKey()).matches("^" + Pattern.quote(name) + "\\.\\d+$"))
	        {
				String key = (String)entry.getKey();
				//System.out.println(key);
				logger.debug("Key: "+ key);
				if(!key.contains("#")) {
					final TeamContainer betSiteContainer = new TeamContainer();
					betSiteContainer.setSiteName((String) entry.getValue());
					logger.debug("BetSiteContainer: " + betSiteContainer);
					result.add(betSiteContainer);
				}
	        }
		}
		
		logger.info("Exiting getPropertyList()");
		return result;
	}

	public String getSiteName() {
		return siteName;
	}

	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}

	public static List<TeamContainer> getSites() {
		return sites;
	}

	public static void setSites(List<TeamContainer> sites) {
		TeamContainer.sites = sites;
	}
}
