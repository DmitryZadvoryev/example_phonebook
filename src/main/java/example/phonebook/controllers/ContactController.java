package example.phonebook.controllers;


import example.phonebook.data.entity.Contact;
import example.phonebook.service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static example.phonebook.constants.Constants.VERSION;
import static example.phonebook.validation.Validation.requireNull;

@RestController
@RequestMapping(VERSION + "phonebooks/contacts")
public class ContactController {

    @Autowired
    ContactService contactService;

    /**
     * Возвращает один контакт
     *
     * @param id - ИД контакта
     * @return возвращает контакт
     */
    @GetMapping(value = "{id}")
    public ResponseEntity<Contact> getOne(@PathVariable("id") Long id) {
        Contact contact = contactService.get(id);
        if (requireNull(contact)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(contact, HttpStatus.OK);
        }
    }

    /**
     * Возвращает контакт по номеру телефона
     *
     * @param number номер телефона
     * @return возвращает контакт
     */
    @GetMapping(value = "/number/{number}")
    public ResponseEntity<Contact> getContactByNumber(@PathVariable("number") String number) {
        Contact contact = contactService.getContactByNumber(number);
        if (requireNull(contact)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(contact, HttpStatus.OK);
        }
    }

    /**
     * Создание нового контакта
     *
     * @param contact контакт
     */
    @PostMapping
    public ResponseEntity<Contact> create(@Valid @RequestBody Contact contact) {
        if (requireNull(contact)) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            contactService.create(contact);
            return new ResponseEntity<>(contact, HttpStatus.CREATED);
        }
    }

    /**
     * Обновление контакта
     *
     * @param id - ИД контакта
     * @param contact контакт
     */
    @PutMapping(value = "{id}")
    public ResponseEntity<Contact> update(@PathVariable("id") Long id, @RequestBody Contact contact) {
        if (requireNull(contact)) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        } else {
            contactService.update(id, contact);
            return new ResponseEntity(contact, HttpStatus.OK);
        }
    }

    /**
     * Удаление контакта
     *
     * @param id - ИД контакта
     */
    @DeleteMapping(value = "{id}")
    public ResponseEntity<Contact> delete(@PathVariable("id") Long id) {
        if(!contactService.isExist(id)){
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        } else {
            contactService.delete(id);
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }
    }
}
