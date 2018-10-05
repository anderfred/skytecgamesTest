package com.anderfred.skytecgamesTest.Controller;

import com.anderfred.skytecgamesTest.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class GameController {
    @Autowired
    PlayerService playerService;

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("player", playerService.getPlayerByName("fred"));
        return "index";
    }
}
