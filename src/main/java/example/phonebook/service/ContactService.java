package example.phonebook.service;

import example.phonebook.data.entity.Contact;
import example.phonebook.data.repository.ContactRepository;
import example.phonebook.data.repository.PhoneBookRepository;
import example.phonebook.data.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ContactService {

    @Autowired
    ContactRepository contactRepository;

    @Autowired
    PhoneBookRepository phoneBookRepository;

    /**
     * Поиск контакта по ИД
     *
     * @param id - ИД
     * @return контакт
     */
    public Contact get(Long id) {
        return contactRepository.findContactById(id);
    }

    /**
     * Поиск контакта по номеру
     *
     * @param number - номер
     * @return контакт
     */
    public Contact getContactByNumber(String number) {
        return contactRepository.findContactByNumber(number);
    }

    /**
     * Создание нового контакта
     *
     * @param contact - новый контакт
     */
    public void create(Contact contact) {
        contactRepository.save(contact);
    }

    /**
     * Обновление контакта
     *
     * @param id         - ИД контакта
     * @param newContact - новый контакт
     */
    public void update(Long id, Contact newContact) {
        Contact contactFromDB = contactRepository.findContactById(id);
        String name = newContact.getName();
        String number = newContact.getNumber();
        contactFromDB.setName(name);
        contactFromDB.setNumber(number);
        contactRepository.save(contactFromDB);
    }

    /**
     * Удаление контакта по ИД
     *
     * @param id - ИД контакта
     */
    public void delete(Long id) {
        contactRepository.deleteById(id);
    }

    /**
     * Проверка существует ли контакт в БД
     * @param id - ИД контакта
     * @return true или false
     */
    public boolean isExist(Long id){
        return contactRepository.existsContactById(id);
    }
}
