package example.phonebook.data.repository;

import example.phonebook.data.entity.PhoneBook;
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
class PhoneBookRepositoryTest {

    @Autowired
    PhoneBookRepository phoneBookRepository;

    @Autowired
    UserRepository userRepository;

    @Test
    void findAll() {
        List<PhoneBook> all = phoneBookRepository.findAll();
        assertEquals(3, all.size());
    }

    @Test
    void deleteById() {
        phoneBookRepository.deleteById(1L);
        PhoneBook phoneBookById = phoneBookRepository.findPhonebookById(1L);
        assertNull(phoneBookById);
    }

    @Test
    void findByIdTrue() {
        PhoneBook phoneBookById = phoneBookRepository.findPhonebookById(1L);
        assertNotNull(phoneBookById);
    }

    @Test
    void findByIdFail() {
        PhoneBook phoneBookById = phoneBookRepository.findPhonebookById(99L);
        assertNull(phoneBookById);
    }

    @Test
    void findPhoneBookByOwner_IdTrue() {
        PhoneBook phoneBookByOwner_id = phoneBookRepository.findPhoneBookByOwner_Id(4L);
        assertNotNull(phoneBookByOwner_id);
    }

    @Test
    void findPhoneBookByOwner_IdFail() {
        PhoneBook phoneBookByOwner_id = phoneBookRepository.findPhoneBookByOwner_Id(99L);
        assertNull(phoneBookByOwner_id);
    }
}