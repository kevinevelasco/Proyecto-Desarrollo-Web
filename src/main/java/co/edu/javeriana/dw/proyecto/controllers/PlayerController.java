package co.edu.javeriana.dw.proyecto.controllers;

import co.edu.javeriana.dw.proyecto.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/player")
public class PlayerController {
    @Autowired
    private PlayerService playerService;

  //  @Autowired
    //private SpacecraftService spacecraftService;
}
