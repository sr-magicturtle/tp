package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.FollowUpDate;
import seedu.address.model.person.Person;

/**
 * Sets a follow-up date for an existing person in the address book.
 */
public class SetFollowUpCommand extends Command {

    public static final String COMMAND_WORD = "followup";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Sets a follow-up date for the person identified by the index number used "
            + "in the displayed person list.\n"
            + "Parameters: INDEX d/FOLLOW_UP_DATE\n"
            + "Example: " + COMMAND_WORD + " 1 d/2026-12-31";

    public static final String MESSAGE_SET_FOLLOW_UP_SUCCESS = "Set follow up date for: %1$s";

    public static final String MESSAGE_FOLLOW_UP_UNCHANGED =
            "Follow-up date unchanged: the date is already set to the same value.";

    private final Index targetIndex;
    private final FollowUpDate followUpDate;

    /**
     * Creates a SetFollowUpCommand to set the {@code followUpDate} for the person at the specified {@code targetIndex}.
     */
    public SetFollowUpCommand(Index targetIndex, FollowUpDate followUpDate) {
        requireNonNull(targetIndex);
        requireNonNull(followUpDate);
        this.targetIndex = targetIndex;
        this.followUpDate = followUpDate;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        List<Person> lastShownList = model.getFilteredPersonList();

        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_OOR_INDEX);
        }

        Person personToEdit = lastShownList.get(targetIndex.getZeroBased());

        if (personToEdit.getFollowUpDate().isPresent()
                && personToEdit.getFollowUpDate().get().equals(followUpDate)) {
            throw new CommandException(MESSAGE_FOLLOW_UP_UNCHANGED);
        }

        Person editedPerson = personToEdit.setFollowUpDate(followUpDate);

        model.setPerson(personToEdit, editedPerson);

        StringBuilder feedback = new StringBuilder(
                String.format(MESSAGE_SET_FOLLOW_UP_SUCCESS, Messages.format(editedPerson)));

        feedback.append(followUpDate.getWarnings());

        return new CommandResult(feedback.toString());
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof SetFollowUpCommand)) {
            return false;
        }

        SetFollowUpCommand otherCommand = (SetFollowUpCommand) other;
        return targetIndex.equals(otherCommand.targetIndex)
                && followUpDate.equals(otherCommand.followUpDate);
    }

    @Override
    public String toString() {
        return SetFollowUpCommand.class.getCanonicalName()
                + "{targetIndex=" + targetIndex + ", followUpDate=" + followUpDate + "}";
    }
}
