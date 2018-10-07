package com.anderfred.skytecgamesTest.spring.Controller;

import com.anderfred.skytecgamesTest.spring.entity.Player;
import com.anderfred.skytecgamesTest.spring.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;


@Controller
@SessionAttributes({"player", "hero", "enemy", "heroHp"})
@RequestMapping("/")
public class GameController {

    @Autowired
    GameService gameService;

    public static Map<Integer, Player> game;

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("player", new Player());
        if (game == null) game = new HashMap<>();
        return "login";
    }

    @PostMapping("/")
    public String indexPost(Model model) {
        model.addAttribute("player", new Player());
        if (game == null) game = new HashMap<>();
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
    public String waitIt() {
        return "duelWait";
    }

    @GetMapping("/duelWait")
    public String waitTi(@ModelAttribute("player") Player player, Model model) {
        return gameService.waitForOpponent(player, model);
    }

    @GetMapping("/fight")
    public String fight() {
        return "fight";
    }

    @PostMapping("/hit")
    public String hit(@ModelAttribute("hero") Player hero, @ModelAttribute("enemy") Player enemy, Model model, HttpSession session, SessionStatus status) {
        return gameService.hit(hero, enemy, model, session, status);
    }


}
