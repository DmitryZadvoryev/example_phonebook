package example.phonebook.data.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "users")
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String name;

    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "phone_book_id", nullable = false)
    PhoneBook phoneBook;

    public User() {
    }

    public User(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PhoneBook getPhoneBook() {
        return phoneBook;
    }

    public void setPhoneBook(PhoneBook phoneBook) {
        this.phoneBook = phoneBook;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof PhoneBook)) return false;
        User user = (User) object;
        return Objects.equals(id, user.id) &&
                Objects.equals(name, user.name) &&
                Objects.equals(phoneBook, user.phoneBook);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, phoneBook);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", phoneBook=" + phoneBook +
                '}';
    }
}
