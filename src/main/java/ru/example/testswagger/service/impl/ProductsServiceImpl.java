package ru.example.testswagger.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.example.testswagger.controller.dto.NewProductsDTO;
import ru.example.testswagger.model.Customers;
import ru.example.testswagger.model.Products;
import ru.example.testswagger.repository.CustomersRepository;
import ru.example.testswagger.repository.ProductsRepository;
import ru.example.testswagger.service.ProductsService;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductsServiceImpl implements ProductsService {

    @Autowired
    private final ProductsRepository productsRepository;
    @Autowired
    private final CustomersRepository customersRepository;

    @Override
    public Products saveProduct(UUID customersId, NewProductsDTO newProduct) {
        Customers customer = customersRepository.findById(customersId).orElse(null);
        Products product = new Products();
        if (customer != null) {
            product.setCreated(LocalDateTime.now());
            product.setDecimal(newProduct.getDecimal());
            product.setDescription(newProduct.getDescription());
            product.setId(UUID.randomUUID());
            product.setTitle(newProduct.getTitle());
            product.setCustomer(customer);
            Optional.of(customer.getProducts()).orElse(new ArrayList<>()).add(product);
            productsRepository.save(product);
        }
        return product;
    }

    @Override
    public List<Products> getCustomerProductsDTO(UUID customersId) {
        return productsRepository.findAllByCustomerId(customersId)
                .stream().filter(products -> !products.isDeleted()).collect(Collectors.toList());
    }

    @Override
    public Products getProduct(UUID productsId) {
       return productsRepository.findById(productsId).orElse(null);
    }

    @Override
    public Products editProductDTO(UUID productsId, NewProductsDTO newProductDTO) {
        Products product = getProduct(productsId);
        if (product != null && !product.isDeleted()){
            product.setTitle(newProductDTO.getTitle());
            product.setDescription(newProductDTO.getDescription());
            product.setDecimal(newProductDTO.getDecimal());
            product.setModified(LocalDateTime.now());
            productsRepository.save(product);
        }
        return product;

    }

    @Override
    public boolean deleteProduct(UUID productsId) {
        Products product = getProduct(productsId);
        if (product != null) {
            product.setDeleted(true);
        }
        return product != null;
    }
}
