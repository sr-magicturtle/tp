package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;

/**
 * Filters the address book to show only contacts in a specific circle.
 * Keyword matching is case-insensitive.
 */
public class CircleFilterCommand extends Command {

    public static final String COMMAND_WORD = "circlefilter";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Shows all contacts in the given circle.\n"
        + "Parameters: CIRCLE_NAME\n"
        + "Example: " + COMMAND_WORD + " client";

    public static final String MESSAGE_SUCCESS = "Filtered contacts by circle '%1$s'";
    public static final String MESSAGE_NO_CONTACTS_FOUND = "No contacts found in the '%1$s' circle.";

    private final String circleName;

    /**
     * Constructs a CircleFilterCommand to filter contacts by circle.
     *
     * @param circleName the name of the circle to filter by
     */
    public CircleFilterCommand(String circleName) {
        requireNonNull(circleName);
        this.circleName = circleName.toLowerCase();
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        model.updateFilteredPersonList(person -> person.getCircle()
            .map(c -> c.getCircleName().equals(circleName))
            .orElse(false));

        List<Person> filteredList = model.getFilteredPersonList();
        if (filteredList.isEmpty()) {
            return new CommandResult(String.format(MESSAGE_NO_CONTACTS_FOUND, circleName));
        }

        return new CommandResult(String.format(MESSAGE_SUCCESS, circleName));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof CircleFilterCommand)) {
            return false;
        }

        CircleFilterCommand otherCommand = (CircleFilterCommand) other;
        return circleName.equals(otherCommand.circleName);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
            .add("circleName", circleName)
            .toString();
    }
}
