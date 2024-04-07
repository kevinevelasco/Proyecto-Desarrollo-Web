package co.edu.javeriana.dw.proyecto.auth;

import co.edu.javeriana.dw.proyecto.model.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
  @Autowired
  private AuthService authService;
    
    @PostMapping( "/login")
    // http://localhost:8080/api/auth/login
    //devolvemos el objeto si es que se encuentra
    public ResponseEntity<Player> login(@RequestBody LoginRequest loginRequest) {
      System.out.println("loginRequest: " + loginRequest.toString());
        if (authService.login(loginRequest) != null) {
            return ResponseEntity.ok(authService.login(loginRequest));
        }
        return ResponseEntity.notFound().build();
    }
}
