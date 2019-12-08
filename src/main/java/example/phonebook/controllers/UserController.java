package example.phonebook.controllers;

import example.phonebook.data.entity.Contact;
import example.phonebook.data.entity.User;
import example.phonebook.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static example.phonebook.constants.Constants.VERSION;
import static example.phonebook.validation.Validation.requireNull;

@RestController
@RequestMapping(VERSION + "phonebooks/users")
public class UserController {

    @Autowired
    UserService userService;

    /**
     * Возвращает список всех пользователей (владельцев телефонных книжек)
     *
     * @return возвращает список всех пользователей
     */
    @GetMapping(value = "/owners")
    public ResponseEntity<List<User>> list() {
        List<User> users = userService.getAllOwners();
        if (users.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(users, HttpStatus.OK);
        }
    }

    /**
     * Возвращает список контактов пользователя
     *
     * @param id - ИД пользователя
     * @return возвращает список контактов пользователя
     */
    @GetMapping(value = "{id}/contacts")
    public ResponseEntity<List<Contact>> getAllUserContacts(@PathVariable("id") Long id) {
        List<Contact> allUserContaсts = userService.getAllUserContacts(id);
        if (allUserContaсts.isEmpty() || allUserContaсts == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(allUserContaсts, HttpStatus.OK);
        }
    }

    /**
     * Поиск пользователя по имени
     *
     * @param name имя
     * @return возвращает пользователя
     */
    @GetMapping(value = {"names/{name}"})
    public ResponseEntity<List<User>> getUserByName(@PathVariable("name") String name) {
        List<User> user = userService.getByName(name);
        if (requireNull(user)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(user, HttpStatus.OK);
        }
    }

    /**
     * Возвращает пользователя по id
     *
     * @param id - ИД пользователя
     */
    @GetMapping(value = "{id}")
    public ResponseEntity<User> getOne(@PathVariable("id") Long id) {
        User user = userService.get(id);
        if (requireNull(user)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(user, HttpStatus.OK);
        }
    }

    /**
     * Создание нового пользователя
     *
     * @param user пользователь
     */
    @PostMapping
    public ResponseEntity<User> create(@RequestBody User user) {
        if (requireNull(user)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else {
            userService.create(user);
            return new ResponseEntity<>(user, HttpStatus.CREATED);
        }
    }

    /**
     * Редактирование пользователя
     *
     * @param user пользователь
     */
    @PutMapping(value = "{id}")
    public ResponseEntity<User> update(@PathVariable("id") Long id, @RequestBody User user) {
        if (requireNull(user)) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        } else {
            userService.update(id, user);
            return new ResponseEntity(user, HttpStatus.OK);
        }
    }

    /**
     * Удаление пользователя
     *
     * @param id пользователя
     */
    @DeleteMapping(value = "{id}")
    public ResponseEntity<User> delete(@PathVariable("id") Long id) {
        if (!userService.isExist(id)) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        } else {
            userService.delete(id);
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }
    }
}

