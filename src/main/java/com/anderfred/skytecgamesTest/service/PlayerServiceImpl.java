package com.anderfred.skytecgamesTest.service;

import com.anderfred.skytecgamesTest.entity.Player;
import com.anderfred.skytecgamesTest.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PlayerServiceImpl implements PlayerService {
    @Autowired
    PlayerRepository playerRepository;

    @Override
    public Player getPlayerByName(String name) {
        return playerRepository.getPlayerByName(name).get();
    }
}
