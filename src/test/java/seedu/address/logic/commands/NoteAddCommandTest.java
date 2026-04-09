package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.Optional;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Note;
import seedu.address.model.person.Person;

public class NoteAddCommandTest {

    private static final String NOTE_STUB = "Met at career fair";
    private static final String NOTE_STUB_2 = "Follow up next week";

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    @Test
    public void execute_validIndexUnfilteredList_success() {
        Person personToEdit = model.getFilteredPersonList().get(0);
        NoteAddCommand command = new NoteAddCommand(Index.fromOneBased(1), new Note(NOTE_STUB));

        String existingNote = personToEdit.getNotes().map(Note::toString).orElse("");
        String expectedNote = existingNote.isEmpty() ? NOTE_STUB : existingNote + " | " + NOTE_STUB;

        Person editedPerson = new Person(
                personToEdit.getName(), personToEdit.getPhone(), personToEdit.getEmail(),
                personToEdit.getAddress(), personToEdit.getTags(),
                personToEdit.getFollowUpDate(),
                Optional.of(new Note(expectedNote)),
                personToEdit.getCircle());

        String expectedMessage = String.format(NoteAddCommand.MESSAGE_ADD_NOTE_SUCCESS,
                Messages.format(editedPerson));

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.setPerson(personToEdit, editedPerson);

        assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexUnfilteredList_throwsCommandException() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        NoteAddCommand command = new NoteAddCommand(outOfBoundIndex, new Note(NOTE_STUB));
        assertCommandFailure(command, model, Messages.MESSAGE_OOR_INDEX);
    }

    @Test
    public void execute_appendsToExistingNote() throws Exception {
        Person original = model.getFilteredPersonList().get(0);
        Person withExistingNote = new Person(
                original.getName(), original.getPhone(), original.getEmail(),
                original.getAddress(), original.getTags(),
                original.getFollowUpDate(),
                Optional.of(new Note(NOTE_STUB)),
                original.getCircle());
        model.setPerson(original, withExistingNote);

        NoteAddCommand command = new NoteAddCommand(Index.fromOneBased(1), new Note(NOTE_STUB_2));
        command.execute(model);

        Person result = model.getFilteredPersonList().get(0);
        String expectedCombined = NOTE_STUB + " | " + NOTE_STUB_2;
        assertEquals(Optional.of(new Note(expectedCombined)), result.getNotes());
    }

    @Test
    public void execute_noExistingNote() throws Exception {
        Person original = model.getFilteredPersonList().get(0);
        Person withNoNote = new Person(
                original.getName(), original.getPhone(), original.getEmail(),
                original.getAddress(), original.getTags(),
                original.getFollowUpDate(),
                Optional.empty(),
                original.getCircle());
        model.setPerson(original, withNoNote);

        NoteAddCommand command = new NoteAddCommand(Index.fromOneBased(1), new Note(NOTE_STUB));
        command.execute(model);

        Person result = model.getFilteredPersonList().get(0);
        assertEquals(Optional.of(new Note(NOTE_STUB)), result.getNotes());
    }

    @Test
    public void execute_preservesFollowUpDateAndCircle() throws Exception {
        Person original = model.getFilteredPersonList().get(0);
        NoteAddCommand command = new NoteAddCommand(Index.fromOneBased(1), new Note(NOTE_STUB));
        command.execute(model);

        Person result = model.getFilteredPersonList().get(0);
        assertEquals(original.getFollowUpDate(), result.getFollowUpDate());
        assertEquals(original.getCircle(), result.getCircle());
    }

    @Test
    public void execute_wordLimitExceeded_throwsCommandException() {
        // Start a person with 500 characters in their note
        Person original = model.getFilteredPersonList().get(0);
        String existingNote = "w".repeat(500).trim();
        Person withExistingNote = new Person(
                original.getName(), original.getPhone(), original.getEmail(),
                original.getAddress(), original.getTags(),
                original.getFollowUpDate(),
                Optional.of(new Note(existingNote)),
                original.getCircle());
        model.setPerson(original, withExistingNote);

        // Adding 501 characters would push total to 1001 - should fail
        String newNote = "w".repeat(501).trim();
        NoteAddCommand command = new NoteAddCommand(Index.fromOneBased(1), new Note(newNote));

        assertThrows(CommandException.class, () -> command.execute(model));
    }
    @Test
    public void equals() {
        NoteAddCommand command1 = new NoteAddCommand(Index.fromOneBased(1), new Note(NOTE_STUB));
        NoteAddCommand command2 = new NoteAddCommand(Index.fromOneBased(1), new Note(NOTE_STUB));
        NoteAddCommand diffIndex = new NoteAddCommand(Index.fromOneBased(2), new Note(NOTE_STUB));
        NoteAddCommand diffNote = new NoteAddCommand(Index.fromOneBased(1), new Note(NOTE_STUB_2));

        // same object
        assertTrue(command1.equals(command1));
        // same values
        assertTrue(command1.equals(command2));
        // different index
        assertFalse(command1.equals(diffIndex));
        // different note
        assertFalse(command1.equals(diffNote));
        // different type
        assertFalse(command1.equals(1));
        // null
        assertFalse(command1.equals(null));
    }

    @Test
    public void toStringMethod() {
        Index index = Index.fromOneBased(1);
        NoteAddCommand command = new NoteAddCommand(index, new Note(NOTE_STUB));
        String expected = NoteAddCommand.class.getCanonicalName()
                + "{index=" + index + ", note=" + NOTE_STUB + "}";
        assertEquals(expected, command.toString());
    }
}
