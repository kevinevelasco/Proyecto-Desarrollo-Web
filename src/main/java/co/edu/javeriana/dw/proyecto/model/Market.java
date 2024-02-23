package co.edu.javeriana.dw.proyecto.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Market {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "planet_id")
    private Planet planet;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    private Integer stock;
    private Integer demandFactor;
    private Integer supplyFactor;
    private Double buyPrice;
    private Double sellPrice;
}
