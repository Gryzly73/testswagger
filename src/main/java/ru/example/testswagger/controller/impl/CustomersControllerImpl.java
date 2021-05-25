package ru.example.testswagger.controller.impl;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.example.testswagger.controller.CustomersController;
import ru.example.testswagger.controller.dto.CustomersDTO;
import ru.example.testswagger.controller.dto.NewCustomersDTO;
import ru.example.testswagger.model.Customers;
import ru.example.testswagger.service.CustomersService;


import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
public class CustomersControllerImpl implements CustomersController {

    @Autowired
    private CustomersService customersService;

    @Override
    public ResponseEntity<?> saveCustomer(NewCustomersDTO newCustomer) {
        customersService.saveCustomer(newCustomer);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<List<CustomersDTO>> getAllCustomers() {
        List<Customers> allCustomers = customersService.getAllCustomers();
        return allCustomers != null
                ? new ResponseEntity<>(mappingCustomersList(allCustomers), HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @Override
    public ResponseEntity<CustomersDTO> getCustomer(UUID customerId) {
        Customers customer = customersService.getCustomer(customerId);
        return customer != null
                ? new ResponseEntity<>(mappingCustomers(customer), HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @Override
    public ResponseEntity<?> deleteCustomer(UUID customersId) {
        return customersService.deleteCustomer(customersId)
                ? new ResponseEntity<>(HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }

    @Override
    public ResponseEntity<?> editCustomer(UUID customersId, NewCustomersDTO newCustomer) {
        return customersService.editCustomer(customersId, newCustomer) != null
                ? new ResponseEntity<>(HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }

    private CustomersDTO mappingCustomers(Customers customers){
        CustomersDTO customersDTO = new CustomersDTO();
        customersDTO.setId(customers.getId());
        customersDTO.setCreated(customers.getCreated());
        customersDTO.setModified(customers.getModified());
        customersDTO.setTitle(customers.getTitle());
        return customersDTO;
    }

    private List<CustomersDTO> mappingCustomersList(List<Customers> customersList){
        List<CustomersDTO> customersDTOList = new ArrayList<>();
        customersList.forEach(customers -> customersDTOList.add(mappingCustomers(customers)));
        return customersDTOList;
    }

}
