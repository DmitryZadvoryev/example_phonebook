package example.phonebook.controllers;

import example.phonebook.data.entity.Contact;
import example.phonebook.data.entity.User;
import example.phonebook.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.jdbc.Sql;

import java.net.URI;
import java.util.List;

import static example.phonebook.util.ObjectToJSON.mapToJson;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(value = {"/create-phonebooks-before.sql", "/create-users-before.sql", "/create-contacts-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/delete-contacts-after.sql", "/delete-users-after.sql", "/delete-phonebooks-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class UserControllerTest {

    @LocalServerPort
    int randomServerPort;

    @Autowired
    TestRestTemplate restTemplate;

    @Autowired
    UserService userService;

    /**
     * Получение списка всех владельцев телефонных книже
     */
    @Test
    void list() throws Exception {
        final String baseUri = "http://localhost:" + randomServerPort + "/api/v1/phonebooks/users/owners";
        URI uri = new URI(baseUri);
        User[] forObject = restTemplate.getForObject(uri, User[].class);
        int actual = forObject.length;
        List<User> allOwners = userService.getAllOwners();
        int expected = allOwners.size();
        assertEquals(actual, expected);
    }

    /**
     * Получение всех контактов пользователя
     */
    @Test
    void getAllUserContacts() throws Exception {
        final String baseUri = "http://localhost:" + randomServerPort + "/api/v1/phonebooks/users/1/contacts";
        URI uri = new URI(baseUri);
        User[] forObject = restTemplate.getForObject(uri, User[].class);
        int actual = forObject.length;
        List<Contact> allUserContacts = userService.getAllUserContacts(1L);
        int expected = allUserContacts.size();
        assertEquals(expected, actual);
    }

    /**
     * Получение пользователя по имени
     */
    @Test
    void getUserByNameFullNameTrue() throws Exception {
        final String baseUri = "http://localhost:" + randomServerPort + "/api/v1/phonebooks/users/names/Jay";
        URI uri = new URI(baseUri);
        User[] forObject = restTemplate.getForObject(uri, User[].class);
        int actual = forObject.length;
        List<User> users = userService.getByName("Jay");
        int expected = users.size();
        assertEquals(expected, actual);
    }

    /**
     * Получение пользователя по части имени
     */
    @Test
    void getUserByNameNotFullNameTrue() throws Exception {
        final String baseUri = "http://localhost:" + randomServerPort + "/api/v1/phonebooks/users/names/D";
        URI uri = new URI(baseUri);
        User[] forObject = restTemplate.getForObject(uri, User[].class);
        int actual = forObject.length;
        List<User> users = userService.getByName("D");
        int expected = users.size();
        assertEquals(expected, actual);
    }

    /**
     * Получение польвователя с несуществующим именем
     */
    @Test
    void getUserByNameFail() throws Exception {
        final String baseUri = "http://localhost:" + randomServerPort + "/api/v1/phonebooks/users/names/Vova";
        URI uri = new URI(baseUri);
        User[] forObject = restTemplate.getForObject(uri, User[].class);
        int actual = forObject.length;
        List<User> users = userService.getByName("Vova");
        int expected = users.size();
        assertEquals(0, expected);
        assertEquals(0, actual);
        assertEquals(expected, actual);
    }

    /**
     * Получение пользователя по ИД
     */
    @Test
    void getOneTrue() throws Exception {
        final String baseUri = "http://localhost:" + randomServerPort + "/api/v1/phonebooks/users/1";
        URI uri = new URI(baseUri);
        ResponseEntity<User> responseEntity = restTemplate.getForEntity(uri, User.class);
        User body = responseEntity.getBody();
        String expected = mapToJson(body);
        User user = userService.get(1L);
        String actual = mapToJson(user);
        assertEquals(actual, expected);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    /**
     * Получение пользователя с несуществующим ИД
     */
    @Test
    void getOneFail() throws Exception {
        final String baseUri = "http://localhost:" + randomServerPort + "/api/v1/phonebooks/users/99";
        URI uri = new URI(baseUri);
        ResponseEntity<User> responseEntity = restTemplate.getForEntity(uri, User.class);
        User user = userService.get(99L);
        assertNull(user);
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    /**
     * Создание польвователя
     */
    @Test
    void create() throws Exception {
        final String baseUri = "http://localhost:" + randomServerPort + "/api/v1/phonebooks/users";
        URI uri = new URI(baseUri);
        User user = new User("Dima");
        HttpEntity request = new HttpEntity(user);
        ResponseEntity<User> responseEntity = restTemplate.exchange(uri, HttpMethod.POST, request, User.class);
        User actual = responseEntity.getBody();
        List<User> expected = userService.getByName("Dima");
        assertEquals(mapToJson(expected.get(0)), mapToJson(actual));
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
    }

    /**
     * Создание пользователя, передается NULL
     */
    @Test
    void createFailNULL() throws Exception {
        final String baseUri = "http://localhost:" + randomServerPort + "/api/v1/phonebooks/users";
        URI uri = new URI(baseUri);
        User user = null;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity request = new HttpEntity(user, headers);
        ResponseEntity<User> responseEntity = restTemplate.exchange(uri, HttpMethod.POST, request, User.class);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    /**
     * Обновление пользователя
     */
    @Test
    void update() throws Exception {
        final String baseUri = "http://localhost:" + randomServerPort + "/api/v1/phonebooks/users/1";
        URI uri = new URI(baseUri);
        User user = new User();
        user.setId(1L);
        user.setName("Dima");
        HttpEntity request = new HttpEntity(user);
        ResponseEntity<User> responseEntity = restTemplate.exchange(uri, HttpMethod.PUT, request, User.class);
        User actual = responseEntity.getBody();
        List<User> expected = userService.getByName("Dima");
        assertEquals(mapToJson(expected.get(0)), mapToJson(actual));
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    /**
     * Обновление пользователя, передается NULL
     */
    @Test
    void updateFail_NULL() throws Exception {
        final String baseUri = "http://localhost:" + randomServerPort + "/api/v1/phonebooks/users/1";
        URI uri = new URI(baseUri);
        User user = null;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity request = new HttpEntity(user);
        ResponseEntity<User> responseEntity = restTemplate.exchange(uri, HttpMethod.PUT, request, User.class);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    /**
     * Удаление пользователя
     */
    @Test
    void deleteTrue() throws Exception {
        final String baseUri = "http://localhost:" + randomServerPort + "/api/v1/phonebooks/users/1";
        URI uri = new URI(baseUri);
        User user = new User();
        user.setId(1L);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<User> request = new HttpEntity<>(user, headers);
        ResponseEntity<User> responseEntity = restTemplate.exchange(uri, HttpMethod.DELETE, request, User.class);
        User expected = userService.get(1L);
        assertNull(expected);
        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
    }

    /**
     * Удаление несуществующего пользователя
     */
    @Test
    void deleteFail() throws Exception {
        final String baseUri = "http://localhost:" + randomServerPort + "/api/v1/phonebooks/users/99";
        URI uri = new URI(baseUri);
        User user = new User();
        user.setId(99L);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<User> request = new HttpEntity<>(user, headers);
        ResponseEntity<User> responseEntity = restTemplate.exchange(uri, HttpMethod.DELETE, request, User.class);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

}