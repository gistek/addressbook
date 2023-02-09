package org.example.addressbook.controller;

import org.example.addressbook.model.AddressBook;
import org.example.addressbook.repository.AddressBookRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
class AddressBookControllerTest {

    @Autowired
    WebTestClient webTestClient;

    @Autowired
    AddressBookRepository addressBookRepository;

    @Test
    void contextLoads() {
        assertThat(webTestClient).isNotNull();
    }

    @Test
    void testFindALl() {
        webTestClient.get()
                .uri("/api/v1/addressbooks")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .json("[{\"id\":-2,\"firstName\":\"Petr\",\"lastName\":\"Petrov\",\"phone\":\"+79999999999\",\"birthday\":\"1990-01-01\"}," +
                        "{\"id\":-1,\"firstName\":\"Alexey\",\"lastName\":\"Alexeev\",\"phone\":\"+7000000000\",\"birthday\":\"1980-01-01\"}]\n");
    }

    @Test
    void testFindALlPagination() {
        webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path("/api/v1/addressbooks")
                        .queryParam("page","0")
                        .queryParam("size","1")
                        .build())
                //.uri("/api/v1/addressbooks?page=0&size=1")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .json("[{\"id\":-2,\"firstName\":\"Petr\",\"lastName\":\"Petrov\",\"phone\":\"+79999999999\",\"birthday\":\"1990-01-01\"}]");
    }

    @Test
    void testSave() {
        AddressBook addressBook = new AddressBook(null, "Ivan", "Ivanov", "+7123456789", LocalDate.parse("2000-01-01"));

        final Long sizeBefore = addressBookRepository.findAll().count().block();
        webTestClient.post()
                .uri("/api/v1/addressbook")
                .bodyValue(addressBook)
                .header("Content-Type", "application/json")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .json("{\"firstName\": \"Ivan\"," +
                        "\"lastName\": \"Ivanov\"," +
                        "\"phone\": \"+7123456789\"," +
                        "\"birthday\": \"2000-01-01\"}");
        final Long sizeAfter = addressBookRepository.findAll().count().block();
        assertEquals(sizeBefore+1, sizeAfter);
        assertTrue(sizeBefore<sizeAfter);
    }
}