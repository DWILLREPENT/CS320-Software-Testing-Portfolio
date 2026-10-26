/**
 * A contact with an immutable ID and validated, editable contact details.
 */
public class Contact {
    private final String contactId;
    private String firstName;
    private String lastName;
    private String phone;
    private String address;

    public Contact(String contactId, String firstName, String lastName,
                   String phone, String address) {
        validateLength(contactId, 10, "Contact ID");
        this.contactId = contactId;
        setFirstName(firstName);
        setLastName(lastName);
        setPhone(phone);
        setAddress(address);
    }

    private static void validateLength(String value, int maximum, String field) {
        if (value == null || value.length() > maximum) {
            throw new IllegalArgumentException(
                    field + " must not be null or exceed " + maximum + " characters.");
        }
    }

    public String getContactId() {
        return contactId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        validateLength(firstName, 10, "First name");
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        validateLength(lastName, 10, "Last name");
        this.lastName = lastName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        // Keep the number as text so leading zeroes are preserved.
        if (phone == null || !phone.matches("[0-9]{10}")) {
            throw new IllegalArgumentException("Phone must contain exactly 10 digits.");
        }
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        validateLength(address, 30, "Address");
        this.address = address;
    }
}
