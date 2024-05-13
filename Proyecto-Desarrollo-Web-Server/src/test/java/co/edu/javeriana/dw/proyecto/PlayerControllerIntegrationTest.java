package co.edu.javeriana.dw.proyecto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ActiveProfiles;

import co.edu.javeriana.dw.proyecto.controllers.newcontrollers.PlayerController;
import co.edu.javeriana.dw.proyecto.model.*;
import co.edu.javeriana.dw.proyecto.persistence.*;
import co.edu.javeriana.dw.proyecto.service.PlayerService;

import org.springframework.web.client.RestTemplate;

//Para correr todas al mismo tiempo se debe usar mvn test -Dtest=co.edu.javeriana.dw.proyecto.PlayerControllerIntegrationTest

@ActiveProfiles("integration-testing")
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
public class PlayerControllerIntegrationTest {

    private static final String SERVER_URL = "http://localhost:8081";

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
    private TestRestTemplate testRestTemplate;

    @LocalServerPort
private int port;

    @BeforeEach
    void init() {
        // Creando las estrellas del servidor de pruebas
        Star star1 = starRepository.save(new Star("Estrella1", 0.0, 0.0, 0.0));
        Star star2 = starRepository.save(new Star("Estrella2", 1.0, 1.0, 1.0));
        Star star3 = starRepository.save(new Star("Estrella3", 2.0, 2.0, 2.0));

        // Creando los planetas a la primera estrella del servidor de pruebas
        Planet planet1 = planetRepository.save(new Planet("Planeta1", star1));
        Planet planet2 = planetRepository.save(new Planet("Planeta2", star1));
        Planet planet3 = planetRepository.save(new Planet("Planeta3", star1));

        // Creando los planetas a la segunda estrella del servidor de pruebas
        Planet planet4 = planetRepository.save(new Planet("Planeta4", star2));
        Planet planet5 = planetRepository.save(new Planet("Planeta5", star2));
        Planet planet6 = planetRepository.save(new Planet("Planeta6", star2));

        // Creando los planetas a la tercera estrella del servidor de pruebas
        Planet planet7 = planetRepository.save(new Planet("Planeta7", star3));
        Planet planet8 = planetRepository.save(new Planet("Planeta8", star3));
        Planet planet9 = planetRepository.save(new Planet("Planeta9", star3));

        // Crear modelos de naves espaciales del servidor de pruebas
        SpacecraftModel model1 = spacecraftModelRepository.save(new SpacecraftModel("Modelo A", 100.0, 2.5));
        SpacecraftModel model2 = spacecraftModelRepository.save(new SpacecraftModel("Modelo B", 150.0, 3.0));
        SpacecraftModel model3 = spacecraftModelRepository.save(new SpacecraftModel("Modelo C", 200.0, 4.0));

        // Crear naves espaciales para cada planeta de la estrella 1 del servidor de pruebas
        spacecraftRepository.save(new Spacecraft("Nave 1", new BigDecimal("1000"), 150.0, planet1, model1));
        spacecraftRepository.save(new Spacecraft("Nave 2", new BigDecimal("2000"), 300.0, planet2, model1));
        spacecraftRepository.save(new Spacecraft("Nave 3", new BigDecimal("1100"), 160.0, planet3, model1));

        // Crear naves espaciales para cada planeta de la estrella 2 del servidor de pruebas
        spacecraftRepository.save(new Spacecraft("Nave 4", new BigDecimal("1100"), 160.0, planet4, model2));
        spacecraftRepository.save(new Spacecraft("Nave 5", new BigDecimal("2100"), 310.0, planet5, model2));
        spacecraftRepository.save(new Spacecraft("Nave 6", new BigDecimal("1200"), 170.0, planet6, model2));

        // Crear naves espaciales para cada planeta de la estrella 3 del servidor de pruebas
        spacecraftRepository.save(new Spacecraft("Nave 7", new BigDecimal("1200"), 170.0, planet7, model3));
        spacecraftRepository.save(new Spacecraft("Nave 8", new BigDecimal("2200"), 320.0, planet8, model3));
        spacecraftRepository.save(new Spacecraft("Nave 9", new BigDecimal("1300"), 180.0, planet9, model3));

        // Supongamos que recuperamos algunas naves espaciales para asociar a los jugadores
        Spacecraft spacecraft1 = spacecraftRepository.findById(1L).orElseThrow(() -> new RuntimeException("Spacecraft not found"));
        Spacecraft spacecraft2 = spacecraftRepository.findById(2L).orElseThrow(() -> new RuntimeException("Spacecraft not found"));
        Spacecraft spacecraft3 = spacecraftRepository.findById(3L).orElseThrow(() -> new RuntimeException("Spacecraft not found"));

        // Crear jugadores y asignarlos a las naves
        playerRepository.save(new Player("jugador1", "12345", PlayerType.PILOT, spacecraft1));
        playerRepository.save(new Player("jugador2", "12345", PlayerType.CAPTAIN, spacecraft1));
        playerRepository.save(new Player("jugador3", "12345", PlayerType.MERCHANT, spacecraft1));

        playerRepository.save(new Player("jugador4", "12345", PlayerType.PILOT, spacecraft2));
        playerRepository.save(new Player("jugador5", "12345", PlayerType.CAPTAIN, spacecraft2));
        playerRepository.save(new Player("jugador6", "12345", PlayerType.MERCHANT, spacecraft2));

        playerRepository.save(new Player("jugador7", "12345", PlayerType.PILOT, spacecraft3));
        playerRepository.save(new Player("jugador8", "12345", PlayerType.CAPTAIN, spacecraft3));
        playerRepository.save(new Player("jugador9", "12345", PlayerType.MERCHANT, spacecraft3));

        // Crear productos
        productRepository.save(new Product(0.5, "Producto1"));
        productRepository.save(new Product(1.2, "Producto2"));
        productRepository.save(new Product(0.3, "Producto3"));
        productRepository.save(new Product(0.7, "Producto4"));
        productRepository.save(new Product(0.9, "Producto5"));
        productRepository.save(new Product(0.1, "Producto6"));
        productRepository.save(new Product(0.4, "Producto7"));
        productRepository.save(new Product(0.6, "Producto8"));
        productRepository.save(new Product(0.8, "Producto9"));

        // Recuperar productos y planetas específicos para mercado
        Product product1 = productRepository.findById(1L).orElseThrow(() -> new RuntimeException("Product not found"));
        Product product2 = productRepository.findById(2L).orElseThrow(() -> new RuntimeException("Product not found"));
        Product product3 = productRepository.findById(3L).orElseThrow(() -> new RuntimeException("Product not found"));
        
        Planet marketPlanet1 = planetRepository.findById(1L).orElseThrow(() -> new RuntimeException("Planet not found"));
        Planet marketPlanet2 = planetRepository.findById(2L).orElseThrow(() -> new RuntimeException("Planet not found"));
        Planet marketPlanet3 = planetRepository.findById(3L).orElseThrow(() -> new RuntimeException("Planet not found"));

        // Crear mercados
        marketRepository.save(new Market(marketPlanet1, product1, 100, 1, 1, 5.0, 6.0));
        marketRepository.save(new Market(marketPlanet2, product2, 150, 2, 2, 10.0, 12.0));
        marketRepository.save(new Market(marketPlanet3, product3, 200, 3, 3, 15.0, 18.0));
        
        // Crear inventarios
        inventoryRepository.save(new Inventory(spacecraft1, product1, 100));
        inventoryRepository.save(new Inventory(spacecraft1, product2, 150));
        inventoryRepository.save(new Inventory(spacecraft2, product3, 200));
        inventoryRepository.save(new Inventory(spacecraft3, product1, 250));

    }


