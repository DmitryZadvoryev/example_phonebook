package example.phonebook.controllers;

import example.phonebook.data.entity.Contact;
import example.phonebook.service.ContactService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.jdbc.Sql;

import java.net.URI;

import static example.phonebook.util.ObjectToJSON.mapToJson;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(value = {"/create-phonebooks-before.sql", "/create-users-before.sql", "/create-contacts-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/delete-contacts-after.sql", "/delete-users-after.sql", "/delete-phonebooks-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class ContactControllerTest {

    @LocalServerPort
    int randomServerPort;

    @Autowired
    TestRestTemplate restTemplate;

    @Autowired
    ContactService contactService;

    /**
     * Получение одного существующего контакта
     */
    @Test
    void getOneTrue() throws Exception {
        final String baseUri = "http://localhost:" + randomServerPort + "/api/v1/phonebooks/contacts/4";
        URI uri = new URI(baseUri);
        ResponseEntity<Contact> responseEntity = restTemplate.getForEntity(uri, Contact.class);
        Contact body = responseEntity.getBody();
        String expected = mapToJson(body);
        Contact contact = contactService.get(4L);
        String actual = mapToJson(contact);
        assertEquals(actual, expected);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    /**
     * Получение одного несуществующего контакта
     */

    @Test
    void getOneFail() throws Exception {
        final String baseUri = "http://localhost:" + randomServerPort + "/api/v1/phonebooks/contacts/99";
        URI uri = new URI(baseUri);
        ResponseEntity<Contact> responseEntity = restTemplate.getForEntity(uri, Contact.class);
        Contact contact = contactService.get(99L);
        assertNull(contact);
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    /**
     * Получение существующего контакта по номеру
     */
    @Test
    void getContactByNumberTrue() throws Exception {
        final String baseUri = "http://localhost:" + randomServerPort + "/api/v1/phonebooks/contacts/number/+79217775556";
        URI uri = new URI(baseUri);
        ResponseEntity<Contact> responseEntity = restTemplate.getForEntity(uri, Contact.class);
        Contact body = responseEntity.getBody();
        String actual = mapToJson(body);
        Contact contact = contactService.getContactByNumber("+79217775556");
        String expected = mapToJson(contact);
        assertEquals(mapToJson(expected), mapToJson(actual));
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    /**
     * Получение несуществующего контакта
     */
    @Test
    void getContactByNumberFail() throws Exception {
        final String baseUri = "http://localhost:" + randomServerPort + "/api/v1/phonebooks/contacts/number/+79996669966";
        URI uri = new URI(baseUri);
        ResponseEntity<Contact> responseEntity = restTemplate.getForEntity(uri, Contact.class);
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    /**
     * Создание контакта
     */
    @Test
    void createTrue() throws Exception {
        final String baseUri = "http://localhost:" + randomServerPort + "/api/v1/phonebooks/contacts";
        URI uri = new URI(baseUri);
        Contact contact = new Contact("Bob", "+79650560689");
        HttpEntity request = new HttpEntity(contact);
        ResponseEntity<Contact> contactResponseEntity = restTemplate.exchange(uri, HttpMethod.POST, request, Contact.class);
        Contact actual = contactResponseEntity.getBody();
        Contact expected = contactService.getContactByNumber("+79650560689");
        assertEquals(mapToJson(expected), mapToJson(actual));
        assertEquals(HttpStatus.CREATED, contactResponseEntity.getStatusCode());
    }

    /**
     * Создание контакта, передается NULL
     */
    @Test
    void createFailContact_NULL() throws Exception {
        final String baseUri = "http://localhost:" + randomServerPort + "/api/v1/phonebooks/contacts";
        URI uri = new URI(baseUri);
        Contact contact = null;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity request = new HttpEntity(contact, headers);
        ResponseEntity<Contact> responseEntity = restTemplate.exchange(uri, HttpMethod.POST, request, Contact.class);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    /**
     * Создание контакта с номером, который не проходит валидацию
     */
    @Test
    void createFailWrongNumber() throws Exception {
        final String baseUri = "http://localhost:" + randomServerPort + "/api/v1/phonebooks/contacts";
        URI uri = new URI(baseUri);
        Contact contact = new Contact("Bob", "79650560689");
        HttpEntity request = new HttpEntity(contact);
        ResponseEntity<Contact> contactResponseEntity = restTemplate.postForEntity(uri, request, Contact.class);
        Contact expected = contactService.getContactByNumber("79650560689");
        assertNull(expected);
        assertEquals(HttpStatus.BAD_REQUEST, contactResponseEntity.getStatusCode());
    }

    /**
     * Обновление контакта
     */
    @Test
    void updateTrue() throws Exception {
        final String baseUri = "http://localhost:" + randomServerPort + "/api/v1/phonebooks/contacts/1";
        URI uri = new URI(baseUri);
        Contact contact = new Contact("Bob", "+79650560689");
        contact.setId(1L);
        HttpEntity request = new HttpEntity(contact);
        ResponseEntity<Contact> contactResponseEntity = restTemplate.exchange(uri, HttpMethod.PUT, request, Contact.class);
        Contact actual = contactResponseEntity.getBody();
        Contact expected = contactService.getContactByNumber("+79650560689");
        assertEquals(mapToJson(expected), mapToJson(actual));
        assertEquals(HttpStatus.OK, contactResponseEntity.getStatusCode());
    }

    /**
     * Обновление контакта, передается NULL
     */
    @Test
    void updateFail_NULL() throws Exception {
        final String baseUri = "http://localhost:" + randomServerPort + "/api/v1/phonebooks/contacts/1";
        URI uri = new URI(baseUri);
        Contact contact = null;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity request = new HttpEntity(contact, headers);
        ResponseEntity<Contact> contactResponseEntity = restTemplate.exchange(uri, HttpMethod.PUT, request, Contact.class);
        assertEquals(HttpStatus.BAD_REQUEST, contactResponseEntity.getStatusCode());
    }

    /**
     * Удаление контакта
     */
    @Test
    void deleteTrue() throws Exception {
        final String baseUri = "http://localhost:" + randomServerPort + "/api/v1/phonebooks/contacts/1";
        URI uri = new URI(baseUri);
        Contact contact = new Contact();
        contact.setId(1L);
        HttpEntity<Contact> request = new HttpEntity<>(contact);
        ResponseEntity<Contact> contactResponseEntity = restTemplate.exchange(uri, HttpMethod.DELETE, request, Contact.class);
        Contact expected = contactService.getContactByNumber("+79217775544");
        assertNull(expected);
        assertEquals(HttpStatus.NO_CONTENT, contactResponseEntity.getStatusCode());
    }

    /**
     * Удаление несуществующего контакта
     */
    @Test
    void deleteFail() throws Exception {
        final String baseUri = "http://localhost:" + randomServerPort + "/api/v1/phonebooks/contacts/99";
        URI uri = new URI(baseUri);
        Contact contact = new Contact();
        contact.setId(99L);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Contact> request = new HttpEntity<>(contact, headers);
        ResponseEntity<Contact> contactResponseEntity = restTemplate.exchange(uri, HttpMethod.DELETE, request, Contact.class);
        assertEquals(HttpStatus.NOT_FOUND, contactResponseEntity.getStatusCode());
    }
}