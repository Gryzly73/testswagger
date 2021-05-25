package ru.example.testswagger.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.example.testswagger.model.Customers;
import java.util.UUID;

@Repository
public interface CustomersRepository extends JpaRepository<Customers, UUID> {
}
