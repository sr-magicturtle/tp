package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NOTE;
import static seedu.address.model.person.Note.MAX_CHAR_COUNT;
import static seedu.address.model.person.Note.MESSAGE_CHAR_LIMIT_EXCEEDED;

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
 * Adds or replaces a note for an existing person in the address book.
 */
public class NoteAddCommand extends Command {

    public static final String COMMAND_WORD = "note";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Adds a note to the person identified by the index number used in the displayed person list.\n"
            + "Parameters: INDEX " + PREFIX_NOTE + "NOTE (max 1000 characters)\n"
            + "Example: " + COMMAND_WORD + " 1 " + PREFIX_NOTE + "Looking for family coverage";

    public static final String MESSAGE_ADD_NOTE_SUCCESS = "Added note to Person: %1$s";

    private final Index index;
    private final Note note;

    /**
     * @param index of the person in the filtered person list
     * @param note  text to set as the person's note
     */
    public NoteAddCommand(Index index, Note note) {
        requireNonNull(index);
        requireNonNull(note);
        this.index = index;
        this.note = note;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();
        if (lastShownList == null) {
            throw new CommandException("Failed to retrieve person list.");
        }

        if (index.getZeroBased() < 0 || index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_OOR_INDEX);
        }

        if (note.charCount() > MAX_CHAR_COUNT) {
            throw new CommandException(MESSAGE_CHAR_LIMIT_EXCEEDED);
        }

        Person personToEdit = lastShownList.get(index.getZeroBased());
        String existingValue = personToEdit.getNotes().map(Note::toString).orElse("");
        String updatedValue = existingValue.isEmpty()
                ? note.toString().trim()
                : existingValue + " | " + note.toString().trim();

        if (updatedValue.length() > MAX_CHAR_COUNT) {
            throw new CommandException(MESSAGE_CHAR_LIMIT_EXCEEDED);
        }

        Person editedPerson = new Person(
                personToEdit.getName(),
                personToEdit.getPhone(),
                personToEdit.getEmail(),
                personToEdit.getAddress(),
                personToEdit.getTags(),
                personToEdit.getFollowUpDate(),
                Optional.of(new Note(updatedValue)),
                personToEdit.getCircle()
        );

        model.setPerson(personToEdit, editedPerson);

        return new CommandResult(String.format(MESSAGE_ADD_NOTE_SUCCESS, Messages.format(editedPerson)));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof NoteAddCommand)) {
            return false;
        }
        NoteAddCommand noteOther = (NoteAddCommand) other;
        return index.equals(noteOther.index) && note.equals(noteOther.note);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("index", index)
                .add("note", note)
                .toString();
    }
}
