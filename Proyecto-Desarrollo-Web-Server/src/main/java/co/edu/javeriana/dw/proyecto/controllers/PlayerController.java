package co.edu.javeriana.dw.proyecto.controllers;

import co.edu.javeriana.dw.proyecto.model.Player;
import co.edu.javeriana.dw.proyecto.model.Spacecraft;
import co.edu.javeriana.dw.proyecto.service.PlayerService;
import co.edu.javeriana.dw.proyecto.service.SpacecraftService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import jakarta.validation.Valid;


import java.util.List;

@Controller
@RequestMapping("/player")
public class PlayerController {

    Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private PlayerService playerService;

    @Autowired
    private SpacecraftService spacecraftService;

    @GetMapping("/list")
    public String listPlayers(Model model) {
        List<Player> players = playerService.getAllPlayers();
        log.info("Players: " + players.size());
        model.addAttribute("players", players);
        return "player-list";
    }
    @GetMapping("/view/{id}")
    public String viewPlayer(Model model,  @PathVariable Long  id) {
        Player player = playerService.getPlayerById(id);
        model.addAttribute("player", player);
        return "player-view";
    }
    @PostMapping(value = "/save")
    public String savePlayer(@Valid Player player, BindingResult result, Model model) {
        if(result.hasErrors()) {
            List<Spacecraft> spacecrafts = spacecraftService.getAllSpacecrafts();
            model.addAttribute("player", player);
            model.addAttribute("spacecrafts", spacecrafts);
            return "player-edit";
        }
        log.info("Player: " + player.toString());
        playerService.savePlayer(player);
        return "redirect:/player/list";
    }
    @GetMapping("/delete/{id}")
    public String deletePlayer(@PathVariable Long  id) {
        playerService.deletePlayer(id);
        return "redirect:/player/list";
    }
    @GetMapping("/edit/{id}")
    public String editPlayer(Model model, @PathVariable Long id) {
        Player player = playerService.getPlayerById(id);
        List<Spacecraft> spacecrafts = spacecraftService.getAllSpacecrafts();
        model.addAttribute("player", player);
        model.addAttribute("spacecrafts", spacecrafts);
        return "player-edit";
    }
    @GetMapping("/create")
    public String createPlayer(Model model) {
        List<Spacecraft> spacecrafts = spacecraftService.getAllSpacecrafts();
        model.addAttribute("spacecrafts", spacecrafts);
        model.addAttribute("player", new Player());
        return "player-create";
    }

}
