package com.anderfred.skytecgamesTest.spring.repository;

import com.anderfred.skytecgamesTest.spring.entity.Player;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlayerRepository extends CrudRepository<Player, Integer> {
    Optional<Player> getPlayerByName(String name);
}
