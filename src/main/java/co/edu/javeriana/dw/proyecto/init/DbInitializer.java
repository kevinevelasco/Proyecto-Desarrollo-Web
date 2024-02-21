package co.edu.javeriana.dw.proyecto.init;

import co.edu.javeriana.dw.proyecto.model.*;
import co.edu.javeriana.dw.proyecto.persistence.*;
import lombok.*;
import net.datafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.NoSuchElementException;
import java.util.Random;

@Component
public class DbInitializer implements CommandLineRunner {

    @Autowired
    private IStarRepository starRepository;
    @Autowired
    private IPlanetRepository planetRepository;
    @Autowired
    private ISpacecraftModelRepository spacecraftModelRepository;
    @Autowired
    private ISpacecraftRepository spacecraftRepository;
    @Autowired
    private IPlayerRepository playerRepository;

    @Override
    public void run(String... args) throws Exception {
        for (int i = 0; i < 40000; i++) {
            Faker faker = new Faker();

            Star star = new Star();
            Planet planet1 = new Planet();
            Planet planet2 = new Planet();
            Planet planet3 = new Planet();
            SpacecraftModel spacecraftModel = new SpacecraftModel();
            Spacecraft spacecraft = new Spacecraft();

            star.setCoordinates("TODO");//TODO falta generar las coordenadas aleatorias sin que se sobrelapen

            if (i <= 20) {
                while (true) {
                    try {
                        spacecraftModel.setModelName(faker.unique().fetchFromYaml("space.nasa_space_craft"));
                    } catch (NoSuchElementException e) {
                        spacecraftModel.setModelName(faker.unique().fetchFromYaml("star_wars.vehicles"));
                        break;
                    } finally {
                        spacecraftModel.setStorage(faker.number().randomDouble(0, 40, 1000));
                        spacecraftModel.setMaxSpeed(faker.number().randomDouble(0, 100, 1000));
                        spacecraftModelRepository.save(spacecraftModel);
                    }
                }
            }
            if (i > 20 && i <= 30) {
                spacecraft.setName(faker.team().name());
                spacecraft.setCredit(BigDecimal.valueOf(faker.number().randomDouble(5, 1000, 1000000)));
                spacecraft.setTotalTime(faker.number().randomDouble(0, 100, 1000));
                spacecraft.setCoordinates("TODO");//TODO falta generar las coordenadas aleatorias
                spacecraft.setSpacecraftModel(spacecraftModelRepository.findById((long) i - 20).get());
                spacecraftRepository.save(spacecraft);
            }
            if (i <= 400) {
                star.setName(faker.space().star());
                starRepository.save(star);
                //creamos un número random entre 1 y 3 para la cantidad de planetas
                int random = new Random().nextInt(3) + 1;
                if (random == 1) {
                    planet1.setName(faker.space().planet());
                    planet1.setStar(star);
                    planetRepository.save(planet1);
                } else if (random == 2) {
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

        for (int j = 0; j < 100; j++) {
            Faker faker = new Faker();
            Player player = new Player();
            player.setUserName(faker.internet().username());
            player.setPassword("12345");
            val random = new Random().nextInt(3);

            if (random == 0)
                player.setType(PlayerType.CAPTAIN);
            else if (random == 1)
                player.setType(PlayerType.MERCHANT);
            else player.setType(PlayerType.PILOT);

            long spacecraftId = (j / 10) + 1;
            if (j >= 90) {
                spacecraftId = 10;
            }
            player.setSpacecraft(spacecraftRepository.findById(spacecraftId).get());
            playerRepository.save(player);

        }
    }
}
