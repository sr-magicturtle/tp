package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NOTE;

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
            + "Parameters: INDEX " + PREFIX_NOTE + "NOTE (max 200 words)\n"
            + "Example: " + COMMAND_WORD + " 1 " + PREFIX_NOTE + "Met at career fair";

    public static final String MESSAGE_ADD_NOTE_SUCCESS = "Added note to Person: %1$s";
    public static final String MESSAGE_INVALID_PERSON = "The person index provided is invalid.";
    public static final int MAX_WORD_COUNT = 200;
    public static final String MESSAGE_WORD_LIMIT_EXCEEDED =
            "Note exceeds the maximum word count of " + MAX_WORD_COUNT + " words.";

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
            throw new CommandException(MESSAGE_INVALID_PERSON);
        }
        if (note.toString().trim().split("\\s+").length > 200) {
            throw new CommandException(MESSAGE_WORD_LIMIT_EXCEEDED);
        }

        Person personToEdit = lastShownList.get(index.getZeroBased());
        String existingValue = personToEdit.getNotes().map(Note::toString).orElse("");
        String updatedValue = existingValue.isEmpty()
                ? note.toString().trim()
                : existingValue + "\n" + note.toString().trim();
        if (updatedValue.split("\\s+").length > Note.MAX_WORD_COUNT) {
            throw new CommandException(Note.MESSAGE_WORD_LIMIT_EXCEEDED);
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
