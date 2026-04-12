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
            "Follow up date should be in YYYY-MM-DD format.";

    public static final String MESSAGE_INVALID_DATE =
            "Follow up date is not a valid calendar date.";

    public static final String MESSAGE_PAST_DATE_WARNING =
            "Warning: follow up date is before today.";
    public static final String MESSAGE_FAR_FUTURE_WARNING =
            "Warning: follow up date is more than 5 years from today.";

    public final LocalDate value;

    /**
     * Constructs a {@code FollowUpDate}.
     *
     * @param date A valid follow-up date string in YYYY-MM-DD format.
     */
    public FollowUpDate(String date) {
        requireNonNull(date);
        checkArgument(isValidFollowUpDate(date), MESSAGE_CONSTRAINTS);
        value = LocalDate.parse(date);
    }

    /**
     * Returns true if a given string matches the YYYY-MM-DD format pattern
     * (without checking whether the date actually exists on the calendar).
     */
    public static boolean isValidDateFormat(String test) {
        return test.matches("^\\d{4}-\\d{2}-\\d{2}$");
    }

    /**
     * Returns true if a given string is a valid follow-up date in YYYY-MM-DD format.
     */
    public static boolean isValidFollowUpDate(String test) {
        try {
            LocalDate.parse(test);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    public String getWarnings() {
        LocalDate today = LocalDate.now();
        StringBuilder warnings = new StringBuilder();

        if (value.isBefore(today)) {
            warnings.append("\n").append(MESSAGE_PAST_DATE_WARNING);
        }
        if (value.isAfter(today.plusYears(5))) {
            warnings.append("\n").append(MESSAGE_FAR_FUTURE_WARNING);
        }

        return warnings.toString();
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

        FollowUpDate otherDate = (FollowUpDate) other;
        return value.equals(otherDate.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
