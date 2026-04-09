package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.Optional;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;

/**
 * Removes the circle from a contact in the address book.
 * The contact is identified by its index in the filtered person list.
 */
public class CircleRemoveCommand extends Command {

    public static final String COMMAND_WORD = "circlerm";

    public static final String MESSAGE_USAGE = COMMAND_WORD
        + ": Removes the circle from a contact in the address book.\n"
        + "Parameters: INDEX\n"
        + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_CIRCLE_PERSON_SUCCESS = "Removed circle from %1$s";
    public static final String MESSAGE_CIRCLE_PERSON_FAILURE = "Remove failed: contact does not have a circle set.";
    public static final String MESSAGE_INVALID_PERSON = "The person does not exist in the address book.";

    private final Index index;

    /**
     * Constructs a CircleRemoveCommand to remove a circle from a contact.
     *
     * @param index the index of the contact in the filtered person list
     */
    public CircleRemoveCommand(Index index) {
        requireNonNull(index);
        this.index = index;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_OOR_INDEX);
        }

        Person personAtIndex = lastShownList.get(index.getZeroBased());
        if (personAtIndex.getCircle().isEmpty()) {
            throw new CommandException(MESSAGE_CIRCLE_PERSON_FAILURE);
        }

        Person editedPerson = new Person(
            personAtIndex.getName(),
            personAtIndex.getPhone(),
            personAtIndex.getEmail(),
            personAtIndex.getAddress(),
            personAtIndex.getTags(),
            personAtIndex.getFollowUpDate(),
            personAtIndex.getNotes(),
            Optional.empty()
        );

        model.setPerson(personAtIndex, editedPerson);
        return new CommandResult(String.format(MESSAGE_CIRCLE_PERSON_SUCCESS, editedPerson.getName()));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof CircleRemoveCommand)) {
            return false;
        }

        CircleRemoveCommand otherCircleRemoveCommand = (CircleRemoveCommand) other;
        return index.equals(otherCircleRemoveCommand.index);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
            .add("index", index)
            .toString();
    }
}
