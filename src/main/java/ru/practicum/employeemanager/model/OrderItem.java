package ru.practicum.employeemanager.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "order_items", schema = "public",
        uniqueConstraints = @UniqueConstraint(name = "uc_order_product", columnNames = {"order_id", "product_id"}))

public class OrderItem {
    //Новые сущности нельзя хранить в картах так как нет id
    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_item_id")
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    // Предположим, что количество целое, хотя реальности бывает дробное
    @NotNull
    @Column(name = "quantity", nullable = false)
    Integer quantity;
}
