package example.phonebook.data.repository;

import example.phonebook.data.entity.Contact;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContactRepository extends JpaRepository<Contact, Long> {

    void deleteById(Long aLong);

    Contact findContactById(Long id);

    Contact findContactByNumber(String number);

    boolean existsContactById(Long id);
}
