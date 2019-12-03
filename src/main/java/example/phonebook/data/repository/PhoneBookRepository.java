package example.phonebook.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import example.phonebook.data.entity.PhoneBook;

import java.util.List;
import java.util.Optional;

public interface PhoneBookRepository extends JpaRepository<PhoneBook, Long> {

    List<PhoneBook> findAll();

    Optional<PhoneBook> findById(Long id);

    void deleteById(Long id);

    PhoneBook findPhoneBookByOwner_Id(Long id);

}
