package com.anderfred.skytecgamesTest.spring.service;

import com.anderfred.skytecgamesTest.spring.Controller.GameController;
import com.anderfred.skytecgamesTest.spring.entity.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.support.SessionStatus;

import javax.servlet.http.HttpSession;
import java.util.*;

@Service
public class GameServiceImpl implements GameService {
    @Autowired
    PlayerService playerService;

    @Override
    public String verify(Player player1, Model model) {
        if (playerService.getPlayerByName(player1.getName()) == null) {
            Player player = new Player(player1.getName(), player1.getPassword(), 100, 0, 10, false);
            playerService.save(player);
            model.addAttribute("player", player);
            GameController.playersOnline.put(player.getId(), player);
            return "welcome";
        } else {
            if (playerService.getPlayerByName(player1.getName()).getPassword().equals(player1.getPassword())) {
                Player player = playerService.getPlayerByName(player1.getName());
                player.setReady(false);
                playerService.save(player);
                model.addAttribute("player", playerService.getPlayerByName(player.getName()));
                GameController.playersOnline.put(player.getId(), player);
                return "welcome";
            } else {
                model.addAttribute("error", "wrong password");
                model.addAttribute("player", new Player());
                return "login";
            }
        }
    }

    @Override
    public String waitForOpponent(Player player, Model model, HttpSession session) {
        makeMatch();

        if (GameController.match.size() >= 2 && GameController.match.containsKey(player.getId())) {
            addEnemyAttribute(player, model);
            model.addAttribute("heroHp", playerService.getPlayerByName(player.getName()).getHealth());
            return "prepareFight";
        }
        return "duelWait";
    }

    @Override
    public String hit(Player player, Model model, HttpSession session, SessionStatus status) {
        if (playerService.getPlayerByName(player.getName()).getHealth() > 0) {
            playerService.hit(player, playerService.findById(GameController.match.get(player.getId())).get());
            logHits(playerService.getPlayerByName(player.getName()), playerService.findById(GameController.match.get(player.getId())).get());
            model.addAttribute("hits", GameController.hitLog.get(player.getId()));
            addEnemyAttribute(player, model);
        }
        if (playerService.getPlayerByName(player.getName()).getHealth() <= 0) {
            return lose(player, session, status, model);
        } else if (playerService.getPlayerByName(playerService.findById(GameController.match.get(player.getId())).get().getName()).getHealth() <= 0) {
            return win(player, session, status, model);
        }
        return "fight";
    }

    @Override
    public void makeMatch() {
        Player first = null, second = null;
        if (GameController.playersOnline.size() >= 2) {
            for (Map.Entry<Integer, Player> m : GameController.playersOnline.entrySet()) {
                if (m.getValue().isReady()) {
                    if (first == null) {
                        first = m.getValue();
                    } else if (second == null) {
                        second = m.getValue();
                    }
                }
            }
        }
        if (first != null && second != null) {
            first.setReady(false);
            playerService.save(first);
            second.setReady(false);
            playerService.save(second);
            GameController.playersOnline.replace(first.getId(), first);
            GameController.playersOnline.replace(second.getId(), second);
            GameController.match.put(first.getId(), second.getId());
            GameController.match.put(second.getId(), first.getId());
            GameController.hitLog.put(second.getId(), new LinkedList<>());
            GameController.hitLog.put(first.getId(), new LinkedList<>());
        }
    }

    @Override
    public void setDate(Model model) {
        Date date = new Date();
        model.addAttribute("time", date.getTime());
    }

    @Override
    public void logHits(Player hero, Player enemy) {

        Deque<String> listHero = GameController.hitLog.get(hero.getId());
        Deque<String> listEnemy = GameController.hitLog.get(enemy.getId());
        listHero.addFirst("Вы ударили " + enemy.getName() + " на " + hero.getDamage() + " урона.");
        listEnemy.addFirst(hero.getName() + " ударил вас на " + hero.getDamage() + " урона");
        if (hero.getHealth() < 0) {
            listHero.addFirst("Вас убил " + enemy.getName());
            listEnemy.addFirst("Вы убили " + hero.getName());
        }

        GameController.hitLog.replace(hero.getId(), listHero);
        GameController.hitLog.replace(enemy.getId(), listEnemy);

    }

    @Override
    public String win(Player hero, HttpSession session, SessionStatus status, Model model) {
        hero.setReady(false);
        playerService.afterFight(hero, Integer.parseInt(session.getAttribute("heroHp").toString()), +1);
        GameController.match.remove(hero.getId());
        GameController.playersOnline.remove(hero.getId());
        GameController.hitLog.remove(hero.getId());
        status.setComplete();
        model.addAttribute("message", "Вы победили!");
        model.addAttribute("hero", playerService.getPlayerByName(hero.getName()));
        return "summary";
    }

    @Override
    public String lose(Player hero, HttpSession session, SessionStatus status, Model model) {
        hero.setReady(false);
        playerService.afterFight(hero, Integer.parseInt(session.getAttribute("heroHp").toString()), -1);
        GameController.match.remove(hero.getId());
        GameController.playersOnline.remove(hero.getId());
        GameController.hitLog.remove(hero.getId());
        status.setComplete();
        model.addAttribute("message", "Вы проиграли!");
        model.addAttribute("hero", playerService.getPlayerByName(hero.getName()));
        return "summary";
    }

    @Override
    public void addEnemyAttribute(Player player, Model model) {
        model.addAttribute("hero", playerService.getPlayerByName(player.getName()));
        model.addAttribute("enemy", playerService.findById(GameController.match.get(player.getId())).get());
    }

    @Override
    public void ready(Player player) {
        player.setReady(true);
        playerService.save(player);
    }
}
