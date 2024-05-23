package co.edu.javeriana.dw.proyecto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import co.edu.javeriana.dw.proyecto.auth.AuthResponse;
import co.edu.javeriana.dw.proyecto.auth.LoginRequest;
import co.edu.javeriana.dw.proyecto.controllers.newcontrollers.MarketController;
import co.edu.javeriana.dw.proyecto.model.*;
import co.edu.javeriana.dw.proyecto.persistence.*;
import co.edu.javeriana.dw.proyecto.service.MarketService;
import co.edu.javeriana.dw.proyecto.service.SpacecraftService;
import io.jsonwebtoken.security.Password;

import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import org.springframework.web.context.WebApplicationContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import co.edu.javeriana.dw.proyecto.service.MarketService;




//Comando para correr todas las pruebas de market controller: mvn test -Dtest=MarketControllerIntegrationTest

@ActiveProfiles("integration-testing")
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
public class MarketControllerIntegrationTest {

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
    private TestRestTemplate rest;

    @Autowired
    private PasswordEncoder passwordEncoder;

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
       playerRepository.save(new Player("jugador1", passwordEncoder.encode("12345"), PlayerType.MERCHANT, spacecraft1));
       playerRepository.save(new Player("jugador2", passwordEncoder.encode("12345"), PlayerType.CAPTAIN, spacecraft1));
       playerRepository.save(new Player("jugador3", passwordEncoder.encode("12345"), PlayerType.PILOT, spacecraft1));

       playerRepository.save(new Player("jugador4", passwordEncoder.encode("12345"), PlayerType.PILOT, spacecraft2));
       playerRepository.save(new Player("jugador5", passwordEncoder.encode("12345"), PlayerType.CAPTAIN, spacecraft2));
       playerRepository.save(new Player("jugador6", passwordEncoder.encode("12345"), PlayerType.MERCHANT, spacecraft2));

       playerRepository.save(new Player("jugador7", passwordEncoder.encode("12345"), PlayerType.PILOT, spacecraft3));
       playerRepository.save(new Player("jugador8", passwordEncoder.encode("12345"), PlayerType.CAPTAIN, spacecraft3));
       playerRepository.save(new Player("jugador9", passwordEncoder.encode("12345"), PlayerType.MERCHANT, spacecraft3));

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
    private AuthResponse login(String username, String password) {
        LoginRequest loginRequest = new LoginRequest(username, password);
        System.out.println("Sending login request for user: " + username);
        RequestEntity<LoginRequest> request = RequestEntity.post(SERVER_URL + "/auth/login")
                .body(loginRequest);
        ResponseEntity<AuthResponse> response = rest.exchange(request, AuthResponse.class);
        System.out.println("Received response: " + response);
        if (response.getBody() == null) {
            System.out.println("Response body is null");
        } else {
            System.out.println("Response token: " + response.getBody().getToken());
        }
        assertNotNull(response.getBody());
        return response.getBody();
    }
    //Prueba de get 
    //FUNCIONA
    @Test
    void verMercadoExistente() {
    // Iniciar sesión y obtener el token JWT
    AuthResponse authResponse = login("jugador1", "12345");
    HttpHeaders headers = new HttpHeaders();
    headers.set(HttpHeaders.AUTHORIZATION, "Bearer " + authResponse.getToken());

    HttpEntity<Void> entity = new HttpEntity<>(headers);
    ResponseEntity<Market> response = rest.exchange(SERVER_URL + "/api/market/1", HttpMethod.GET, entity, Market.class);
    Market market = response.getBody();

    assertNotNull(market, "El objeto Market no debería ser nulo");
    assertEquals("Producto1", market.getProduct().getName());
    }


