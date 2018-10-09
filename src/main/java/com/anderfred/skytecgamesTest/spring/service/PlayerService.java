package com.anderfred.skytecgamesTest.spring.service;


import com.anderfred.skytecgamesTest.spring.entity.Player;

import java.util.Optional;

public interface PlayerService {
    Player getPlayerByName(String name);

    <S extends Player> S save(S s);

    void hit(Player hero, Player enemy);

    void afterFight(Player player, Integer hp, Integer rating);

    Optional<Player> findById(Integer integer);
}
