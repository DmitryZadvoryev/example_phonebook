package example.phonebook.data.repository;

import example.phonebook.data.entity.PhoneBook;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PhoneBookRepository extends JpaRepository<PhoneBook, Long> {

    List<PhoneBook> findAll();

    void deleteById(Long id);

    PhoneBook findPhoneBookByOwner_Id(Long id);

    PhoneBook findPhonebookById(long id);
}