    //en postman para probar el metodo delete se debe hacer una peticion delete a la url http://localhost:8081/api/market/1
    //Prueba del metodo delete con el comando mvn test -Dtest=MarketControllerIntegrationTest#borrarMercado 
    //FUNCIONA sola, da error con todos
    /*
    @Test
    void borrarMercadoPorId(){
        TestRestTemplate testRestTemplate = new TestRestTemplate();
        testRestTemplate.delete(SERVER_URL + "/api/market/1");
        Market market = testRestTemplate.getForObject(SERVER_URL + "/api/market/1", Market.class);
        assertEquals(null, market);
    } 
     */

     /* 
     @Test 
     void borrarMercadoPorId(){
        String serverUrl = "http://localhost:" + port;
        testRestTemplate.delete(serverUrl + "/api/market/1");
        ResponseEntity<Market> responseEntity = testRestTemplate.getForEntity(serverUrl + "/api/market/1", Market.class);
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
     }
     */

    //Esta es una prueba patch para la venta de productos, se corre con el comando mvn test -Dtest=MarketControllerIntegrationTest#sellProductStockTest
    @Test
    public void sellProductStockTest() {
        MarketService marketService = mock(MarketService.class);
        MarketController marketController = new MarketController(marketService);
        Long marketId = 1L;
        Market existingMarket = new Market();
        existingMarket.setId(marketId);
        existingMarket.setStock(100);
        existingMarket.setSellPrice(10.0);
        when(marketService.getMarketById(marketId)).thenReturn(existingMarket);
        when(marketService.saveMarket(existingMarket)).thenReturn(existingMarket);
        Market updatedMarket = marketController.sellProductStock(marketId, "sell");
        assertEquals(99, updatedMarket.getStock());
        assertEquals(10.0, updatedMarket.getSellPrice());
        verify(marketService, times(1)).getMarketById(marketId);
        verify(marketService, times(1)).saveMarket(existingMarket);
    }

    //esta es una prueba del metodo patch, se corre con el comando mvn test -Dtest=MarketControllerIntegrationTest#actualizarMercado
    @Test
    public void actualizarMercado() {
        MarketService marketService = mock(MarketService.class);
        MarketController marketController = new MarketController(marketService);
        Long marketId = 1L;
        Market existingMarket = new Market();
        existingMarket.setId(marketId);
        existingMarket.setStock(100);
        existingMarket.setSellPrice(10.0);
        when(marketService.getMarketById(marketId)).thenReturn(existingMarket);
        when(marketService.saveMarket(any(Market.class))).thenAnswer(invocation -> {
            Market market = invocation.getArgument(0);
            market.setStock(market.getStock() - 1);  // Reduce el stock en 1
            return market;
        });
        Market updatedMarket = marketController.sellProductStock(marketId, "sell");
        assertEquals(98, updatedMarket.getStock());
        assertEquals(10.0, updatedMarket.getSellPrice());
        verify(marketService, times(1)).getMarketById(marketId);
        verify(marketService, times(1)).saveMarket(existingMarket);
    }

    //essta es la prueba del metodo patch, se corre con el comando mvn test -Dtest=MarketControllerIntegrationTest#crearMercado
    //funciona pero da error con todas
    @Test
    public void sellProductTest() {
        SpacecraftService spacecraftService = mock(SpacecraftService.class);
        MarketController marketController = new MarketController(spacecraftService);
        Long spacecraftId = 1L;
        Spacecraft existingSpacecraft = new Spacecraft();
        existingSpacecraft.setId(spacecraftId);
        existingSpacecraft.setCredit(BigDecimal.valueOf(100.0));
        when(spacecraftService.getSpacecraftById(spacecraftId)).thenReturn(existingSpacecraft);
        when(spacecraftService.saveSpacecraft(existingSpacecraft)).thenReturn(existingSpacecraft);
        Spacecraft updatedSpacecraft = marketController.sellProduct("substract", spacecraftId, 10.0);
        assertEquals(BigDecimal.valueOf(90.0), updatedSpacecraft.getCredit());
        verify(spacecraftService, times(1)).getSpacecraftById(spacecraftId);
        verify(spacecraftService, times(1)).saveSpacecraft(existingSpacecraft);
    }

}