package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

/**
 * Represents a Person's follow-up date in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidFollowUpDate(String)}
 */
public class FollowUpDate {

    public static final String MESSAGE_CONSTRAINTS =
            "Follow up date should be in YYYY-MM-DD format and be today or later.";

    public final LocalDate value;

    /**
     * Constructs a {@code FollowUpDate}.
     *
     * @param date A valid follow-up date.
     */
    public FollowUpDate(String date) {
        requireNonNull(date);
        checkArgument(isValidFollowUpDate(date), MESSAGE_CONSTRAINTS);
        value = LocalDate.parse(date);
    }

    /**
     * Returns true if a given string is a valid follow-up date.
     */
    public static boolean isValidFollowUpDate(String test) {
        requireNonNull(test);
        try {
            LocalDate.parse(test);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    @Override
    public String toString() {
        return value.toString();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof FollowUpDate)) {
            return false;
        }

        FollowUpDate otherFollowUpDate = (FollowUpDate) other;
        return value.equals(otherFollowUpDate.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
