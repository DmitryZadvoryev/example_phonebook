package example.phonebook.service;

import example.phonebook.data.entity.Contact;
import example.phonebook.data.entity.PhoneBook;
import example.phonebook.data.entity.User;
import example.phonebook.data.repository.PhoneBookRepository;
import example.phonebook.data.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.NoResultException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PhoneBookRepository phoneBookRepository;


    /**
     * Поиск пользователя по ИД
     *
     * @param id - ИД пользователя
     * @return пользователь
     */
    public User get(long id) {
            return userRepository.findUserById(id);
    }

    /**
     * Список всех пользователей (владельцев телефонных книжек)
     *
     * @return список пользователей
     */
    public List<User> getAllOwners() {
            List<PhoneBook> phoneBooks = phoneBookRepository.findAll();
            List<User> owners = new ArrayList<>();
            for (PhoneBook phoneBook : phoneBooks) {
                User user = phoneBook.getOwner();
                owners.add(user);
            }
            return owners;
    }

    /**
     * Список всех контактов пользователя
     *
     * @param id - ИД пользователя
     * @return список контактов
     */
    public List<Contact> getAllUserContacts(Long id) {
            PhoneBook phoneBook = phoneBookRepository.findPhoneBookByOwner_Id(id);
            return phoneBook.getContacts();
    }

    /**
     * Поиск пользователя по имени
     *
     * @param name - имя пользователя
     * @return пользователь
     */
    public List<User> getByName(String name) {
            return userRepository.findByNameStartingWithIgnoreCase(name);
    }

    /**
     * Создание нового пользователя
     *
     * @param user - пользователь
     */
    public void create(User user) {
        userRepository.save(user);
    }

    /**
     * Обновление пользователя
     *
     * @param id      - ИД пользователя
     * @param newUser -  новый пользователь
     */
    public void update(Long id, User newUser) {
        User userFromDB = userRepository.findUserById(id);
        String name = newUser.getName();
        userFromDB.setName(name);
        userRepository.save(userFromDB);
    }

    /**
     * Удаление пользователя
     *
     * @param id - ИД пользователя
     */
    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    /**
     * Проверка существует ли пользователь в БД
     *
     * @param id - ИД пользователя
     * @return true или false
     */
    public boolean isExist(Long id) {
        return userRepository.existsUserById(id);
    }

}
