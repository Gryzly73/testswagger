package ru.example.testswagger.service;

import ru.example.testswagger.controller.dto.CustomersDTO;
import ru.example.testswagger.controller.dto.NewCustomersDTO;
import ru.example.testswagger.model.Customers;

import java.util.List;
import java.util.UUID;

public interface CustomersService {

    Customers saveCustomer(NewCustomersDTO newCustomer);

    List<Customers> getAllCustomers();

    Customers getCustomer(UUID customerId);

    boolean deleteCustomer(UUID customersId);

    Customers editCustomer(UUID productsId, NewCustomersDTO customer);
}
