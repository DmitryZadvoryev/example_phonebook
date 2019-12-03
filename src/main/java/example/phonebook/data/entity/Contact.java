package example.phonebook.data.entity;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "contacts")
public class Contact implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    @Pattern(regexp = "^7\\d{10}$")
    private String number;

    @ManyToOne()
    PhoneBook phoneBook;

    public Contact() {}

    public Contact(String name, String number){
        this.name = name;
        this.number = number;
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

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }


    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Contact)) return false;
        Contact contact = (Contact) object;
        return id.equals(contact.id) &&
                name.equals(contact.name) &&
                number.equals(contact.number) &&
                phoneBook.equals(contact.phoneBook);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, number, phoneBook);
    }

    @Override
    public String toString() {
        return "Contact{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", number='" + number + '\'' +
                ", phoneBook=" + phoneBook +
                '}';
    }
}
