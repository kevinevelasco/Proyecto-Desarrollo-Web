package co.edu.javeriana.dw.proyecto.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;
import jakarta.validation.constraints.NotNull;

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

    @JsonIgnore
    @NotNull
    @ManyToOne
    @JoinColumn(name = "planet_id", nullable = false)
    private Planet planet;

   // @JsonIgnore
    @NotNull
    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @NotNull(message = "El stock no puede ser nulo")
    @Column(nullable = false)
    private Integer stock;

    @NotNull(message = "El factor demandante no puede ser nulo")
    @Column(nullable = false)
    private Integer demandFactor;

    @NotNull(message = "El factor de oferta no puede ser nulo")
    @Column(nullable = false)
    private Integer supplyFactor;

    @NotNull(message = "El precio de compra no puede ser nulo")
    @Column(nullable = false)
    private Double buyPrice;

    @NotNull(message = "El precio de venta no puede ser nulo")
    @Column(nullable = false)
    private Double sellPrice;

    public Market(Planet planet, Product product, Integer stock, Integer demandFactor, Integer supplyFactor, Double buyPrice, Double sellPrice) {
        this.planet = planet;
        this.product = product;
        this.stock = stock;
        this.demandFactor = demandFactor;
        this.supplyFactor = supplyFactor;
        this.buyPrice = buyPrice;
        this.sellPrice = sellPrice;
    }    

}
