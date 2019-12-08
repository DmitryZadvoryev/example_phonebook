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
        try {
            return userRepository.findUserById(id);
        } catch (NoResultException e) {
            return null;
        }
    }

    /**
     * Список всех пользователей (владельцев телефонных книжек)
     *
     * @return список пользователей
     */
    public List<User> getAllOwners() {
        try {
            List<PhoneBook> phoneBooks = phoneBookRepository.findAll();
            List<User> owners = new ArrayList<>();
            for (PhoneBook phoneBook : phoneBooks) {
                User user = phoneBook.getOwner();
                owners.add(user);
            }
            return owners;
        } catch (NoResultException e) {
            return Collections.emptyList();
        }
    }

    /**
     * Список всех контактов пользователя
     *
     * @param id - ИД пользователя
     * @return список контактов
     */
    public List<Contact> getAllUserContacts(Long id) {
        try {
            PhoneBook phoneBook = phoneBookRepository.findPhoneBookByOwner_Id(id);
            return phoneBook.getContacts();
        } catch (NoResultException e) {
            return Collections.emptyList();
        }
    }

    /**
     * Поиск пользователя по имени
     *
     * @param name - имя пользователя
     * @return пользователь
     */
    public List<User> getByName(String name) {
        try {
            return userRepository.findByNameStartingWithIgnoreCase(name);
        } catch (NoResultException e) {
            return Collections.emptyList();
        }
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
