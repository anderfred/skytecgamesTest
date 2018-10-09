package com.anderfred.skytecgamesTest.spring.service;

import com.anderfred.skytecgamesTest.spring.entity.Player;
import org.springframework.ui.Model;
import org.springframework.web.bind.support.SessionStatus;

import javax.servlet.http.HttpSession;

public interface GameService {
    String verify(Player player1, Model model);

    String waitForOpponent(Player player, Model model, HttpSession session);

    String hit(Player hero, Player enemy, Model model, HttpSession session, SessionStatus status);

    void makeMatch();

    void setDate(Model model);

    void logHits(Player hero, Player enemy);

    void win(Player hero, HttpSession session, SessionStatus status);

    void lose(Player hero, HttpSession session, SessionStatus status);
}
