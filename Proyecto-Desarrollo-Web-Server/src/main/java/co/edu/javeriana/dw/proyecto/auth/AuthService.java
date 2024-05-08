package co.edu.javeriana.dw.proyecto.auth;

import co.edu.javeriana.dw.proyecto.model.Player;
import co.edu.javeriana.dw.proyecto.persistence.IPlayerRepository;
import co.edu.javeriana.dw.proyecto.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.UserDetails;


@Service
public class AuthService {
    @Autowired
    private IPlayerRepository playerRepository;
    //con el JwtService se le pasa el token ya sea para validar o para extraer el usuario
    @Autowired
    private JwtService jwtService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private PlayerService playerService;

    public AuthResponse login(LoginRequest loginRequest) {
        //se busca el usuario por el nombre de usuario
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        final UserDetails userDetails = playerService.userDetailsService().loadUserByUsername(loginRequest.getUsername());
        final Player player = playerRepository.findByUserName(loginRequest.getUsername()).orElseThrow(() -> new IllegalArgumentException("Invalid email or password."));
        final String token = jwtService.generateToken(userDetails);
        return new AuthResponse(token, player.getUsername(), player.getType());
    }
}
