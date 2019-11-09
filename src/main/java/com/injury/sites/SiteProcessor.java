package com.injury.sites;

import java.util.ArrayList;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import com.injury.dto.TeamData;
import com.injury.errorhandling.BatchException;
import com.injury.httpclient.HttpClientWrapper;
import com.injury.repository.PlayerRepository;
import com.injury.repository.TeamCategoryRepository;
import com.injury.repository.TeamDataRepository;
import com.injury.repository.TeamsRepository;

/**
*  @author SureshKoumar @Pinesucceed
*  Jun 28, 2019
*/

public abstract class SiteProcessor {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	protected HttpClientWrapper httpClientWrapper;
	protected InjuryDataProcessor dataProcessor;
	protected TeamsRepository teamsRepo;
	protected TeamCategoryRepository teamsCatRepo;
	protected PlayerRepository playerRepo;
	protected ApplicationContext context;
	protected TeamDataRepository teamDataRepo;
	
	protected static List<String> linesList = new ArrayList<>();
	protected static List<String> gamesList = new ArrayList<>();
	protected static List<String> sportsbookList = new ArrayList<>();
	protected static List<String> sportsList = new ArrayList<>(); 
	
	static {
		sportsList.add("nba");
		sportsList.add("nhl");
		sportsList.add("mlb");
		sportsList.add("ncaab");
		//sportsList.add("nfl");
		//sportsList.add("ncaaf");
		//sportsList.add("wnba");
		
		linesList.add("spread");
		linesList.add("moneyline");
		linesList.add("total");
		
		gamesList.add("game");
		gamesList.add("first");
		gamesList.add("second");

		sportsbookList.add("VI CONSENSUS LINE MOVEMENTS");
		sportsbookList.add("BOOKMAKER LINE MOVEMENTS");
		sportsbookList.add("PINNACLESPORTS LINE MOVEMENTS");
		sportsbookList.add("WESTGATE SUPERBOOK LINE MOVEMENTS");
		sportsbookList.add("CAESARS/HARRAH'S LINE MOVEMENTS");

	}
	
	
	public SiteProcessor() {
		logger.info("initialing HttpClient");
		httpClientWrapper = new HttpClientWrapper();
		
		try {
			httpClientWrapper.setupHttpClient("None");
		} catch (BatchException e) {
			e.printStackTrace();
		}
	}

	public SiteProcessor(String proxyName){
		logger.info("initialing HttpClient");
		httpClientWrapper = new HttpClientWrapper();
		
		try {
			httpClientWrapper.setupHttpClient(proxyName);
		} catch (BatchException e) {
			e.printStackTrace();
		}
	}
	
	public SiteProcessor(String accountSoftware, String host, String username, String password, boolean isMobile,
			boolean showRequestResponse) {
		super();
		logger.info("Entering SiteProcessor()");
		logger.debug("accountSoftware: " + accountSoftware);
		logger.debug("host: " + host);
		logger.debug("username: " + username);
		logger.debug("password: " + password);
		logger.debug("isMobile: " + isMobile);
		logger.debug("showRequestResponse: " + showRequestResponse);

		httpClientWrapper = new HttpClientWrapper(host, username, password);
		httpClientWrapper.setMobile(isMobile);
		httpClientWrapper.setShowRequestResponse(showRequestResponse);

		
		logger.info("Exiting SiteProcessor()");
	}
	
	public SiteProcessor(String accountSoftware, String host, String username, String password) {
		super();
		logger.info("Entering SiteProcessor()");
		logger.debug("accountSoftware: " + accountSoftware);
		logger.debug("host: " + host);
		logger.debug("username: " + username);
		logger.debug("password: " + password);

		httpClientWrapper = new HttpClientWrapper(host, username, password);

		logger.info("Exiting SiteProcessor()");
	}
	
	/*
	 * @PostConstruct public void inititalize() { System.out.println("done");
	 * System.out.println(teamsRepo); //dataProcessor.setTeamsRepo(teamsRepo);
	 * //dataProcessor.setTeamsCatRepo(teamsCatRepo); }
	 */
	
	
	//abstract public void saveData(String sport);
	public abstract void updatePlayerData(String sport);
	public abstract List<TeamData> getTeamData(String sportName);
	public abstract void saveCurrentData();
	
	protected String getSite(String actionName) throws BatchException {
		logger.info("Entering getSite()");
		logger.debug("actionName: " + actionName);

		List<NameValuePair> retValue = httpClientWrapper.getSitePage(actionName, null, setupHeader(false));
		String xhtml = httpClientWrapper.getCookiesAndXhtml(retValue);

		// Get webapp name
		String webappName = httpClientWrapper.getWebappname();
		if (webappName == null) {
			webappName = "";
		}

		// Check for redirect
		retValue = httpClientWrapper.checkForRedirect(retValue, httpClientWrapper.getHost(), setupHeader(false), null,
				webappName + "/");
		xhtml = httpClientWrapper.getCookiesAndXhtml(retValue);

		logger.info("Exiting getSite()");
		return xhtml;
	}
	
