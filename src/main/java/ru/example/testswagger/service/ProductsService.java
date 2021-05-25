package ru.example.testswagger.service;

import ru.example.testswagger.controller.dto.NewProductsDTO;
import ru.example.testswagger.model.Products;
import java.util.List;
import java.util.UUID;

public interface ProductsService {

    Products saveProduct(UUID customersId, NewProductsDTO newProduct);

    List<Products> getCustomerProductsDTO(UUID customersId);

    Products getProduct(UUID productsId);

    Products editProductDTO(UUID productsId, NewProductsDTO product);

    boolean deleteProduct(UUID productsId);
}
