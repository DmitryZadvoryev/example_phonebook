package example.phonebook.data.repository;

import example.phonebook.data.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface UserRepository extends JpaRepository<User, Long> {

    List<User> findAll();

    User findUserById(Long id);

    List<User> findByNameStartingWithIgnoreCase(String name);

    void deleteById(Long id);

    boolean existsUserById(Long id);

}
