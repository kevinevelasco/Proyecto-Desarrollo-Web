package co.edu.javeriana.dw.proyecto.init;

import co.edu.javeriana.dw.proyecto.model.*;
import co.edu.javeriana.dw.proyecto.persistence.*;
import lombok.val;
import net.datafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.function.Supplier;

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

    @Override
    public void run(String... args) throws Exception {
        Faker faker = new Faker();
        //bucle para generar las 40000 estrellas, dentro de las primeras 20 iteraciones se generan los modelos de naves, en las siguientes 10 se generan las naves y en las siguientes 400 se generan las estrellas y planetas
        for (int i = 0; i < 40000; i++) {

            Star star = new Star();
            Planet planet1 = new Planet();
            Planet planet2 = new Planet();
            Planet planet3 = new Planet();
            SpacecraftModel spacecraftModel = new SpacecraftModel();
            Spacecraft spacecraft = new Spacecraft();


            double randomX = faker.number().randomDouble(0, -100, 100);
            double randomY = faker.number().randomDouble(0, -100, 100);
            double randomZ = faker.number().randomDouble(0, -100, 100);

            star.setX((double)(i+1)*randomX);
            star.setY((double)(i+1)*randomY);
            star.setZ((double)(i+1)*randomZ);



            if (i <= 20) {
                while (true) {
                    try {
                        spacecraftModel.setModelName(faker.unique().fetchFromYaml("space.nasa_space_craft"));
                    } catch (NoSuchElementException e) {
                        spacecraftModel.setModelName(faker.unique().fetchFromYaml("star_wars.vehicles"));
                        break;
                    } finally {
                        spacecraftModel.setStorage(faker.number().randomDouble(0, 15, 580));
                        spacecraftModel.setMaxSpeed(faker.number().randomDouble(0, 100, 1000));
                        spacecraftModelRepository.save(spacecraftModel);
                    }
                }
            }
            if (i > 20 && i <= 30) {
                spacecraft.setName(faker.team().name());
                spacecraft.setCredit(BigDecimal.valueOf(faker.number().randomDouble(5, 1000, 1000000)));
                spacecraft.setTotalTime(faker.number().randomDouble(0, 100, 1000));
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
        int totalItemsPrinted = 0;
        int totalItemsToPrint = 500;
        List<Supplier<String>> fakerCategories = new ArrayList<>();
        fakerCategories.add(() -> faker.unique().fetchFromYaml("minecraft.item_name"));
        fakerCategories.add(() -> faker.unique().fetchFromYaml("food.spices"));
        fakerCategories.add(() -> faker.unique().fetchFromYaml("food.dish"));
        fakerCategories.add(() -> faker.unique().fetchFromYaml("food.fruits"));
        fakerCategories.add(() -> faker.unique().fetchFromYaml("food.vegetables"));
        fakerCategories.add(() -> faker.unique().fetchFromYaml("food.sushi"));
        fakerCategories.add(() -> faker.unique().fetchFromYaml("creature.animal.name"));
        fakerCategories.add(() -> faker.unique().fetchFromYaml("creature.animal.genus"));

        // Itera a través de cada categoría de Faker
        for (Supplier<String> category : fakerCategories) {
            while (totalItemsPrinted < totalItemsToPrint) {
                try {
                    Product product = new Product();
                    product.setName(category.get());
                    //generamos un numero aleatorio que identifica los metros cubicos que ocupa el producto
                    product.setSize(faker.number().randomDouble(2, 1, 10));

                    productRepository.save(product);
                    totalItemsPrinted++;
                } catch (Exception e) {
                    // Rompe el bucle interno si se agotan los elementos únicos de la categoría actual
                    break;
                }
            }
            // Verifica si ya se alcanzó el límite total de impresiones
            if (totalItemsPrinted >= totalItemsToPrint) {
                break;
            }
        }

        //bucle para el Market, en el que por cada planeta, cada producto tiene un precio diferente, dependiendo del stock y la demanda y oferta
        for (int i = 0; i < 400; i++) {
            int numberOfProducts = faker.number().numberBetween(15,50);
            Planet planet = planetRepository.findById((long) i + 1).get();
            List<Product> products = productRepository.findAll();
            List<Product> productsInPlanet = new ArrayList<>();
            for (int j = 0; j < numberOfProducts; j++) {
                productsInPlanet.add(products.get(faker.number().numberBetween(0, products.size())));
            }
            for (Product product : productsInPlanet) {
                Market market = new Market();
                market.setPlanet(planet);
                market.setProduct(product);
                market.setDemandFactor(faker.number().numberBetween(0, 1000000));
                market.setStock(faker.number().numberBetween(0, 1000000));
                market.setSupplyFactor(faker.number().numberBetween(0, 1000000));
                market.setSellPrice((double) (market.getDemandFactor()/(1+market.getSupplyFactor())));
                market.setBuyPrice((double) (market.getSupplyFactor()/(1+market.getDemandFactor())));
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
