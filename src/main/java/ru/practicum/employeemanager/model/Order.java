package ru.practicum.employeemanager.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "orders", schema = "public")
public class Order {
    //Новые сущности нельзя хранить в картах так как нет id
    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long id;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 30)
    private Status status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id",
            foreignKey = @ForeignKey(name = "fk_orders_customer_id",
                    foreignKeyDefinition = "FOREIGN KEY (customer_id) REFERENCES customers(customer_id) ON DELETE SET NULL"))
    //Влияет только если генерация через Hibernate
    private Customer customer;

    @OneToMany(
            mappedBy = "order",
            cascade = {CascadeType.PERSIST, CascadeType.MERGE},
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    @OnDelete(action = OnDeleteAction.CASCADE) // Каскадное удаление на уровне базы, чтобы избежать n+1
    private List<OrderItem> items = new ArrayList<>();


    // Автоматическое заполнение при создании
    @PrePersist
    void onCreate() {
        if (createdAt == null) {
            createdAt = Instant.now().truncatedTo(ChronoUnit.SECONDS);
        }
    }
}
