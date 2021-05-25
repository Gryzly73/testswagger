package ru.example.testswagger.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(schema = "swaggertask", name = "products")
public class Products {

    @Id
    private UUID id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;

    @Column(name = "price", nullable = false)
    private Double decimal;

    @Column(name = "is_deleted", nullable = false)
    private boolean deleted;

    @Column(name = "created_at")
    private LocalDateTime created;

    @Column(name = "modified_at", nullable = false)
    private LocalDateTime modified;

    @JoinColumn(nullable = false, name = "customer_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Customers customer;
}
