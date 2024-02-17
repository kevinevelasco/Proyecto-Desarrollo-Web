package co.edu.javeriana.dw.proyecto.init;

import co.edu.javeriana.dw.proyecto.persistence.IPlanetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DbInitializer implements CommandLineRunner {

    @Autowired
    private IPlanetRepository planetRepository;

    @Override
    public void run(String... args) throws Exception {

    }
    
}
