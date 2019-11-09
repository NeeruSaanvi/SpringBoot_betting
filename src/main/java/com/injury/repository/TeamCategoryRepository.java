package com.injury.repository;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.injury.entity.Team;
import com.injury.entity.TeamCategory;


/**
*  @author SureshKoumar @Pinesucceed
*  Jun 28, 2019
*/

@Repository
public interface TeamCategoryRepository extends MongoRepository<TeamCategory, String>{
	public List<TeamCategory> findBySport(String sport,Sort sort);
}
