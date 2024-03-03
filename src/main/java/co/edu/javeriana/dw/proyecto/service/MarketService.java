package co.edu.javeriana.dw.proyecto.service;

import co.edu.javeriana.dw.proyecto.model.Market;

import co.edu.javeriana.dw.proyecto.model.Planet;
import co.edu.javeriana.dw.proyecto.persistence.IMarketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MarketService {

    @Autowired
    private IMarketRepository marketRepository;

    public List<Market> getAllMarket() {return marketRepository.findAll();}

    public Market getMarketById(Long id) {return marketRepository.findById(id).orElse(null);}

    public Market saveMarket(Market market) {return marketRepository.save(market);}

    public void deleteMarket(Long id) {marketRepository.deleteById(id);}

    public List<Market> buscarPorNombre(String textoBusqueda) {
        return marketRepository.findPlanetsByNameStartingWithCaseInsensitive(textoBusqueda);
    }

}
