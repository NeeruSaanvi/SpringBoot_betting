package com.injury.repository;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.injury.entity.Team;


/**
*  @author SureshKoumar @Pinesucceed
*  Apr 25, 2019
*/

@Repository
public interface TeamsRepository extends MongoRepository<Team, String>{
	//@Query("{'sport':?0, groupBy: 'teamcategory'}")
	public List<Team> findBySport(String sport,Sort sort);

	@Query("{'sport':?0, 'teamcategory':?1}")
	public List<Team> findBySportCategory(String sport, String category, Sort sort);
	
	//@org.springframework.data.jpa.repository.Query("select distinct teamcategory from Team")
	public List<Team> findDistinctByTeamcategory();
	
	public void deleteById(String id);
	
	public void deleteBySport(String sport);
}
