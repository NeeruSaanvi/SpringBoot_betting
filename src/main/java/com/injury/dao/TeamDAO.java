package com.injury.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.injury.entity.Team;

/**
*  @author SureshKoumar @Pinesucceed
*  Jul 12, 2019
*/

@Repository
public interface TeamDAO {
	public void updateTeams(List<Team> teams);
}
