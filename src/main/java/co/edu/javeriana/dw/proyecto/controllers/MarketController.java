package co.edu.javeriana.dw.proyecto.controllers;

import co.edu.javeriana.dw.proyecto.model.Market;
import co.edu.javeriana.dw.proyecto.service.MarketService;
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
@RequestMapping("/market")
public class MarketController {

    Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private MarketService marketService;

    @GetMapping("/list")
    public String listMarkets(Model model) {
        List<Market> markets = marketService.getAllMarket();
        log.info("Markets: " + markets.size());
        model.addAttribute("markets", markets);
        return "market-list";
    }
    @GetMapping("/view/{id}")
    public String viewMarket(Model model, Long  id) {
        Market market = marketService.getMarketById(id);
        model.addAttribute("market", marketService);
        return "market-view";
    }
    @GetMapping("/delete/{id}")
    public String deleteMarket(Model model, Long  id) {
        marketService.deleteMarket(id);
        return "redirect:/market/list";
    }
    @GetMapping("/edit/{id}")
    public String editMarket(Model model, Long  id) {
        Market market = marketService.getMarketById(id);
        model.addAttribute("market", market);
        return "player-edit";
    }

    @PostMapping(value = "/save")
    public String saveMarket(@Valid Market market, BindingResult result, Model model) {
        if(result.hasErrors()) {
            return "market-edit";
        }
        marketService.saveMarket(market);
        return "redirect:/market/list";
    }
}
