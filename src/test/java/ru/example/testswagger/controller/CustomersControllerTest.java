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

import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.Assert;
import ru.example.testswagger.TestswaggerApplication;
import ru.example.testswagger.controller.dto.CustomersDTO;
import ru.example.testswagger.controller.dto.NewCustomersDTO;
import ru.example.testswagger.model.Customers;
import ru.example.testswagger.repository.CustomersRepository;
import ru.example.testswagger.repository.ProductsRepository;
import ru.example.testswagger.service.CustomersService;
import ru.example.testswagger.service.ProductsService;

import javax.transaction.Transactional;

import java.io.UnsupportedEncodingException;
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
class CustomersControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ProductsRepository productsRepository;
    @Autowired
    private CustomersRepository customersRepository;
    @Autowired
    private CustomersService customersService;
    @Autowired
    private ObjectMapper objectMapper;

    private final CustomersDTO customersDTO_1 = new CustomersDTO();
    private final CustomersDTO customersDTO_2 = new CustomersDTO();

    @BeforeEach
    void init(){
        productsRepository.deleteAll();
        customersRepository.deleteAll();
    }

    @Test
    @WithMockUser
    void saveCustomer() throws Exception {
        var customersDTO = new NewCustomersDTO();
        customersDTO.setTitle("title");
        String request = objectMapper.writeValueAsString(customersDTO);
        mockMvc.perform(
                        post("/customers")
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(request))
                        .andExpect(status().isCreated()).andReturn();

        assertEquals(customersRepository.findAll().size(), 1);
        var actual = customersRepository.findAll().get(0);
        assertEquals(customersDTO.getTitle(), actual.getTitle());
        assertNull(actual.getModified());
        assertNotNull(actual.getCreated());
        assertFalse(actual.isDeleted());
    }

    @Test
    void getAllCustomers() throws Exception {

       createListCustomers();
        var response =
                mockMvc.perform(
                        get("/customers"))
                        .andExpect(status().isOk()).andReturn();
        String content = response.getResponse().getContentAsString();
        List<CustomersDTO> savedList = objectMapper.readValue(content,
                new TypeReference<>() {
                });
        assertEquals(2, savedList.size());
    }
    @Test
    void deleteCustomer() throws Exception {
        createListCustomers();
        UUID uuid_1 = customersRepository.findAll().get(0).getId();
        assertEquals(customersService.getAllCustomers().size(), 2);
        mockMvc.perform(
                        delete("/customers/"+uuid_1.toString())
                                .contentType(MediaType.APPLICATION_JSON_VALUE))
                        .andExpect(status().isOk()).andReturn();
        assertEquals(customersService.getAllCustomers().size(), 1);
    }

    @Test
    void getCustomer() throws Exception {
        createListCustomers();
        UUID uuid_1 = customersRepository.findAll().get(0).getId();
        var response =
                mockMvc.perform(
                        get("/customers/"+uuid_1.toString())
                                .contentType(MediaType.APPLICATION_JSON_VALUE))
                        .andExpect(status().isOk()).andReturn();
        String content = response.getResponse().getContentAsString();

        var actual = objectMapper.readValue(content, CustomersDTO.class);
        var expected = customersRepository.findAll().get(0);
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getTitle(), actual.getTitle());
        assertEquals(expected.getCreated(), actual.getCreated());
        assertEquals(expected.getModified(), actual.getModified());
        assertEquals(expected.isDeleted(), actual.isDeleted());
    }

    @Test
    void editCustomer() throws Exception {
        createListCustomers();
        UUID uuid_1 = customersRepository.findAll().get(0).getId();
        CustomersDTO updateCustomersDTO = new CustomersDTO();
        updateCustomersDTO.setTitle("title_new");
        updateCustomersDTO.setId(uuid_1);
        String request = objectMapper.writeValueAsString(updateCustomersDTO);
        mockMvc.perform(
                        put("/customers/"+uuid_1.toString())
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(request))
                        .andExpect(status().isOk()).andReturn();

        Customers actual = customersRepository.getById(uuid_1);

        assertEquals(updateCustomersDTO.getId(), actual.getId());
        assertEquals(updateCustomersDTO.getTitle(), actual.getTitle());
        assertNotNull(actual.getModified());
        assertEquals(updateCustomersDTO.isDeleted(), actual.isDeleted());
    }

    private void createListCustomers() throws JsonProcessingException {
        customersDTO_1.setTitle("title_1");
        customersService.saveCustomer(customersDTO_1);
        String request_1 = objectMapper.writeValueAsString(customersDTO_1);
        customersDTO_2.setTitle("title_2");
        customersService.saveCustomer(customersDTO_2);
        String request_2 = objectMapper.writeValueAsString(customersDTO_2);
    }
}