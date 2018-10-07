package com.anderfred.skytecgamesTest.spring.service;

import com.anderfred.skytecgamesTest.spring.Controller.GameController;
import com.anderfred.skytecgamesTest.spring.entity.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.support.SessionStatus;

import javax.servlet.http.HttpSession;
import java.util.Map;

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
    public String waitForOpponent(Player player, Model model) {
        if (!GameController.game.containsKey(player.getId())) {
            playerService.ready(player, true);
            GameController.game.put(player.getId(), playerService.getPlayerByName(player.getName()));
        }
        if (GameController.game.size() >= 2) {
            model.addAttribute("hero", playerService.getPlayerByName(player.getName()));
            model.addAttribute("heroHp", playerService.getPlayerByName(player.getName()).getHealth());
            for (Map.Entry<Integer, Player> m : GameController.game.entrySet()) {
                if (m.getKey() != player.getId() && playerService.getPlayerByName(m.getValue().getName()).isReady()) {
                    model.addAttribute("enemy", m.getValue());
                    break;
                }
            }
            return "prepareFight";
        }
        return "duelWait";
    }

    @Override
    public String hit(Player hero, Player enemy, Model model, HttpSession session, SessionStatus status) {
        if (playerService.getPlayerByName(hero.getName()).getHealth() > 0
                && playerService.getPlayerByName(enemy.getName()).getHealth() > 0
                && playerService.getPlayerByName(hero.getName()).isReady() &&
                playerService.getPlayerByName(enemy.getName()).isReady()) {
            playerService.hit(hero, enemy);
            model.addAttribute("enemy", playerService.getPlayerByName(enemy.getName()));
            model.addAttribute("hero", playerService.getPlayerByName(hero.getName()));
            return "fight";
        } else if (playerService.getPlayerByName(hero.getName()).getHealth() <= 0) {

            playerService.afterFight(hero, Integer.parseInt(session.getAttribute("heroHp").toString()), -1);
            playerService.ready(hero, false);
            GameController.game = null;
            status.setComplete();
            return "lose";
        } else {
            playerService.afterFight(hero, Integer.parseInt(session.getAttribute("heroHp").toString()), +1);
            playerService.ready(hero, false);
            GameController.game = null;
            status.setComplete();
            return "win";
        }
    }
}
