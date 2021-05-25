package ru.example.testswagger.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import ru.example.testswagger.TestswaggerApplication;
import ru.example.testswagger.controller.dto.CustomersDTO;
import ru.example.testswagger.controller.dto.NewProductsDTO;
import ru.example.testswagger.controller.dto.ProductsDTO;
import ru.example.testswagger.model.Customers;
import ru.example.testswagger.model.Products;
import ru.example.testswagger.repository.CustomersRepository;
import ru.example.testswagger.repository.ProductsRepository;
import ru.example.testswagger.service.CustomersService;
import ru.example.testswagger.service.ProductsService;

import javax.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@Transactional
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = TestswaggerApplication.class)
class ProductsControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private CustomersRepository customersRepository;
    @Autowired
    private ProductsRepository productsRepository;
    @Autowired
    private ProductsService productsService;
    @Autowired
    private CustomersService customersService;
    @Autowired
    private ObjectMapper objectMapper;

    private final CustomersDTO customersDTO_1 = new CustomersDTO();
    private final CustomersDTO customersDTO_2 = new CustomersDTO();
    private final ProductsDTO productsDTO_1 = new ProductsDTO();
    private final ProductsDTO productsDTO_2 = new ProductsDTO();

    @BeforeEach
    void init(){
        productsRepository.deleteAll();
        customersRepository.deleteAll();
    }

    @Test
    void saveProduct() throws Exception {
        createListCustomers();
        UUID idCustomer_1 = customersService.getAllCustomers().get(0).getId();
        var productsDTO = new NewProductsDTO();
        productsDTO.setTitle("title");
        productsDTO.setDescription("description");
        productsDTO.setDecimal(10.0);
        productsDTO.setCustomerId(idCustomer_1);
        String request = objectMapper.writeValueAsString(productsDTO);
        mockMvc.perform(
                post("/customers/"+idCustomer_1.toString()+"/products")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(request))
                .andExpect(status().isCreated()).andReturn();

        assertEquals(productsRepository.findAll().size(), 1);
        var actual = productsRepository.findAll().get(0);
        assertEquals(productsDTO.getTitle(), actual.getTitle());
        assertNull(actual.getModified());
        assertNotNull(actual.getCreated());
        assertFalse(actual.isDeleted());
        Products products = customersRepository.findAll().get(0).getProducts().get(0);
        assertEquals(products.getId(), actual.getId());
        assertEquals(products.getTitle(), actual.getTitle());
        assertEquals(products.getCustomer().getId(), actual.getCustomer().getId());
        assertEquals(products.getDescription(), actual.getDescription());
        assertEquals(products.getCreated(), actual.getCreated());
        assertEquals(products.getModified(), actual.getModified());
        assertEquals(products.isDeleted(), actual.isDeleted());
    }

    @Test
    void getCustomerProducts() throws Exception {
       createListProducts();
       UUID idCustomer_1 = customersService.getAllCustomers().get(0).getId();
       UUID idCustomer_2 = customersService.getAllCustomers().get(1).getId();

       // 2 products in customer (idCustomer_1)
       var response_1 =
                mockMvc.perform(
                        get("/customers/"+idCustomer_1.toString()+"/products"))
                        .andExpect(status().isOk()).andReturn();
       String content_1 = response_1.getResponse().getContentAsString();
       List<ProductsDTO> savedList_1 = objectMapper.readValue(content_1,
                new TypeReference<>() {
                });
        assertEquals(2, savedList_1.size());

        // empty list in customer (idCustomer_2)
        var response_2 =
                mockMvc.perform(
                        get("/customers/"+idCustomer_2.toString()+"/products"))
                        .andExpect(status().isOk()).andReturn();
        String content_2 = response_2.getResponse().getContentAsString();
        List<ProductsDTO> savedList_2 = objectMapper.readValue(content_2,
                new TypeReference<>() {
                });
        assertEquals(0, savedList_2.size());
    }

    @Test
    void getProduct() throws Exception {
        createListProducts();
        Products expected = productsRepository.findAll().get(0);
        String toString = expected.getId().toString();
        var response_1 =
                mockMvc.perform(
                        get("/products/"+toString))
                        .andExpect(status().isOk()).andReturn();
        String content_1 = response_1.getResponse().getContentAsString();
        var actual = objectMapper.readValue(content_1, ProductsDTO.class);

        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getTitle(), actual.getTitle());
        assertEquals(expected.getCreated(), actual.getCreated());
        assertEquals(expected.getModified(), actual.getModified());
        assertEquals(expected.getDescription(), actual.getDescription());
        assertEquals(expected.getCustomer().getId(), actual.getCustomerId());
        assertEquals(expected.isDeleted(), actual.isDeleted());
    }

    @Test
    void deleteProduct() throws Exception {
        createListProducts();
        UUID idCustomer_1 = customersService.getAllCustomers().get(0).getId();
        Products expected = productsRepository.findAll().get(0);
        String toString = expected.getId().toString();
        assertEquals(productsService.getCustomerProductsDTO(idCustomer_1).size(), 2);
        mockMvc.perform(
                delete("/products/"+toString)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk()).andReturn();
        assertEquals(productsService.getCustomerProductsDTO(idCustomer_1).size(), 1);
    }

    @Test
    void editProduct() throws Exception {
        createListProducts();
        UUID idCustomer_1 = customersService.getAllCustomers().get(0).getId();
        Products expected = productsRepository.findAll().get(0);
        String toString = expected.getId().toString();

        var updateProductsDTO = new NewProductsDTO();
        updateProductsDTO.setTitle("Product_new");
        updateProductsDTO.setDescription("New description");
        updateProductsDTO.setDecimal(99.99);
        updateProductsDTO.setCustomerId(idCustomer_1);
       // updateProductsDTO.setId(idUUIDProduct_1);
        String request = objectMapper.writeValueAsString(updateProductsDTO);
        mockMvc.perform(
                put("/products/"+toString)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(request))
                .andExpect(status().isOk()).andReturn();

        var actual = productsRepository.getById(expected.getId());


        assertEquals(updateProductsDTO.getTitle(), actual.getTitle());
        assertEquals(updateProductsDTO.getDescription(), actual.getDescription());
        assertEquals(updateProductsDTO.getDecimal(), actual.getDecimal());
        assertNotNull(actual.getModified());
    }

    private void createListCustomers() throws JsonProcessingException {
        customersDTO_1.setTitle("title_1");
   //     customersDTO_1.setCreated(LocalDateTime.now());
//        customersDTO_1.setId(idUUID_1);
        customersService.saveCustomer(customersDTO_1);
        String request_1 = objectMapper.writeValueAsString(customersDTO_1);

        customersDTO_2.setTitle("title_2");
  //      customersDTO_2.setCreated(LocalDateTime.now());
  //      customersDTO_2.setId(idUUID_2);
        customersService.saveCustomer(customersDTO_2);
        String request_2 = objectMapper.writeValueAsString(customersDTO_2);
    }

    private void createListProducts() throws JsonProcessingException {
        createListCustomers();
        UUID idCustomer_1 = customersService.getAllCustomers().get(0).getId();
        UUID idCustomer_2 = customersService.getAllCustomers().get(1).getId();

        productsDTO_1.setTitle("Product_1");
        productsDTO_1.setCreated(LocalDateTime.now().minusHours(24));
        productsDTO_1.setDecimal(12.0);
        productsDTO_1.setDescription("Description_1");
        productsDTO_1.setId(idCustomer_1);
        productsService.saveProduct(idCustomer_1, productsDTO_1);
        String requestProduct_1 = objectMapper.writeValueAsString(productsDTO_1);

        productsDTO_2.setTitle("Product_2");
        productsDTO_2.setCreated(LocalDateTime.now());
        productsDTO_2.setDecimal(62.0);
        productsDTO_2.setId(idCustomer_1);
        productsService.saveProduct(idCustomer_1, productsDTO_2);
        String requestProduct_2 = objectMapper.writeValueAsString(productsDTO_2);
    }
}