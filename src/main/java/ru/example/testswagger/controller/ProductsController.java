package ru.example.testswagger.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.example.testswagger.controller.dto.NewProductsDTO;
import ru.example.testswagger.controller.dto.ProductsDTO;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RequestMapping("/")
@Tag(name = "Products")
@RestController
public interface ProductsController {

    @GetMapping(value = "customers/{customersId}/products", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @Operation(summary = "Return paginated list of all customer products", description = "Return paginated list of all customer products")
    ResponseEntity<List<ProductsDTO>> getCustomerProducts(
            @PathVariable("customersId") UUID customersId
    );

    @PostMapping(value = "customers/{customersId}/products", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @Operation(summary = "Create new product for customer", description = "Create new product for customer")
    ResponseEntity<?> saveProduct(
            @PathVariable("customersId") UUID customersId,
            @Valid @RequestBody NewProductsDTO newProduct
    );

    @DeleteMapping(value = "products/{productsId}")
    @Operation(summary = "Delete product", description = "Delete product")
    ResponseEntity<?> deleteProduct(
            @PathVariable("productsId") UUID productsId
    );

    @GetMapping(value = "products/{productsId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @Operation(summary = "Return product by Id", description = "Return product by Id")
    ResponseEntity<ProductsDTO> getProduct(
            @PathVariable("productsId") UUID productsId
    );

    @PutMapping(value = "products/{productsId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @Operation(summary = "Edit product", description = "Edit product")
    ResponseEntity<?> editProduct(
            @PathVariable("productsId") UUID productsId,
            @Valid @RequestBody NewProductsDTO newProductsDTO
    );
}
