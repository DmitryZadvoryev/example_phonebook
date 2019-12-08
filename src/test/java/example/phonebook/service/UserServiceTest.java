package example.phonebook.service;

import example.phonebook.data.entity.Contact;
import example.phonebook.data.entity.User;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DirtiesContext
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@TestPropertySource("/application-test.properties")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(value = {"/create-phonebooks-before.sql", "/create-users-before.sql", "/create-contacts-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/delete-contacts-after.sql", "/delete-users-after.sql", "/delete-phonebooks-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class UserServiceTest {

    @Autowired
   private UserService userService;

    @Test
    void getTrue() {
        User user = userService.get(1L);
        assertEquals(1L, user.getId());
    }
    @Test
    void getFalse() {
        User user = userService.get(999L);
        assertNull(user);
    }

    @Test
    void getAllOwners() {
        List<User> allOwners = userService.getAllOwners();
        assertEquals(5, allOwners.size());
    }

    @Test
    void getAllUserContacts() {
        List<Contact> allUserContacts = userService.getAllUserContacts(1L);
        assertEquals(3, allUserContacts.size());
    }

    @Test
    void getByName() {
        List<User> users = userService.getByName("d");
        assertEquals(2, users.size());
    }

    @Test
    void create() {
        User user = new User("Patrick");
        userService.create(user);
        List<User> usersByName = userService.getByName("Patrick");
        assertEquals(user.getName(), usersByName.get(0).getName());
    }

    @Test
    void update() {
        User user =  new User("Patrick");
        userService.update(5L, user);
        User userById = userService.get(5L);
        assertEquals("Patrick",userById.getName());
    }

    @Test
    void delete() {
        userService.delete(3L);
        User user = userService.get(3L);
        assertNull(user);
    }

    @Test
    void isExistTrue() {
        boolean exist = userService.isExist(3L);
        assertTrue(exist);
    }

    @Test
    void isExistFalse() {
        boolean exist = userService.isExist(99L);
        assertFalse(exist);
    }

}