    //Prueba para get, para acceder usar comando mvn test -Dtest=PlayerControllerIntegrationTest#traerJugadorPorId
    //FUNCIONA  
    @Test
    void traerJugadorPorId() {
        TestRestTemplate testRestTemplate = new TestRestTemplate();
        Player player = testRestTemplate.getForObject(SERVER_URL + "/api/player/1", Player.class);
        assertEquals("jugador1", player.getUserName());
    }

    //prueba para delete, para acceder usar comando mvn test -Dtest=PlayerControllerIntegrationTest#borrarJugadorPorId
    //FUNCIONA
    void borrarJugadorPorId() {
        String serverUrl = "http://localhost:" + port;
        testRestTemplate.delete(serverUrl + "/api/player/1");
        ResponseEntity<Player> responseEntity = testRestTemplate.getForEntity(serverUrl + "/api/player/1", Player.class);
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    //Prueba para post, para acceder usar comando mvn test -Dtest=PlayerControllerIntegrationTest#crearJugador
    //FUNCIONA
    @Test
    public void testRegisterPlayer() {
        PlayerService playerService = mock(PlayerService.class);
        PlayerController playerController = new PlayerController(playerService);
        // el "jugador" q se va a crear
        Player player = new Player();
        player.setUserName("nombredeusuario");
        player.setPassword("contraseña");
        when(playerService.getPlayerByUsername("nombredeusuario")).thenReturn(null); 
        when(playerService.savePlayer(player)).thenReturn(player); 
        ResponseEntity<Player> responseEntity = playerController.registerPlayer(player);
        assert(responseEntity.getStatusCode() == HttpStatus.OK);
        assert(responseEntity.getBody() == player);
        verify(playerService, times(1)).getPlayerByUsername("nombredeusuario");
        verify(playerService, times(1)).savePlayer(player);
    }


    //Prueba para put, para acceder usar comando mvn test -Dtest=PlayerControllerIntegrationTest#actualizarJugador
    //FUNCIONA
    @Test
    public void actualizarJugador() {
        PlayerService playerService = mock(PlayerService.class);
        PlayerController playerController = new PlayerController(playerService);
        Long playerId = 1L;
        Player existingPlayer = new Player();
        existingPlayer.setId(playerId);
        existingPlayer.setUserName("nombredeusuario");
        existingPlayer.setPassword("contraseña");
        Player updatedPlayer = new Player();
        updatedPlayer.setId(playerId);
        updatedPlayer.setUserName("nuevoNombreDeUsuario");
        updatedPlayer.setPassword("nuevaContraseña");
        when(playerService.getPlayerById(playerId)).thenReturn(existingPlayer);
        when(playerService.savePlayer(updatedPlayer)).thenReturn(updatedPlayer); 
        ResponseEntity<Player> responseEntity = playerController.updatePlayer(playerId, updatedPlayer);
        assert(responseEntity.getStatusCode() == HttpStatus.OK);
        assert(responseEntity.getBody() == updatedPlayer);
        verify(playerService, times(1)).getPlayerById(playerId);
        verify(playerService, times(1)).savePlayer(updatedPlayer);
    }


    //Prueba para patch, para acceder usar comando mvn test -Dtest=PlayerControllerIntegrationTest#actualizarJugadorParcialmente
    //FUNCIONA
    @Test
    public void actualizarJugadorParcialmente() {
        PlayerService playerService = mock(PlayerService.class);
        PlayerController playerController = new PlayerController(playerService);
        Long playerId = 1L;
        Player existingPlayer = new Player();
        existingPlayer.setId(playerId);
        existingPlayer.setUserName("nombredeusuario");
        existingPlayer.setPassword("contraseña");
        Player updatedPlayer = new Player();
        updatedPlayer.setUserName("nuevoNombreDeUsuario");
        when(playerService.getPlayerById(playerId)).thenReturn(existingPlayer); 
        when(playerService.savePlayer(existingPlayer)).thenReturn(existingPlayer);
        ResponseEntity<Player> responseEntity = playerController.updatePlayerPartially(playerId, updatedPlayer);
        assert(responseEntity.getStatusCode() == HttpStatus.OK);
        assert(responseEntity.getBody() == existingPlayer);
        verify(playerService, times(1)).getPlayerById(playerId);
        verify(playerService, times(1)).savePlayer(existingPlayer);
    }
}
