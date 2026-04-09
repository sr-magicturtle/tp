package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

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
    public static final String MESSAGE_PAST_DATE_WARNING =
            "Warning: follow up date is before today.";
    public static final String MESSAGE_FAR_FUTURE_WARNING =
            "Warning: follow up date is more than 5 years from today.";

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

        Person editedPerson = new Person(
                personToEdit.getName(),
                personToEdit.getPhone(),
                personToEdit.getEmail(),
                personToEdit.getAddress(),
                personToEdit.getTags(),
                Optional.of(followUpDate),
                personToEdit.getNotes(),
                personToEdit.getCircle()
        );

        model.setPerson(personToEdit, editedPerson);

        StringBuilder feedback = new StringBuilder(
                String.format(MESSAGE_SET_FOLLOW_UP_SUCCESS, Messages.format(editedPerson)));

        LocalDate today = LocalDate.now();
        LocalDate date = followUpDate.value;

        if (date.isBefore(today)) {
            feedback.append("\n").append(MESSAGE_PAST_DATE_WARNING);
        }

        if (date.isAfter(today.plusYears(5))) {
            feedback.append("\n").append(MESSAGE_FAR_FUTURE_WARNING);
        }

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
