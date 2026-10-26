import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Modifier;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

public class ContactTest {
    private Contact validContact() {
        return new Contact("ID1", "Jane", "Smith", "0123456789", "123 Main Street");
    }

    @Test
    void constructorStoresAllFields() {
        Contact contact = validContact();
        assertEquals("ID1", contact.getContactId());
        assertEquals("Jane", contact.getFirstName());
        assertEquals("Smith", contact.getLastName());
        assertEquals("0123456789", contact.getPhone());
        assertEquals("123 Main Street", contact.getAddress());
    }

    @Test
    void acceptsMaximumFieldLengths() {
        Contact contact = new Contact("1234567890", "ABCDEFGHIJ", "KLMNOPQRST",
                "0123456789", "123456789012345678901234567890");
        assertEquals("1234567890", contact.getContactId());
        assertEquals("ABCDEFGHIJ", contact.getFirstName());
        assertEquals("KLMNOPQRST", contact.getLastName());
        assertEquals("123456789012345678901234567890", contact.getAddress());
    }

    @Test
    void acceptsEmptyStringsWhereOnlyNullAndMaximumLengthAreRestricted() {
        // The rubric does not specify minimum lengths for these four fields.
        Contact contact = new Contact("", "", "", "0123456789", "");
        assertEquals("", contact.getContactId());
        assertEquals("", contact.getFirstName());
        assertEquals("", contact.getLastName());
        assertEquals("", contact.getAddress());
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"12345678901"})
    void rejectsInvalidContactId(String value) {
        assertThrows(IllegalArgumentException.class,
                () -> new Contact(value, "Jane", "Smith", "0123456789", "123 Main Street"));
    }

    @Test
    void idIsPrivateFinalAndHasNoPublicSetter() throws Exception {
        int modifiers = Contact.class.getDeclaredField("contactId").getModifiers();
        assertTrue(Modifier.isPrivate(modifiers));
        assertTrue(Modifier.isFinal(modifiers));
        for (java.lang.reflect.Method method : Contact.class.getMethods()) {
            assertFalse(method.getName().equalsIgnoreCase("setContactId"));
        }
    }

    @Test
    void validUpdatesPreserveId() {
        Contact contact = validContact();
        contact.setFirstName("ABCDEFGHIJ");
        contact.setLastName("KLMNOPQRST");
        contact.setPhone("9876543210");
        contact.setAddress("123456789012345678901234567890");
        assertEquals("ID1", contact.getContactId());
        assertEquals("ABCDEFGHIJ", contact.getFirstName());
        assertEquals("KLMNOPQRST", contact.getLastName());
        assertEquals("9876543210", contact.getPhone());
        assertEquals("123456789012345678901234567890", contact.getAddress());
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"12345678901"})
    void rejectsInvalidFirstNameInConstructor(String value) {
        assertThrows(IllegalArgumentException.class,
                () -> new Contact("ID1", value, "Smith", "0123456789", "123 Main Street"));
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"12345678901"})
    void rejectsInvalidFirstNameUpdateWithoutChangingValue(String value) {
        Contact contact = validContact();
        assertThrows(IllegalArgumentException.class, () -> contact.setFirstName(value));
        assertEquals("Jane", contact.getFirstName());
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"12345678901"})
    void rejectsInvalidLastNameInConstructor(String value) {
        assertThrows(IllegalArgumentException.class,
                () -> new Contact("ID1", "Jane", value, "0123456789", "123 Main Street"));
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"12345678901"})
    void rejectsInvalidLastNameUpdateWithoutChangingValue(String value) {
        Contact contact = validContact();
        assertThrows(IllegalArgumentException.class, () -> contact.setLastName(value));
        assertEquals("Smith", contact.getLastName());
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"", "123456789", "12345678901", "12345a7890", "123-456789", "12345 7890", "１２３４５６７８９０"})
    void rejectsInvalidPhoneInConstructor(String value) {
        assertThrows(IllegalArgumentException.class,
                () -> new Contact("ID1", "Jane", "Smith", value, "123 Main Street"));
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"", "123456789", "12345678901", "12345a7890", "123-456789", "12345 7890", "１２３４５６７８９０"})
    void rejectsInvalidPhoneUpdateWithoutChangingValue(String value) {
        Contact contact = validContact();
        assertThrows(IllegalArgumentException.class, () -> contact.setPhone(value));
        assertEquals("0123456789", contact.getPhone());
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"1234567890123456789012345678901"})
    void rejectsInvalidAddressInConstructor(String value) {
        assertThrows(IllegalArgumentException.class,
                () -> new Contact("ID1", "Jane", "Smith", "0123456789", value));
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"1234567890123456789012345678901"})
    void rejectsInvalidAddressUpdateWithoutChangingValue(String value) {
        Contact contact = validContact();
        assertThrows(IllegalArgumentException.class, () -> contact.setAddress(value));
        assertEquals("123 Main Street", contact.getAddress());
    }
}
