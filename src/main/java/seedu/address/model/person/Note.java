package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents a Person's note in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidNote(String)}
 */
public class Note {

    public static final String MESSAGE_CONSTRAINTS =
            "Notes must not be blank.";
    public static final int MAX_CHAR_COUNT = 1000;
    public static final String MESSAGE_CHAR_LIMIT_EXCEEDED =
            "Note exceeds the maximum character count of " + MAX_CHAR_COUNT + " characters.";

    public final String value;

    /**
     * Constructs a {@code Note}.
     *
     * @param note A valid note string.
     */
    public Note(String note) {
        requireNonNull(note);
        checkArgument(isValidNote(note), MESSAGE_CONSTRAINTS);
        this.value = note;
    }

    /**
     * Returns true if a given string is non-blank && <= MAX_CHAR_COUNT.
     * @param test user input.
     */
    public static boolean isValidNote(String test) {
        return !test.trim().isEmpty() && test.length() <= MAX_CHAR_COUNT;
    }

    /**
     * Returns the number of characters in this note.
     */
    public int charCount() {
        return value.trim().length();
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof Note)) {
            return false;
        }
        Note otherNote = (Note) other;
        return value.equals(otherNote.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