	protected String getSiteWithCheck(String actionName, String locationName) throws BatchException {
		logger.info("Entering getSiteWithCheck()");
		logger.debug("actionName: " + actionName);

		List<NameValuePair> retValue = httpClientWrapper.getSitePage(actionName, null, setupHeader(false));
		String xhtml = httpClientWrapper.getCookiesAndXhtml(retValue);
		logger.trace("XHTML: " + xhtml);

		// Get webapp name
		String webappName = httpClientWrapper.getWebappname();
		if (webappName == null) {
			webappName = "";
		}

		// Check for redirect
		retValue = httpClientWrapper.checkForRedirectWithLocation(retValue, httpClientWrapper.getHost(),
				setupHeader(false), null, webappName + "/", locationName, xhtml);
		xhtml = httpClientWrapper.getCookiesAndXhtml(retValue);

		logger.info("Exiting getSiteWithCheck()");
		return xhtml;
	}
	protected String getSiteNoBr(String actionName) throws BatchException {
		logger.info("Entering getSiteNoBr()");
		logger.debug("actionName: " + actionName);

		List<NameValuePair> retValue = httpClientWrapper.getSitePageNoBr(actionName, null, setupHeader(false));
		String xhtml = httpClientWrapper.getCookiesAndXhtml(retValue);

		// Get webapp name
		String webappName = httpClientWrapper.getWebappname();
		if (webappName == null) {
			webappName = "";
		}

		// Check for redirect
		retValue = httpClientWrapper.checkForRedirectNoBr(retValue, httpClientWrapper.getHost(), setupHeader(false), null,
				webappName + "/");
		xhtml = httpClientWrapper.getCookiesAndXhtml(retValue);

		logger.info("Exiting getSiteNoBr()");
		return xhtml;
	}
	protected List<NameValuePair> setupHeader(boolean withContentType) {
		logger.info("Entering setupHeader()");

		final List<NameValuePair> headerValuePairs = new ArrayList<NameValuePair>(1);
		if (withContentType) {
			headerValuePairs.add(new BasicNameValuePair("Content-Type", "application/x-www-form-urlencoded"));
		}
		headerValuePairs.add(new BasicNameValuePair("Upgrade-Insecure-Requests", "1"));
		headerValuePairs.add(new BasicNameValuePair("Referer", this.httpClientWrapper.getPreviousUrl()));

		logger.info("Exiting setupHeader()");
		return headerValuePairs;
	}

	protected List<NameValuePair> getHeader(){
		List<NameValuePair> headerValuePairs = new ArrayList<NameValuePair>(1);
		//headerValuePairs.add(new BasicNameValuePair("Host", "www.sports-reference.com"));
		headerValuePairs.add(new BasicNameValuePair("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:67.0) Gecko/20100101 Firefox/67.0"));
		headerValuePairs.add(new BasicNameValuePair("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8"));
		headerValuePairs.add(new BasicNameValuePair("Accept-Language", "en-US,en;q=0.5"));
		headerValuePairs.add(new BasicNameValuePair("Accept-Encoding", "gzip, deflate, br"));
		headerValuePairs.add(new BasicNameValuePair("Connection", "keep-alive"));
		headerValuePairs.add(new BasicNameValuePair("Cookie", "_ga=GA1.2.1935716834.1561643817; _gid=GA1.2.199347490.1561643817; _fsloc=?i=IN&c=Jaipur; _pubcid=a421ac09-f715-499b-950e-6b2ffcc469e4; __gads=ID=c7fa295833f2fcc3:T=1561643879:S=ALNI_MaSEYGPkBFGHF3DdL8IbPqdu9nx6g; _fsuid=c6bd7e2d-494f-4cab-b9cf-369f7673dd81; __vrz=1.16.12; _fbp=fb.1.1561643824411.356218207; __qca=P0-336944371-1561643824091; __beaconTrackerID=9800y5c3c; auth_checked=true; is_live=true; SR_user=; srcssfull=yes; _gat=1; _gat_combined_tracker=1; _gat_sr_tracker=1"));
		headerValuePairs.add(new BasicNameValuePair("Upgrade-Insecure-Requests", "1"));
		headerValuePairs.add(new BasicNameValuePair("Cache-Control", "max-age=0"));
		headerValuePairs.add(new BasicNameValuePair("TE", "Trailers"));
				
		return headerValuePairs;
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

	public ApplicationContext getContext() {
		return context;
	}

	public void setContext(ApplicationContext context) {
		this.context = context;
	}

	public InjuryDataProcessor getDataProcessor() {
		return dataProcessor;
	}

	public void setDataProcessor(InjuryDataProcessor dataProcessor) {
		this.dataProcessor = dataProcessor;
	}

	public PlayerRepository getPlayerRepo() {
		return playerRepo;
	}

	public void setPlayerRepo(PlayerRepository playerRepo) {
		this.playerRepo = playerRepo;
	}

	public TeamDataRepository getTeamDataRepo() {
		return teamDataRepo;
	}

	public void setTeamDataRepo(TeamDataRepository teamDataRepo) {
		this.teamDataRepo = teamDataRepo;
	}

	
}
