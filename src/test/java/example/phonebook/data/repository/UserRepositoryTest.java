package example.phonebook.data.repository;

import example.phonebook.data.entity.User;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringJUnit4ClassRunner.class)
@DataJpaTest
@TestPropertySource("/application-test.properties")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(value = {"/create-phonebooks-before.sql", "/create-users-before.sql", "/create-contacts-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/delete-contacts-after.sql", "/delete-users-after.sql", "/delete-phonebooks-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class UserRepositoryTest{

    @Autowired
    UserRepository userRepository;

    @Autowired
    PhoneBookRepository phoneBookRepository;

    @Test
    void findAll() {
        List<User> all = userRepository.findAll();
        assertEquals(5,all.size());
    }

    @Test
    void findUserByIdTrue() {
        User user = userRepository.findUserById(1L);
        assertEquals(1L, user.getId());
    }

    @Test
    void findUserByIdFalse() {
        User user = userRepository.findUserById(99L);
        assertNull(user);
    }

    @Test
    void findByNameStartingWithIgnoreCase() {
        List<User> users = userRepository.findByNameStartingWithIgnoreCase("D");
        assertEquals(2, users.size());
    }

    @Test
    void findByNameStartingWithIgnoreCaseFullName() {
        List<User> users = userRepository.findByNameStartingWithIgnoreCase("Jay");
        assertEquals(1, users.size());
    }

    @Test
    void deleteById() {
        userRepository.deleteById(1L);
        User userById = userRepository.findUserById(1L);
        assertNull(userById);
    }

    @Test
    void existsUserByIdTrue() {
        boolean userExist = userRepository.existsUserById(2L);
        assertTrue(userExist);
    }

    @Test
    void existsUserByIdFalse() {
        boolean userExist = userRepository.existsUserById(99L);
        assertFalse(userExist);
    }
}