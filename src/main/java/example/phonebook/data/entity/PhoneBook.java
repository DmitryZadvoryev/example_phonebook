package example.phonebook.data.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "phonebooks")
public class PhoneBook implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne(orphanRemoval = true, mappedBy = "phoneBook")
    private User owner;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true, mappedBy = "phoneBook")
    private List<Contact> contacts;

    public PhoneBook() {
    }

    public PhoneBook(User owner, List<Contact> contacts) {
        this.owner = owner;
        this.contacts = contacts;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public List<Contact> getContacts() {
        return contacts;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof PhoneBook)) return false;
        PhoneBook phoneBook = (PhoneBook) object;
        return Objects.equals(id, phoneBook.id) &&
                Objects.equals(owner, phoneBook.owner) &&
                Objects.equals(contacts, phoneBook.contacts);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, owner, contacts);
    }

}
