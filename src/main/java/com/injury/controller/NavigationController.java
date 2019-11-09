package com.injury.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.injury.dto.TeamData;
import com.injury.repository.TeamDataRepository;

/**
*  @author SureshKoumar @Pinesucceed
*  Jul 11, 2019
*/

@Controller
public class NavigationController {
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private TeamDataRepository teamDataRepo;
	
	@GetMapping(value = "/{pagename}")
	public String returnPageName(@PathVariable String pagename) {
		logger.info("Entering by"+pagename);
		String pageName = "";
		switch(pagename) {
		case "teamsconferences":
			pageName=pagename;
			break;
		case "home":
			pageName=pagename;
			break;
		case "nfl":
			pageName=pagename;
			break;
		case "nba":
			pageName=pagename;
			break;
		case "ncaaf":
			pageName=pagename;
			break;
		case "ncaab":
			pageName=pagename;
			break;
		case "mlb":
			pageName=pagename;
			break;
		case "nhl":
			pageName=pagename;
			break;
			default:
				pageName = pagename;
		}
		logger.info("Exiting by"+pageName);
		return pageName;
	}
}
