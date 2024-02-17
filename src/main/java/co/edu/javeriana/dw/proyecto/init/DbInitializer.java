package co.edu.javeriana.dw.proyecto.init;

import co.edu.javeriana.dw.proyecto.model.Planet;
import co.edu.javeriana.dw.proyecto.model.Star;
import co.edu.javeriana.dw.proyecto.persistence.IPlanetRepository;
import co.edu.javeriana.dw.proyecto.persistence.IStarRepository;
import lombok.*;
import net.datafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class DbInitializer implements CommandLineRunner {

    @Autowired
    private IStarRepository starRepository;
    @Autowired
    private IPlanetRepository planetRepository;

    @Override
    public void run(String... args) throws Exception {
        for(int i = 0; i < 40000; i++) {
            Faker faker = new Faker();
            Star star = new Star();
            Planet planet1 = new Planet();
            Planet planet2 = new Planet();
            Planet planet3 = new Planet();
            star.setCoordinates("TODO");//TODO falta generar las coordenadas aleatorias sin que se sobrelapen
            if(i <= 400) {
                star.setName(faker.space().star());
                starRepository.save(star);
                //creamos un número random entre 1 y 3 para la cantidad de planetas
                int random = new Random().nextInt(3) + 1;
                if(random == 1) {
                    planet1.setName(faker.space().planet());
                    planet1.setStar(star);
                    planetRepository.save(planet1);
                } else if(random == 2) {
                    planet1.setName(faker.starWars().planets());
                    planet1.setStar(star);
                    planetRepository.save(planet1);
                    planet2.setName(faker.starCraft().planet());
                    planet2.setStar(star);
                    planetRepository.save(planet2);
                } else {
                    planet1.setName(faker.dune().planet());
                    planet1.setStar(star);
                    planetRepository.save(planet1);
                    planet2.setName(faker.cultureSeries().planets());
                    planet2.setStar(star);
                    planetRepository.save(planet2);
                    planet3.setName(faker.massEffect().planet());
                    planet3.setStar(star);
                    planetRepository.save(planet3);
                }
            }
            star.setName(faker.space().galaxy());
            starRepository.save(star);
            //TODO falta realizar el grafo con las relaciones entre los wormholes y las estrellas
        }
    }
}
