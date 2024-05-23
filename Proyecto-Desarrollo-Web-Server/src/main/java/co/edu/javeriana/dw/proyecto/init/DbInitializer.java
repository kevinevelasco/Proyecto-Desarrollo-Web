package co.edu.javeriana.dw.proyecto.init;

import co.edu.javeriana.dw.proyecto.model.*;
import co.edu.javeriana.dw.proyecto.persistence.*;
import lombok.val;
import net.datafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Supplier;
import org.springframework.context.annotation.Profile;


// mvn  spring-boot:run -Dspring-boot.run.profiles=default
@Profile({"default"})
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
    @Autowired
    private IProductRepository productRepository;
    @Autowired
    private IMarketRepository marketRepository;
    @Autowired
    private IInventoryRepository inventoryRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        Faker faker = new Faker();
        //bucle para generar las 40000 estrellas, dentro de las primeras 20 iteraciones se generan los modelos de naves, en las siguientes 10 se generan las naves y en las siguientes 400 se generan las estrellas y planetas
        for (int i = 0; i < 40000; i++) {

            Star star = new Star();
            Planet planet1 = new Planet();
            Planet planet2 = new Planet();
            Planet planet3 = new Planet();



            double randomX = faker.number().randomDouble(0, -100, 100);
            double randomY = faker.number().randomDouble(0, -100, 100);
            double randomZ = faker.number().randomDouble(0, -100, 100);

            star.setX((double)(i+1)*randomX);
            star.setY((double)(i+1)*randomY);
            star.setZ((double)(i+1)*randomZ);

            if (i <= 400) {
                star.setName(faker.space().star());
                starRepository.save(star);
                //creamos un número random entre 1 y 3 para la cantidad de planetas
                int random = new Random().nextInt(3) + 1;
                if (random == 1) {
                    planet1.setName(faker.space().planet());
                    planet1.setStar(star);

                    planet1.setRing(faker.bool().bool());
                    planet1.setSize(faker.number().numberBetween(1, 8));
                    planet1.setTexture(faker.number().numberBetween(1, 18));
                    planet1.setPosition(faker.number().numberBetween(20, 40));
                    planet1.setCharacter(faker.number().numberBetween(1, 9));
                    planet1.setAnimation(faker.number().numberBetween(1, 11));
                    planetRepository.save(planet1);
                } else if (random == 2) {
                    planet1.setName(faker.starWars().planets());
                    planet1.setStar(star);

                    planet1.setRing(faker.bool().bool());
                    planet1.setSize(faker.number().numberBetween(1, 4));
                    planet1.setTexture(faker.number().numberBetween(1, 9));
                    planet1.setPosition(faker.number().numberBetween(20, 30));
                    planet1.setCharacter(faker.number().numberBetween(1, 4));
                    planet1.setAnimation(faker.number().numberBetween(1, 5));

                    planet2.setName(faker.starCraft().planet());
                    planet2.setStar(star);

                    planet2.setRing(faker.bool().bool());
                    planet2.setSize(faker.number().numberBetween(4, 6));
                    planet2.setTexture(faker.number().numberBetween(10, 18));
                    planet2.setPosition(faker.number().numberBetween(50, 60));
                    planet2.setCharacter(faker.number().numberBetween(5, 9));
                    planet2.setAnimation(faker.number().numberBetween(6, 11));

                    planetRepository.save(planet1);
                    planetRepository.save(planet2);
                } else {
                    planet1.setName(faker.dune().planet());
                    planet1.setStar(star);

                    planet1.setRing(faker.bool().bool());
                    planet1.setSize(faker.number().numberBetween(1, 3));
                    planet1.setTexture(faker.number().numberBetween(1, 7));
                    planet1.setPosition(faker.number().numberBetween(20, 25));
                    planet1.setCharacter(faker.number().numberBetween(1, 3));
                    planet1.setAnimation(faker.number().numberBetween(1, 4));

                    planet2.setName(faker.cultureSeries().planets());
                    planet2.setStar(star);

                    planet2.setRing(faker.bool().bool());
                    planet2.setSize(faker.number().numberBetween(4, 6));
                    planet2.setTexture(faker.number().numberBetween(8, 13));
                    planet2.setPosition(faker.number().numberBetween(40, 60));
                    planet2.setCharacter(faker.number().numberBetween(4, 6));
                    planet2.setAnimation(faker.number().numberBetween(5, 8));

                    planet3.setName(faker.massEffect().planet());
                    planet3.setStar(star);

                    planet3.setRing(faker.bool().bool());
                    planet3.setSize(faker.number().numberBetween(6, 8));
                    planet3.setTexture(faker.number().numberBetween(14, 18));
                    planet3.setPosition(faker.number().numberBetween(80, 100));
                    planet3.setCharacter(faker.number().numberBetween(7, 9));
                    planet3.setAnimation(faker.number().numberBetween(9, 11));

                    planetRepository.save(planet1);
                    planetRepository.save(planet2);
                    planetRepository.save(planet3);
                }
            }
            star.setName(faker.space().galaxy());
            starRepository.save(star);

        }

        Path d = Paths.get("../Proyecto-Desarrollo-Web-Client/src/assets/ships");
        if(Files.isDirectory(d)){
            System.out.println("El directorio existe.");
            File[] files = d.toFile().listFiles();
            for (File file : files) {
                SpacecraftModel spacecraftModel = new SpacecraftModel();
                String name = file.getName();
                String step1 = name.replace("_", " ");
                String step2 = step1.replace(".png", "");
                spacecraftModel.setModelName(step2.trim());
                spacecraftModel.setStorage(faker.number().randomDouble(0, 15, 580));
                spacecraftModel.setMaxSpeed(faker.number().randomDouble(0, 10, 100));
                spacecraftModelRepository.save(spacecraftModel);
            }
        } else {
            System.out.println("El directorio no existe.");

        }

        for (int i = 0; i < 10; i++) {
            Spacecraft spacecraft = new Spacecraft();
            spacecraft.setName(faker.team().name());
            spacecraft.setCredit(BigDecimal.valueOf(faker.number().randomDouble(2, 20, 999)));
            spacecraft.setTotalTime(faker.number().randomDouble(0, 300, 1000));
            spacecraft.setSpacecraftModel(spacecraftModelRepository.findById((long) i + 1).get());
            spacecraftRepository.save(spacecraft);
        }
        //todas las spacecrafts las inicializamos en la estrella 1 planeta 1
        for (int i = 0; i < 10; i++) {
            Spacecraft spacecraft = spacecraftRepository.findById((long) i + 1).get();
            Planet planet = planetRepository.findById(1L).get();
            spacecraft.setPlanet(planet);
            spacecraftRepository.save(spacecraft);
        }

        // Batch para WORMHOLE, el que representa las conexiones entre planeta
        for (int i = 1; i <= 400; i++) {
            Star star1 = starRepository.findById((long) i ).get();
            Star star2 = starRepository.findById((long) faker.number().numberBetween(1,401)).get();
            star1.getWormholes().add(star2);
            star2.getWormholes().add(star1);
            starRepository.save(star1);
            starRepository.save(star2);
        }



        for (int j = 0; j < 100; j++) {
            Player player = new Player();
            player.setUserName(faker.internet().username());
            player.setPassword(passwordEncoder.encode("12345"));
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
        Path directory = Paths.get("../Proyecto-Desarrollo-Web-Client/src/assets/items");
        System.out.println(directory.toAbsolutePath());

        if (Files.isDirectory(directory)) {
            System.out.println("El directorio existe.");
            File[] files = directory.toFile().listFiles();
            for (File file : files) {
                Product product = new Product();
                String name = file.getName();
                String step1 = name.replace("_", " ");
                String step2 = step1.replace(".png", "");
                String[] words = step2.split(" ");
                StringBuilder result = new StringBuilder();
                for (String word : words) {
                    if (!word.isEmpty()) {
                        result.append(word.substring(0, 1).toUpperCase()).append(word.substring(1)).append(" ");
                    }
                }
                product.setName(result.toString().trim());
                product.setSize(faker.number().randomDouble(2, 1, 10));
                productRepository.save(product);
            }
        } else {
            System.out.println("El directorio no existe.");
        }

        Set<Product> uniqueProducts = new HashSet<>();

        for (int i = 0; i < 400; i++) {
            int numberOfProducts = faker.number().numberBetween(15, 50);
            Planet planet = planetRepository.findById((long) i + 1).get();
            List<Product> products = productRepository.findAll();

            // Limpiar el conjunto de productos para cada planeta
            uniqueProducts.clear();

            // Agregar productos únicos al conjunto
            while (uniqueProducts.size() < numberOfProducts) {
                Product product = products.get(faker.number().numberBetween(0, products.size()));
                uniqueProducts.add(product);
            }

            // Generar el mercado para cada producto único
            for (Product product : uniqueProducts) {
                double demandFactor = faker.number().numberBetween(1, 1000000);
                double supplyFactor = faker.number().numberBetween(1, 1000000);
                int stock = faker.number().numberBetween(0, 1000000);

                double sellPrice = demandFactor / (1 + stock);
                double buyPrice = supplyFactor / (1 + stock);

                Market market = new Market();
                market.setPlanet(planet);
                market.setProduct(product);
                market.setDemandFactor((int) demandFactor);
                market.setStock(stock);
                market.setSupplyFactor((int) supplyFactor);
                market.setSellPrice((Math.round(sellPrice * 100.0) / 100.0));
                market.setBuyPrice((Math.round(buyPrice * 100.0) / 100.0));

                marketRepository.save(market);
            }
        }
        
        double spaceAcum = 0;
        //bucle para el inventario de cada nave, hay que tener en cuenta la cantidad maxima de producto que puede haber en la nave, ya que depende de la capacidad de la nave
        for (int i = 0; i < 10; i++) {
            int numberOfProducts = faker.number().numberBetween(5,35);
            Spacecraft spacecraft = spacecraftRepository.findById((long) i + 1).get();
            List<Product> products = productRepository.findAll();
            List<Product> productsInSpacecraft = new ArrayList<>();

            for (int j = 0; j < numberOfProducts; j++) {
                productsInSpacecraft.add(products.get(faker.number().numberBetween(0, products.size())));
            }

            // ahora se agregan en la nave, teniendo en cuenta la restricción de la capacidad de la nave
            for (Product product : productsInSpacecraft) {
                int randomQuantity = faker.number().numberBetween(0, 20);
                Inventory inventory = new Inventory();
                inventory.setSpacecraft(spacecraft);
                inventory.setProduct(product);

                if (spacecraft.getSpacecraftModel().getStorage() >= (spaceAcum + randomQuantity * product.getSize())) {
                    inventory.setQuantity(randomQuantity);
                    spaceAcum += randomQuantity * product.getSize();
                } else {
                    int maxQuantity = (int) ((spacecraft.getSpacecraftModel().getStorage() - spaceAcum) / product.getSize());
                    inventory.setQuantity(maxQuantity);
                    spaceAcum += maxQuantity * product.getSize();
                }
                if(inventory.getQuantity() > 0)
                    inventoryRepository.save(inventory);
            }
        }
    }
}
