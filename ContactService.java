import java.util.HashMap;
import java.util.Map;

/**
 * Stores contacts in memory, using each contact's unique ID as the key.
 * Duplicate IDs and operations on missing IDs are rejected.
 */
public class ContactService {
    private final Map<String, Contact> contacts = new HashMap<>();

    public void addContact(Contact contact) {
        if (contact == null) {
            throw new IllegalArgumentException("Contact must not be null.");
        }
        String contactId = contact.getContactId();
        if (contacts.containsKey(contactId)) {
            throw new IllegalArgumentException("Contact ID already exists.");
        }
        contacts.put(contactId, contact);
    }

    public Contact getContact(String contactId) {
        Contact contact = contacts.get(contactId);
        if (contact == null) {
            throw new IllegalArgumentException("Contact ID does not exist.");
        }
        return contact;
    }

    public void deleteContact(String contactId) {
        getContact(contactId);
        contacts.remove(contactId);
    }

    public void updateFirstName(String contactId, String firstName) {
        getContact(contactId).setFirstName(firstName);
    }

    public void updateLastName(String contactId, String lastName) {
        getContact(contactId).setLastName(lastName);
    }

    public void updatePhone(String contactId, String phone) {
        getContact(contactId).setPhone(phone);
    }

    public void updateAddress(String contactId, String address) {
        getContact(contactId).setAddress(address);
    }
}
