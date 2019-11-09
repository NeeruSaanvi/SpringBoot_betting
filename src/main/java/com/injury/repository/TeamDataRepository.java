package com.injury.repository;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.injury.dto.TeamData;

/**
*  @author SureshKoumar @Pinesucceed
*  Aug 8, 2019
*/

@Repository
public interface TeamDataRepository extends MongoRepository<TeamData, String>{
	
	public List<TeamData> findBySport(String sport, Sort sort);
	
	@Query("{'sport':?0, 'team':?1}")
	public List<TeamData> findTeamofSport(String sport, String team, Sort sort);
	
	public void deleteBySport(String sport);
}
