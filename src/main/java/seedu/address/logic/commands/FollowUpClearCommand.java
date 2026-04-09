package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.Optional;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;

/**
 * Clears the follow-up date of an existing person in the address book.
 */
public class FollowUpClearCommand extends Command {

    public static final String COMMAND_WORD = "followupclear";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Clears the follow-up date of the person identified by the index number used "
            + "in the displayed person list.\n"
            + "Parameters: INDEX\n"
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_CLEAR_FOLLOW_UP_SUCCESS =
            "Cleared follow up date for: %1$s";

    private final Index targetIndex;

    /**
     * Creates a FollowUpClearCommand to clear the follow-up date of the person at the specified {@code targetIndex}.
     */
    public FollowUpClearCommand(Index targetIndex) {
        requireNonNull(targetIndex);
        this.targetIndex = targetIndex;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        List<Person> lastShownList = model.getFilteredPersonList();

        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_OOR_INDEX);
        }

        Person personToEdit = lastShownList.get(targetIndex.getZeroBased());

        Person editedPerson = new Person(
                personToEdit.getName(),
                personToEdit.getPhone(),
                personToEdit.getEmail(),
                personToEdit.getAddress(),
                personToEdit.getTags(),
                Optional.empty(),
                personToEdit.getNotes(),
                personToEdit.getCircle()
        );

        model.setPerson(personToEdit, editedPerson);

        return new CommandResult(
                String.format(MESSAGE_CLEAR_FOLLOW_UP_SUCCESS, Messages.format(editedPerson)));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof FollowUpClearCommand)) {
            return false;
        }

        FollowUpClearCommand otherCommand = (FollowUpClearCommand) other;
        return targetIndex.equals(otherCommand.targetIndex);
    }

    @Override
    public String toString() {
        return FollowUpClearCommand.class.getCanonicalName()
                + "{targetIndex=" + targetIndex + "}";
    }
}
