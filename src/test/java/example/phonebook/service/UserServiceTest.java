package example.phonebook.service;

import example.phonebook.data.entity.Contact;
import example.phonebook.data.entity.User;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DirtiesContext
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(value = {"/create-phonebooks-before.sql", "/create-users-before.sql", "/create-contacts-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/delete-contacts-after.sql", "/delete-users-after.sql", "/delete-phonebooks-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class UserServiceTest {

    @Autowired
    private UserService userService;


    /**
     * Получение пользователя
     */
    @Test
    void getTrue() {
        User user = userService.get(1L);
        assertEquals(1L, user.getId());
    }

    /**
     * Получение несуществующего пользователя
     */
    @Test
    void getFail() {
        User user = userService.get(999L);
        assertNull(user);
    }

    /**
     * Получение все владельцев телефонных книжек
     */
    @Test
    void getAllOwners() {
        List<User> allOwners = userService.getAllOwners();
        assertEquals(3, allOwners.size());
    }

    /**
     * Получение всех контактов пользователя
     */
    @Test
    void getAllUserContacts() {
        List<Contact> allUserContacts = userService.getAllUserContacts(1L);
        assertEquals(3, allUserContacts.size());
    }

    /**
     * Получение пользователя части по имени
     */
    @Test
    void getByName() {
        List<User> users = userService.getByName("d");
        assertEquals(2, users.size());
    }

    /**
     * Создание пользователя
     */
    @Test
    void create() {
        User user = new User("Patrick");
        userService.create(user);
        List<User> usersByName = userService.getByName("Patrick");
        assertEquals(user.getName(), usersByName.get(0).getName());
    }

    /**
     * Обновление пользователя
     */
    @Test
    void update() {
        User user = new User("Patrick");
        userService.update(5L, user);
        User userById = userService.get(5L);
        assertEquals("Patrick", userById.getName());
    }

    /**
     * Удаление пользователя
     */
    @Test
    void delete() {
        userService.delete(3L);
        User user = userService.get(3L);
        assertNull(user);
    }

    /**
     * Проверка существует ли пользователь
     */
    @Test
    void isExistTrue() {
        boolean exist = userService.isExist(3L);
        assertTrue(exist);
    }

    /**
     * Есть ли пользователь с несуществующим ИД
     */
    @Test
    void isExistFail() {
        boolean exist = userService.isExist(99L);
        assertFalse(exist);
    }

}