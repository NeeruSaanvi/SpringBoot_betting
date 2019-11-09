package com.injury.dao;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.injury.entity.Team;
import com.injury.repository.TeamsRepository;

/**
*  @author SureshKoumar @Pinesucceed
*  Jul 12, 2019
*/

@Service
public class TeamDAOImpl implements TeamDAO{
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private TeamsRepository teamsRepo;
	
	@Override
	public void updateTeams(List<Team> teams) {
		logger.info("Entering updateTeams");
		teamsRepo.saveAll(teams);
		logger.info("Exiting updateTeams");
	}
	
}
