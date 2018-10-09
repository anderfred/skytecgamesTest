package com.anderfred.skytecgamesTest.spring.service;

import com.anderfred.skytecgamesTest.spring.Controller.GameController;
import com.anderfred.skytecgamesTest.spring.entity.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
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
            return "welcome";
        } else {
            if (playerService.getPlayerByName(player1.getName()).getPassword().equals(player1.getPassword())) {
                model.addAttribute("player", playerService.getPlayerByName(player1.getName()));
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
        if (GameController.match.size() >= 2 && GameController.match.containsKey(player.getId())) {
            model.addAttribute("hero", playerService.getPlayerByName(player.getName()));
            model.addAttribute("heroHp", playerService.getPlayerByName(player.getName()).getHealth());
            model.addAttribute("enemy", playerService.findById(GameController.match.get(player.getId())).get());
            return "prepareFight";
        }
        if (!GameController.game.containsKey(player.getId())) {
            GameController.game.put(player.getId(), playerService.getPlayerByName(player.getName()));
            makeMatch();
        }
        return "duelWait";
    }

    @Override
    public String hit(Player hero, Player enemy, Model model, HttpSession session, SessionStatus status) {
        if (playerService.getPlayerByName(hero.getName()).getHealth() > 0) {
            playerService.hit(hero, enemy);
            logHits(playerService.getPlayerByName(hero.getName()), playerService.getPlayerByName(enemy.getName()));
            model.addAttribute("hits", GameController.hitLog.get(hero.getId()));
            model.addAttribute("enemy", playerService.getPlayerByName(enemy.getName()));
            model.addAttribute("hero", playerService.getPlayerByName(hero.getName()));
        }
        if (playerService.getPlayerByName(hero.getName()).getHealth() <= 0) {
            lose(hero, session, status);
            return "lose";
        } else if (playerService.getPlayerByName(enemy.getName()).getHealth() <= 0) {
            win(hero, session, status);
            return "win";
        }
        return "fight";
    }

    @Override
    public void makeMatch() {
        if (GameController.game.size() >= 2) {
            Player first = new Player(), second = new Player();
            int i = 0;
            for (Map.Entry<Integer, Player> m : GameController.game.entrySet()) {
                if (i == 0) {
                    first = m.getValue();
                }
                if (i == 1) {
                    second = m.getValue();
                    break;
                }
                i++;
            }
            GameController.game.remove(first.getId());
            GameController.game.remove(second.getId());
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
    public void win(Player hero, HttpSession session, SessionStatus status) {
        playerService.afterFight(hero, Integer.parseInt(session.getAttribute("heroHp").toString()), +1);
        GameController.match.remove(hero.getId());
        GameController.hitLog.remove(hero.getId());
        status.setComplete();
    }

    @Override
    public void lose(Player hero, HttpSession session, SessionStatus status) {
        playerService.afterFight(hero, Integer.parseInt(session.getAttribute("heroHp").toString()), -1);
        GameController.match.remove(hero.getId());
        GameController.hitLog.remove(hero.getId());
        status.setComplete();
    }
}
