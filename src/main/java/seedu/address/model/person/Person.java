package seedu.address.model.person;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.circle.Circle;
import seedu.address.model.tag.Tag;

/**
 * Represents a Person in the address book.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class Person {

    // Identity fields
    private final Name name;
    private final Phone phone;
    private final Email email;

    // Data fields
    private final Address address;
    private final Set<Tag> tags = new HashSet<>();
    private final Optional<FollowUpDate> followUpDate;
    private final Optional<Note> notes;
    private final Optional<Circle> circle;

    /**
     * Every field must be present and not null.
     */
    public Person(Name name, Phone phone, Email email, Address address, Set<Tag> tags,
                  Optional<FollowUpDate> followUpDate,
                  Optional<Note> notes, Optional<Circle> circle) {
        requireAllNonNull(name, phone, email, address, tags);
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.tags.addAll(tags);
        this.followUpDate = Objects.requireNonNullElse(followUpDate, Optional.empty());
        this.notes = Objects.requireNonNullElse(notes, Optional.empty());
        this.circle = Objects.requireNonNullElse(circle, Optional.empty());
    }

    public Name getName() {
        return name;
    }

    public Phone getPhone() {
        return phone;
    }

    public Email getEmail() {
        return email;
    }

    public Address getAddress() {
        return address;
    }

    public Optional<FollowUpDate> getFollowUpDate() {
        return followUpDate;
    }

    public Optional<Note> getNotes() {
        return notes;
    }

    public Optional<Circle> getCircle() {
        return circle;
    }

    /**
     * Returns an immutable tag set, which throws {@code UnsupportedOperationException}
     * if modification is attempted.
     */
    public Set<Tag> getTags() {
        return Collections.unmodifiableSet(tags);
    }

    /**
     * Returns a new {@code Person} with the specified {@code tag} added.
     * <p>
     * This method does not modify the current {@code Person} object. Instead,
     * it creates a new {@code Person} instance with all existing fields unchanged
     * except for the {@code tags}, which includes the newly added {@code tag}.
     *
     * @param tag the {@code Tag} to add to the person's tags
     * @return a new {@code Person} instance with the added tag
     */
    public Person addTag(Tag tag) {
        Set<Tag> newTags = new HashSet<>(tags);
        newTags.add(tag);
        return new Person(name, phone, email, address, newTags, followUpDate, notes, circle);
    }

    /**
     * Returns a new {@code Person} with the specified {@code tag} removed.
     * <p>
     * This method does not modify the current {@code Person} object. Instead,
     * it creates a new {@code Person} instance with all existing fields unchanged
     * except for the {@code tags}, which excludes the specified {@code tag}.
     *
     * @param tag the {@code Tag} to remove from the person's tags
     * @return a new {@code Person} instance with the tag removed
     */
    public Person removeTag(Tag tag) {
        Set<Tag> newTags = new HashSet<>(tags);
        newTags.remove(tag);
        return new Person(name, phone, email, address, newTags, followUpDate, notes, circle);
    }

    public boolean hasTag(Tag tag) {
        return tags.contains(tag);
    }

    /**
     * Returns true if both persons have the same name.
     * This defines a weaker notion of equality between two persons.
     */
    public boolean isSamePerson(Person otherPerson) {
        if (otherPerson == this) {
            return true;
        }

        if (otherPerson == null) {
            return false;
        }
        //reject same email except placeholder email
        boolean samePhone = otherPerson.getPhone().equals(getPhone());
        boolean thisEmailIsPlaceholder = getEmail().toString().equals("missing@email.empty");
        boolean otherEmailIsPlaceholder = otherPerson.getEmail().toString().equals("missing@email.empty");
        boolean sameRealEmail = !thisEmailIsPlaceholder
                && !otherEmailIsPlaceholder
                && otherPerson.getEmail().toString().equalsIgnoreCase(getEmail().toString());

        return samePhone || sameRealEmail;
    }

    /**
     * Returns true if both persons have the same identity and data fields.
     * This defines a stronger notion of equality between two persons.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Person)) {
            return false;
        }

        Person otherPerson = (Person) other;
        return name.equals(otherPerson.name)
                && phone.equals(otherPerson.phone)
                && email.equals(otherPerson.email)
                && address.equals(otherPerson.address)
                && tags.equals(otherPerson.tags)
                && followUpDate.equals(otherPerson.followUpDate)
                && notes.equals(otherPerson.notes)
                && circle.equals(otherPerson.circle);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, phone, email, address, tags, followUpDate, notes, circle);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("name", name)
                .add("phone", phone)
                .add("email", email)
                .add("address", address)
                .add("tags", tags)
                .add("followUpDate", followUpDate)
                .add("notes", notes)
                .add("circle", circle)
                .toString();
    }

}
