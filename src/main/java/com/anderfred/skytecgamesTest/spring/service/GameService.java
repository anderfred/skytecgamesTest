package com.anderfred.skytecgamesTest.spring.service;

import com.anderfred.skytecgamesTest.spring.entity.Player;
import org.hibernate.Session;
import org.springframework.ui.Model;
import org.springframework.web.bind.support.SessionStatus;

import javax.servlet.http.HttpSession;

public interface GameService {
    String verify(Player player1, Model model);

    String waitForOpponent(Player player, Model model, HttpSession session);

    String hit(Player player, Model model, HttpSession session, SessionStatus status);

    void makeMatch();

    void logHits(Player hero, Player enemy);

    String win(Player hero, HttpSession session, SessionStatus status, Model model);

    String lose(Player hero, HttpSession session, SessionStatus status, Model model);

    void addEnemyAttribute(Player player, Model model);

    void ready(Player player);

    Session clear();

    void modelSetMetrics(Model model, Session sessionStat);
}
