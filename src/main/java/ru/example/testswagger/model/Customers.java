package ru.example.testswagger.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.*;

@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(schema = "swaggertask", name = "customers")
public class Customers {

    @Id
    private UUID id;

    @Column(nullable = false)
    private String title;

    @Column(name = "is_deleted", nullable = false)
    private boolean deleted;

    @Column(name = "created_at")
    private LocalDateTime created;

    @Column(name = "modified_at", nullable = false)
    private LocalDateTime modified;

    @Column
    @OneToMany(mappedBy = "customer", fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Products> products = new ArrayList<>();
}
