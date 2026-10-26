import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

public class ContactServiceTest {
    private ContactService service;

    @BeforeEach
    void setUp() {
        service = new ContactService();
        service.addContact(new Contact("ID1", "Jane", "Smith", "0123456789", "123 Main Street"));
        service.addContact(new Contact("ID2", "Alex", "Jones", "9876543210", "456 Oak Street"));
    }

    @Test
    void addsContactsWithDistinctIds() {
        assertEquals("Jane", service.getContact("ID1").getFirstName());
        assertEquals("Alex", service.getContact("ID2").getFirstName());
    }

    @Test
    void duplicateIdDoesNotReplaceExistingContact() {
        Contact duplicate = new Contact(new String("ID1"), "Other", "Person",
                "1112223333", "789 Pine Street");
        assertThrows(IllegalArgumentException.class, () -> service.addContact(duplicate));
        assertEquals("Jane", service.getContact("ID1").getFirstName());
        assertEquals("Smith", service.getContact("ID1").getLastName());
        assertEquals("0123456789", service.getContact("ID1").getPhone());
        assertEquals("123 Main Street", service.getContact("ID1").getAddress());
    }

    @Test
    void rejectsNullContact() {
        assertThrows(IllegalArgumentException.class, () -> service.addContact(null));
    }

    @Test
    void deletesOnlyRequestedContact() {
        service.deleteContact(new String("ID1"));
        assertThrows(IllegalArgumentException.class, () -> service.getContact("ID1"));
        assertEquals("Alex", service.getContact("ID2").getFirstName());
    }

    @Test
    void deletedIdCanBeReused() {
        service.deleteContact("ID1");
        service.addContact(new Contact("ID1", "New", "Person", "1112223333", "New Address"));
        assertEquals("New", service.getContact("ID1").getFirstName());
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"missing"})
    void rejectsMissingIdsForRetrievalDeletionAndEveryUpdate(String id) {
        assertThrows(IllegalArgumentException.class, () -> service.getContact(id));
        assertThrows(IllegalArgumentException.class, () -> service.deleteContact(id));
        assertThrows(IllegalArgumentException.class, () -> service.updateFirstName(id, "New"));
        assertThrows(IllegalArgumentException.class, () -> service.updateLastName(id, "New"));
        assertThrows(IllegalArgumentException.class, () -> service.updatePhone(id, "1112223333"));
        assertThrows(IllegalArgumentException.class, () -> service.updateAddress(id, "New Address"));
        assertEquals("Jane", service.getContact("ID1").getFirstName());
        assertEquals("Alex", service.getContact("ID2").getFirstName());
    }

    @Test
    void serviceInstancesHaveIndependentStorage() {
        ContactService otherService = new ContactService();
        assertThrows(IllegalArgumentException.class, () -> otherService.getContact("ID1"));
        otherService.addContact(new Contact("ID1", "Other", "Person", "1112223333", "Other Address"));
        assertEquals("Jane", service.getContact("ID1").getFirstName());
    }

    @Test
    void updatesFirstNameOnlyOnRequestedContact() {
        service.updateFirstName(new String("ID1"), "ABCDEFGHIJ");
        Contact updated = service.getContact("ID1");
        assertEquals("ID1", updated.getContactId());
        assertEquals("ABCDEFGHIJ", updated.getFirstName());
        assertEquals("Smith", updated.getLastName());
        assertEquals("0123456789", updated.getPhone());
        assertEquals("123 Main Street", updated.getAddress());
        assertSecondContactUnchanged();
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"12345678901"})
    void rejectsInvalidFirstNameWithoutChangingStoredContact(String value) {
        assertThrows(IllegalArgumentException.class,
                () -> service.updateFirstName("ID1", value));
        assertEquals("Jane", service.getContact("ID1").getFirstName());
        assertSecondContactUnchanged();
    }

    @Test
    void updatesLastNameOnlyOnRequestedContact() {
        service.updateLastName(new String("ID1"), "KLMNOPQRST");
        Contact updated = service.getContact("ID1");
        assertEquals("ID1", updated.getContactId());
        assertEquals("Jane", updated.getFirstName());
        assertEquals("KLMNOPQRST", updated.getLastName());
        assertEquals("0123456789", updated.getPhone());
        assertEquals("123 Main Street", updated.getAddress());
        assertSecondContactUnchanged();
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"12345678901"})
    void rejectsInvalidLastNameWithoutChangingStoredContact(String value) {
        assertThrows(IllegalArgumentException.class,
                () -> service.updateLastName("ID1", value));
        assertEquals("Smith", service.getContact("ID1").getLastName());
        assertSecondContactUnchanged();
    }

    @Test
    void updatesPhoneOnlyOnRequestedContact() {
        service.updatePhone(new String("ID1"), "1112223333");
        Contact updated = service.getContact("ID1");
        assertEquals("ID1", updated.getContactId());
        assertEquals("Jane", updated.getFirstName());
        assertEquals("Smith", updated.getLastName());
        assertEquals("1112223333", updated.getPhone());
        assertEquals("123 Main Street", updated.getAddress());
        assertSecondContactUnchanged();
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"", "123456789", "12345678901", "12345a7890", "123-456789", "12345 7890", "１２３４５６７８９０"})
    void rejectsInvalidPhoneWithoutChangingStoredContact(String value) {
        assertThrows(IllegalArgumentException.class,
                () -> service.updatePhone("ID1", value));
        assertEquals("0123456789", service.getContact("ID1").getPhone());
        assertSecondContactUnchanged();
    }

    @Test
    void updatesAddressOnlyOnRequestedContact() {
        service.updateAddress(new String("ID1"), "123456789012345678901234567890");
        Contact updated = service.getContact("ID1");
        assertEquals("ID1", updated.getContactId());
        assertEquals("Jane", updated.getFirstName());
        assertEquals("Smith", updated.getLastName());
        assertEquals("0123456789", updated.getPhone());
        assertEquals("123456789012345678901234567890", updated.getAddress());
        assertSecondContactUnchanged();
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"1234567890123456789012345678901"})
    void rejectsInvalidAddressWithoutChangingStoredContact(String value) {
        assertThrows(IllegalArgumentException.class,
                () -> service.updateAddress("ID1", value));
        assertEquals("123 Main Street", service.getContact("ID1").getAddress());
        assertSecondContactUnchanged();
    }

    private void assertSecondContactUnchanged() {
        Contact second = service.getContact("ID2");
        assertEquals("ID2", second.getContactId());
        assertEquals("Alex", second.getFirstName());
        assertEquals("Jones", second.getLastName());
        assertEquals("9876543210", second.getPhone());
        assertEquals("456 Oak Street", second.getAddress());
    }
}
