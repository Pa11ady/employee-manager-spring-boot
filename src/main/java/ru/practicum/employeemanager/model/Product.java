package ru.practicum.employeemanager.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "products", schema = "public",
        uniqueConstraints = @UniqueConstraint(name = "uc_product_name", columnNames = "name"))
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    Long id;

    @EqualsAndHashCode.Include
    @NotBlank
    @Size(max = 255)
    @Column(name = "name", nullable = false)
    String name;

    @Size(max = 511)
    @Column(name = "description")
    String description;

    @NotNull
    @Column(name = "price", nullable = false)
    BigDecimal price;
}
