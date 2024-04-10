package co.edu.javeriana.dw.proyecto.controllers.newcontrollers;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SpacecraftPlanet {
    Long idPlanet;
    Long idUser;
}