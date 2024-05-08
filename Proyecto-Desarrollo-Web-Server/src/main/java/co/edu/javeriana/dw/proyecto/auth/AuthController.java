package co.edu.javeriana.dw.proyecto.auth;

import co.edu.javeriana.dw.proyecto.model.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
  @Autowired
  private AuthService authService;
    
    @PostMapping( "/login")
    // http://localhost:8080/auth/login
    //devolvemos el objeto si es que se encuentra
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest loginRequest) {
      System.out.println("loginRequest: " + loginRequest);
          return ResponseEntity.ok(authService.login(loginRequest));
    }

    @GetMapping("/logout")
    // http://localhost:8080/auth/logout
    public ResponseEntity<String> logout() {
        return ResponseEntity.ok("Logout");
    }

}
