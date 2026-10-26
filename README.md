# CS 320 Software Testing, Automation, and Quality Assurance

This repository holds my portfolio work from CS 320 at Southern New Hampshire University.

## Contents

**Project One: Contact Service**

* `Contact.java` creates a contact with a unique, unchangeable ID and validated fields for first name, last name, phone number, and address.
* `ContactService.java` stores contacts in memory and supports adding, deleting, and updating them by ID.
* `ContactTest.java` and `ContactServiceTest.java` are the JUnit 5 tests that verify every requirement for both classes.

**Project Two: Summary and Reflections Report**

* A report analyzing the testing approach I used across the contact, task, and appointment services, how well it lined up with the requirements, and what I learned about testing techniques and the mindset a tester needs.

## Reflection

### How can I ensure that my code, program, or software is functional and secure?

The biggest thing I learned is that you can't call code functional until you've tried to break it. For the contact service, I wrote tests for the normal cases, but most of the value came from the edge cases: a first name at exactly 10 characters and one at 11, a phone number with 9 digits or a letter in it, null values in every field, and a duplicate ID being added a second time. Testing right at the boundaries is where bugs tend to hide.

Security starts at the same place. Every field is validated in the setter before it's stored, and the constructor uses those same setters, so there's no path where bad data gets into a contact object. The contact ID is `private final` with no setter, and I wrote a test that checks this with reflection so the rule can't quietly break later. I also tested that a duplicate ID doesn't overwrite the existing contact and that updating one contact doesn't touch any others. Running the full suite after every change, and checking coverage, gave me confidence that a fix in one spot didn't break something else.

### How do I interpret user needs and incorporate them into a program?

I treat the requirements as the source of truth and turn each one into something I can test. "The contact ID shall not be longer than 10 characters, shall not be null, and shall not be updatable" became three separate checks. Writing the tests from the requirements, instead of from my own code, kept me honest about what the customer actually asked for versus what I assumed.

Some requirements leave room for interpretation, so I made those decisions on purpose. For example, the phone number is stored as a string instead of a number so leading zeroes aren't lost, and empty strings are allowed in name and address fields because the requirements only ruled out null values and lengths over the limit. When something is unclear, the right move is to decide deliberately, write it down, and test it, so anyone reading the code knows it was a choice and not an accident.

### How do I approach designing software?

I start by breaking the problem into small pieces with clear jobs. Here, `Contact` is responsible for keeping its own data valid, and `ContactService` is responsible for managing the collection. Keeping those responsibilities separate made each class easier to test on its own, and it's the same pattern I reused for the task and appointment services.

I've also started thinking about testability as part of the design instead of an afterthought. If a class is hard to test, it's usually doing too much. Using a `HashMap` keyed by ID made lookups simple and made rules like unique IDs easy to enforce in one place. Putting the validation in a single helper method instead of repeating it everywhere meant there was one spot to get right and one spot to test. Going forward, I want to keep designing in a way where the requirements, the code, and the tests all line up with each other.
