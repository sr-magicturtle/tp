package seedu.address.model.circle;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

import java.util.Set;

/**
 * Represents a Circle in the address book.
 * Guarantees: immutable; Circle is valid as declared in {@link #isValidCircleName(String)}
 */
public class Circle {

    public static final String MESSAGE_CONSTRAINTS = "Circle names are strictly limited to the following: "
            + "client, prospect or friend (case-insensitive).";
    private static final Set<String> ALLOWED_CIRCLES = Set.of("client", "prospect", "friend");

    private final String circleName;

    /**
     * Constructs a {@code Circle}.
     *
     * @param circleName A valid circle name.
     */
    public Circle(String circleName) {
        requireNonNull(circleName);
        checkArgument(isValidCircleName(circleName), MESSAGE_CONSTRAINTS);
        this.circleName = circleName.trim().toLowerCase();
    }

    public String getCircleName() {
        return circleName;
    }

    /**
     * Returns true if a given string is a valid circle name.
     */
    public static boolean isValidCircleName(String test) {
        requireNonNull(test);
        String normalised = test.trim().toLowerCase();
        return ALLOWED_CIRCLES.contains(normalised);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof Circle)) {
            return false;
        }

        Circle otherCircle = (Circle) other;
        return circleName.equals(otherCircle.circleName);
    }

    @Override
    public int hashCode() {
        return circleName.hashCode();
    }

    /**
     * Format state as text for viewing.
     */
    @Override
    public String toString() {
        return '[' + circleName + ']';
    }

}
