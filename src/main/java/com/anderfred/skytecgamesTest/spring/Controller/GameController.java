package com.anderfred.skytecgamesTest.spring.Controller;

import com.anderfred.skytecgamesTest.spring.entity.Player;
import com.anderfred.skytecgamesTest.spring.service.GameService;
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
        model.addAttribute("player", new Player());
        return "login";
    }

    @PostMapping("/")
    public String indexPost(Model model) {
        model.addAttribute("player", new Player());
        return "login";
    }

    @PostMapping("/verify")
    public String verify(@ModelAttribute("player") Player player1, Model model) {
        return gameService.verify(player1, model);
    }

    @PostMapping("/exit")
    public String exit(Model model) {
        model.addAttribute("error", "Successful Exit");
        model.addAttribute("player", new Player());
        return "login";
    }

    @PostMapping("/duel")
    public String duel() {
        return "duel";
    }

    @PostMapping("/duelWait")
    public String waitIt(@ModelAttribute("player") Player player) {
        gameService.ready(player);
        return "duelWait";
    }

    @GetMapping("/duelWait")
    public String waitTi(@ModelAttribute("player") Player player, Model model, HttpSession session) {
        return gameService.waitForOpponent(player, model, session);
    }

    @GetMapping("/fight")
    public String fight(@ModelAttribute("player") Player player, Model model) {
        gameService.addEnemyAttribute(player, model);
        gameService.setDate(model);
        return "fight";
    }

    @PostMapping("/hit")
    public String hit(@ModelAttribute("player") Player player, Model model, HttpSession session, SessionStatus status) {
        gameService.setDate(model);
        return gameService.hit(player, model, session, status);
    }
}
