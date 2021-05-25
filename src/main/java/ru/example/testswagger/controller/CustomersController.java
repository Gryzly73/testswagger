package ru.example.testswagger.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.example.testswagger.controller.dto.CustomersDTO;
import ru.example.testswagger.controller.dto.NewCustomersDTO;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RequestMapping("/")
@Tag(name = "Customers", description = "Набор операций для работы с customers")
public interface CustomersController {

    @GetMapping(value = "customers", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @Operation(summary = "Return paginated list of all customers", description = "Return paginated list of all customers")
    ResponseEntity<List<CustomersDTO>> getAllCustomers(
    );

    @PostMapping(value = "customers", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @Operation(summary = "Create new customer", description = "Create new customer")
    ResponseEntity<?> saveCustomer( @Valid @RequestBody NewCustomersDTO newCustomer);

    @DeleteMapping(value = "customers/{customerId}")
    @Operation(summary = "Delete customer", description = "Delete customer")
    ResponseEntity<?> deleteCustomer(
            @PathVariable("customerId") UUID customersId
    );

    @GetMapping(value = "customers/{customersId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @Operation(summary = "Return customer by Id", description = "Return customer by Id")
    ResponseEntity<CustomersDTO> getCustomer(
            @PathVariable("customersId") UUID productsId
    );

    @PutMapping(value = "customers/{customersId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @Operation(summary = "Edit customer", description = "Edit customer")
    ResponseEntity<?> editCustomer(
            @PathVariable("customersId") UUID productsId,
            @Valid @RequestBody NewCustomersDTO customer
    );
}
