package example.phonebook.data.repository;

import example.phonebook.data.entity.Contact;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringJUnit4ClassRunner.class)
@DataJpaTest
@TestPropertySource("/application-test.properties")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(value = {"/create-phonebooks-before.sql", "/create-users-before.sql", "/create-contacts-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/delete-contacts-after.sql", "/delete-users-after.sql", "/delete-phonebooks-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class ContactRepositoryTest {

    @Autowired
    ContactRepository contactRepository;

    @Test
    void findContactByIdTrue() {
        Contact contactById = contactRepository.findContactById(1L);
        assertEquals(1, contactById.getId());
    }

    @Test
    void findContactByIdFail() {
        Contact contactById = contactRepository.findContactById(1L);
        assertEquals(1, contactById.getId());
    }

    @Test
    void findContactByNumberTrue() {
        Contact contactByNumber = contactRepository.findContactByNumber("+79216666666");
        assertEquals(contactByNumber.getNumber(), "+79216666666");
    }

    @Test
    void findContactByNumberFail() {
        Contact contactByNumber = contactRepository.findContactByNumber("+1112223344");
        assertNull(contactByNumber);
    }

    @Test
    void deleteById() {
        contactRepository.deleteById(6L);
        Contact contactById = contactRepository.findContactById(6L);
        assertNull(contactById);
    }

    @Test
    void existsContactById() {
        boolean existsContactById = contactRepository.existsContactById(5L);
        assertTrue(existsContactById);
    }

    @Test
    void save() {
        Contact contact = new Contact();
        contact.setId(55L);
        contact.setName("Bobby");
        contact.setNumber("+79650560689");
        contactRepository.save(contact);
        Contact contactByNumber = contactRepository.findContactByNumber("+79650560689");
        assertEquals(contactByNumber.getNumber(), "+79650560689");
    }
}