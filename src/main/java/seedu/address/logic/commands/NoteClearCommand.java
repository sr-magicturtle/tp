package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.Optional;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Note;
import seedu.address.model.person.Person;

/**
 * Clears the note of an existing person in the address book.
 * Usage: noteclear INDEX
 */
public class NoteClearCommand extends Command {

    public static final String COMMAND_WORD = "noteclear";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Clears the note of the person identified by the index number.\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_CLEAR_NOTE_SUCCESS = "Cleared note for Person: %1$s";
    public static final String MESSAGE_INVALID_PERSON = "The person index provided is invalid.";

    private final Index index;

    /**
     * Creates a NoteClearCommand to clear the note of the person at the specified {@code index}.
     * @param index index of person whose note is to be cleared.
     */
    public NoteClearCommand(Index index) {
        requireNonNull(index);
        this.index = index;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();
        if (lastShownList == null) {
            throw new CommandException("Failed to retrieve person list.");
        }
        if (lastShownList.isEmpty()) {
            throw new CommandException("No persons available to clear notes from.");
        }
        if (index.getZeroBased() < 0 || index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(MESSAGE_INVALID_PERSON);
        }

        Person personToEdit = lastShownList.get(index.getZeroBased());
        if (personToEdit == null) {
            throw new CommandException("Retrieved person is null.");
        }

        Person editedPerson = new Person(
                personToEdit.getName(),
                personToEdit.getPhone(),
                personToEdit.getEmail(),
                personToEdit.getAddress(),
                personToEdit.getTags(),
                personToEdit.getFollowUpDate(),
                Optional.<Note>empty(),
                personToEdit.getCircle()
        );

        model.setPerson(personToEdit, editedPerson);

        boolean hadNote = personToEdit.getNotes().isPresent();
        String message = hadNote
                ? String.format(MESSAGE_CLEAR_NOTE_SUCCESS, Messages.format(editedPerson))
                : "No note to clear for " + Messages.format(editedPerson);
        return new CommandResult(message);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof NoteClearCommand)) {
            return false;
        }
        NoteClearCommand o = (NoteClearCommand) other;
        return index.equals(o.index);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("index", index)
                .toString();
    }
}
