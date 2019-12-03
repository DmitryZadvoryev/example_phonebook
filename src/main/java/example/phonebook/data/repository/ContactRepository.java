package example.phonebook.data.repository;

import example.phonebook.data.entity.Contact;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContactRepository extends JpaRepository<Contact, Long> {

    Contact findContactById(Long id);

    Contact findContactByNumber(String number);

    void deleteById(Long id);

    boolean existsContactById(Long id);

}
