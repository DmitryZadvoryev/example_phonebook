package example.phonebook.service;

import example.phonebook.data.entity.Contact;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.jupiter.api.Assertions.*;

@DirtiesContext
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(value = {"/create-phonebooks-before.sql", "/create-users-before.sql", "/create-contacts-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/delete-contacts-after.sql", "/delete-users-after.sql", "/delete-phonebooks-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class ContactServiceTest {

    @Autowired
    ContactService contactService;


    /**
     * Получение контакта
     */
    @Test
    void getTrue() {
        Contact contact = contactService.get(1L);
        assertEquals(1L, contact.getId());
    }

    /**
     * Получение несуществующего контакта
     */
    @Test
    void getFail() {
        Contact contact = contactService.get(99L);
        assertNull(contact);
    }

    /**
     * Получение контакта по номеру
     */
    @Test
    void getContactByNumberTrue() {
        Contact contactByNumber = contactService.getContactByNumber("+79216666666");
        assertEquals("+79216666666", contactByNumber.getNumber());
    }

    /**
     * Получние контакта с несуществующим номером
     */
    @Test
    void getContactByNumberFail() {
        Contact contactByNumber = contactService.getContactByNumber("+1234567890");
        assertNull(contactByNumber);
    }

    /**
     * Создание конткта
     */
    @Test
    void create() {
        Contact contact = new Contact("Bob", "+79817217635");
        contactService.create(contact);
        Contact contactByNumber = contactService.getContactByNumber("+79817217635");
        assertEquals(contact.getName(), contactByNumber.getName());
        assertEquals(contact.getNumber(), contactByNumber.getNumber());
    }

    /**
     * Обновление контакта
     */
    @Test
    void update() {
        Contact contact = new Contact("Bob", "+89817217635");
        contactService.update(2L, contact);
        Contact contactByNumber = contactService.getContactByNumber("+89817217635");
        assertEquals(contact.getName(), contactByNumber.getName());
        assertEquals(contact.getNumber(), contactByNumber.getNumber());
        assertEquals(2L, contactByNumber.getId());
    }

    /**
     * Удаление контакта
     */
    @Test
    void delete() {
        contactService.delete(3L);
        Contact contact = contactService.get(3L);
        assertNull(contact);
    }

    /**
     * Находится ли контакт в БД с существующим ИД
     */
    @Test
    void isExistTrue() {
        boolean exist = contactService.isExist(3L);
        assertTrue(exist);
    }

    /**
     * Находится ли контакт в БД с несуществующим ИД
     */
    @Test
    void isExistFalse() {
        boolean exist = contactService.isExist(999L);
        assertFalse(exist);
    }
}