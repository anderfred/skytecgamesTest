package com.anderfred.skytecgamesTest.spring.service;

import com.anderfred.skytecgamesTest.spring.entity.Player;
import com.anderfred.skytecgamesTest.spring.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PlayerServiceImpl implements PlayerService {
    @Autowired
    PlayerRepository playerRepository;

    @Override
    public Player getPlayerByName(String name) {
        if (playerRepository.getPlayerByName(name).isPresent())
            return playerRepository.getPlayerByName(name).get();
        return null;
    }

    @Override
    public <S extends Player> S save(S s) {
        return playerRepository.save(s);
    }

    @Override
    public void hit(Player player1, Player player2) {
        Player hero = getPlayerByName(player1.getName());
        Player enemy = getPlayerByName(player2.getName());
        enemy.setHealth(enemy.getHealth() - hero.getDamage());
        save(enemy);
    }

    @Override
    public void afterFight(Player playerAfter, Integer hp, Integer rating) {
        Player player = getPlayerByName(playerAfter.getName());
        player.setDamage(playerAfter.getDamage() + 1);
        player.setHealth(hp + 1);
        player.setRating(playerAfter.getRating() + rating);
        save(player);
    }


    @Override
    public Optional<Player> findById(Integer integer) {
        return playerRepository.findById(integer);
    }
}
