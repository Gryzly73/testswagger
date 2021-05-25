package ru.example.testswagger.service.impl;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.example.testswagger.controller.dto.CustomersDTO;
import ru.example.testswagger.controller.dto.NewCustomersDTO;
import ru.example.testswagger.model.Customers;
import ru.example.testswagger.repository.CustomersRepository;
import ru.example.testswagger.service.CustomersService;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class CustomersServiceImpl implements CustomersService {

    @Autowired
    private CustomersRepository customersRepository;
   
    @Override
    public Customers saveCustomer(NewCustomersDTO newCustomerDTO) {

        Customers newCustomer = new Customers();
        newCustomer.setCreated(LocalDateTime.now());
        newCustomer.setTitle(newCustomerDTO.getTitle());
        newCustomer.setId(UUID.randomUUID());
        newCustomer.setDeleted(false);
        return customersRepository.save(newCustomer);
    }

    @Override
    public List<Customers> getAllCustomers() {
        return customersRepository.findAll()
                .stream().filter(customers -> !customers.isDeleted()).collect(Collectors.toList());
    }

    @Override
    public Customers getCustomer(UUID customersId) {
       return customersRepository.findById(customersId).orElse(null);
    }

    @Override
    public boolean deleteCustomer(UUID customersId) {
        Customers customers = getCustomer(customersId);
        if (customers != null) {
            customers.setDeleted(true);
        }
        return customers != null;
    }

    @Override
    public Customers editCustomer(UUID customersId, NewCustomersDTO newCustomer) {
        Customers customer = getCustomer(customersId);
        if (customer != null && !customer.isDeleted()){
            customer.setTitle(newCustomer.getTitle());
            customer.setModified(LocalDateTime.now());
            customer = customersRepository.save(customer);
        }
        return customer;
    }
}
