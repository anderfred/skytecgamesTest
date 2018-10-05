package com.anderfred.skytecgamesTest.repository;

import com.anderfred.skytecgamesTest.entity.Player;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlayerRepository extends CrudRepository<Player, Integer> {
    Optional<Player> getPlayerByName(String name);
}
