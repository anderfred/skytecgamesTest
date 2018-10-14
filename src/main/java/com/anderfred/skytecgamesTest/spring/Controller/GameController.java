package com.anderfred.skytecgamesTest.spring.Controller;

import com.anderfred.skytecgamesTest.spring.entity.Player;
import com.anderfred.skytecgamesTest.spring.service.GameService;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

import javax.servlet.http.HttpSession;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;


@Controller
@SessionAttributes({"player", "heroHp"})
@RequestMapping("/")
public class GameController {

    @Autowired
    GameService gameService;
    public static Map<Integer, Player> playersOnline = new HashMap<>();
    public static Map<Integer, Integer> match = new HashMap<>();
    public static Map<Integer, Deque<String>> hitLog = new HashMap<>();


    @GetMapping("/")
    public String index(Model model) {
        Session session = gameService.clear();
        model.addAttribute("player", new Player());
        gameService.modelSetMetrics(model, session);
        return "login";
    }

    @PostMapping("/")
    public String indexPost(Model model) {
        Session session = gameService.clear();
        model.addAttribute("player", new Player());
        gameService.modelSetMetrics(model, session);
        return "login";
    }

    @PostMapping("/verify")
    public String verify(@ModelAttribute("player") Player player1, Model model) {
        Session session = gameService.clear();
        String work = gameService.verify(player1, model);
        gameService.modelSetMetrics(model, session);
        return work;
    }

    @PostMapping("/exit")
    public String exit(Model model) {
        Session session = gameService.clear();
        model.addAttribute("error", "Successful Exit");
        model.addAttribute("player", new Player());
        gameService.modelSetMetrics(model, session);
        return "login";
    }

    @PostMapping("/duel")
    public String duel(Model model) {
        Session session = gameService.clear();
        gameService.modelSetMetrics(model, session);
        return "duel";
    }

    @PostMapping("/duelWait")
    public String waitIt(@ModelAttribute("player") Player player, Model model) {
        gameService.modelSetMetrics(model, gameService.clear());
        gameService.ready(player);
        return "duelWait";
    }

    @GetMapping("/duelWait")
    public String waitTi(@ModelAttribute("player") Player player, Model model, HttpSession session) {
        Session sessionStat = gameService.clear();
        String work = gameService.waitForOpponent(player, model, session);
        gameService.modelSetMetrics(model, sessionStat);
        return work;
    }

    @GetMapping("/fight")
    public String fight(@ModelAttribute("player") Player player, Model model) {
        Session session = gameService.clear();
        gameService.addEnemyAttribute(player, model);
        gameService.modelSetMetrics(model, session);
        return "fight";
    }

    @PostMapping("/hit")
    public String hit(@ModelAttribute("player") Player player, Model model, HttpSession session, SessionStatus status) {
        Session sessionStat = gameService.clear();
        String work = gameService.hit(player, model, session, status);
        gameService.modelSetMetrics(model, sessionStat);
        return work;
    }
}
