package ru.example.testswagger.controller.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.example.testswagger.controller.CustomersController;
import ru.example.testswagger.controller.ProductsController;
import ru.example.testswagger.controller.dto.CustomersDTO;
import ru.example.testswagger.controller.dto.NewProductsDTO;
import ru.example.testswagger.controller.dto.ProductsDTO;
import ru.example.testswagger.model.Customers;
import ru.example.testswagger.model.Products;
import ru.example.testswagger.service.CustomersService;
import ru.example.testswagger.service.ProductsService;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
public class ProductsControllerImpl implements ProductsController {

    @Autowired
    private final ProductsService productsService;

    @Override
    public ResponseEntity<?> saveProduct(UUID customersId, NewProductsDTO newProduct) {
        productsService.saveProduct(customersId, newProduct);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<List<ProductsDTO>> getCustomerProducts(UUID customersId) {
        List<Products> customerProductsDTO = productsService.getCustomerProductsDTO(customersId);
        return customerProductsDTO != null
                ? new ResponseEntity<>(mappingProductsList(customerProductsDTO), HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @Override
    public ResponseEntity<ProductsDTO> getProduct(UUID productsId) {
        Products product = productsService.getProduct(productsId);
        return product != null
                ? new ResponseEntity<>(mappingProducts(product), HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @Override
    public ResponseEntity<?> editProduct(UUID productsId, NewProductsDTO newProductDTO) {
        return productsService.editProductDTO(productsId, newProductDTO) != null
                ? new ResponseEntity<>(HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }

    @Override
    public ResponseEntity<?> deleteProduct(UUID productsId) {
        return productsService.deleteProduct(productsId)
                ? new ResponseEntity<>(HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }

    private ProductsDTO mappingProducts(Products product){
        ProductsDTO productsDTO = new ProductsDTO();
        productsDTO.setId(product.getId());
        productsDTO.setCreated(product.getCreated());
        productsDTO.setModified(product.getModified());
        productsDTO.setCustomerId(product.getCustomer().getId());
        productsDTO.setDecimal(product.getDecimal());
        productsDTO.setDescription(product.getDescription());
        productsDTO.setTitle(product.getTitle());
        return productsDTO;
    }

    private List<ProductsDTO> mappingProductsList(List<Products> productsList){
        List<ProductsDTO> productsDTOList = new ArrayList<>();
        productsList.forEach(product -> productsDTOList.add(mappingProducts(product)));
        return productsDTOList;
    }
}
