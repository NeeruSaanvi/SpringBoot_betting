package com.injury.repository;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import com.injury.entity.Player;

/**
*  @author SureshKoumar @Pinesucceed
*  Jun 28, 2019
*/

@Repository
public interface PlayerRepository extends MongoRepository<Player, String>{

	//{"teamName" : "Oakland Raiders","playerName":{ "$regex" : "Derek Carr"}}
	//@Query("{'sportName':?0, 'teamName':?1, 'playerName' : { '$regex' : ?2 , $options: 'i'}}")
	@Query("{'sportName':?0, 'teamName':?1, 'playerName' : ?2}")
	public Player findPlayer(String sportName, String teamName, String playerName);
	
	@Query("{'sportName':?0, 'teamName':?1}")
	public List<Player> findPlayerofTeam(String sportName, String teamName, Sort sort);
	
	@Query("{'sportName':?0}")
	public List<Player> findPlayers(String sportName);

	public void deleteBySportName(String sport);
	
}
