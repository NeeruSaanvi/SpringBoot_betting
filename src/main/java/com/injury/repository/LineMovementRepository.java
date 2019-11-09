package com.injury.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.injury.entity.LineMovement;



/**
*  @author SureshKoumar @Pinesucceed
*  May 23, 2019
*/

@Repository
public interface LineMovementRepository extends MongoRepository<LineMovement, String>{
	
	@Query("{'year':?0,'sportType':?1, '$or' : [{'hometeam' : ?2}, {'visitorteam' : ?2 }],'lineType':?3,'gameType':?4,'sportsbook':?5 }")
	public List<LineMovement> getLinesByTeam(Integer year, String sporttype, String team, String linetype, String gametype, String sportsbook);

	///////////////detail page API
	@Query("{'sportType':?0, '$or' : [{'hometeam' : ?1 , 'visitorteam' : ?2 } , {'hometeam' : ?2 , 'visitorteam' : ?1 }], 'lineType':?3,'gameType':?4,'sportsbook':?5, 'eventDate' : {$gt : ?6, $lt : ?7} }")
	public List<LineMovement> getLastLinesByTeam(String sporttype, String team, String opponent, String linetype, String gametype, String sportsbook, LocalDate startDate, LocalDate endDate, Sort sort);
	///////////////detail page API
	@Query("{'sportType':?0, '$or' : [{'hometeam' : ?1 , 'visitorteam' : ?2 } , {'hometeam' : ?2 , 'visitorteam' : ?1 }], 'lineType':?3,'gameType':?4,'sportsbook':?5 }")
	public List<LineMovement> getLastLinesByTeam(String sporttype, String team, String opponent, String linetype, String gametype, String sportsbook, Sort sort);
	///////////////detail page API	with dates
	@Query("{'sportType':?0, '$or' : [{'hometeam' : ?1}, {'visitorteam' : ?1 }],'lineType':?2,'gameType':?3,'sportsbook':?4, eventDate : {$gt : ?5, $lt : ?6} }")
	public List<LineMovement> getLastLinesByTeam(String sporttype, String team, String linetype, String gametype, String sportsbook, LocalDate startDate, LocalDate endDate, Sort sort);
	///////////////detail page API	without date
	@Query("{'sportType':?0, '$or' : [{'hometeam' : ?1}, {'visitorteam' : ?1 }],'lineType':?2,'gameType':?3,'sportsbook':?4 }")
	public List<LineMovement> getLastLinesByTeam(String sporttype, String team, String linetype, String gametype, String sportsbook, Sort sort);
	
	
	@Query("{'year':?0,'sportType':?1, '$or' : [{'hometeam' : ?2}, {'visitorteam' : ?2 }],'lineType':?3,'gameType':?4,'sportsbook':?5 }")
	public List<LineMovement> getLinesByTeam(Integer year, String sporttype, String team, String linetype, String gametype, String sportsbook, Sort sort);
	
	@Query("{'sportType':?0, '$or' : [{'hometeam' : ?1}, {'visitorteam' : ?1 }], 'lineType':?2, 'gameType':?3, 'sportsbook':?4}")
	public List<LineMovement> getTeamData(String sporttype, String team,  String linetype,  String gametype, String sportsbook, Sort sort);
	
	@Query("{'sportType':?0, 'lineType':?1,'gameType':?2,'sportsbook':?3}")
	public List<LineMovement> getSummaryData(String sporttype, String linetype, String gametype, String sportsbook, Sort sort);
	
	
	@Query("{'sportType':?0, 'lineType':?1,'gameType':?2,'sportsbook':?3, 'eventDate' : {$gt : ?4, $lt : ?5}}")
	public List<LineMovement> getSummaryDataByDates(String sporttype, String linetype, String gametype, String sportsbook, LocalDate start, LocalDate end, Sort sort);
	
	//@DeleteQuery(value="{sportType:$0}")
	public List<LineMovement> deleteBySportType(String sporttype);

}
