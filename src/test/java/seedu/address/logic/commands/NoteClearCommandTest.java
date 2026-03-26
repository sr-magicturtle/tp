package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.Optional;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Note;
import seedu.address.model.person.Person;

public class NoteClearCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_validIndexPersonWithNote_success() {
        // Seed the person with an existing note first
        Person original = model.getFilteredPersonList().get(0);
        Person withNote = new Person(
                original.getName(), original.getPhone(), original.getEmail(),
                original.getAddress(), original.getTags(),
                original.getFollowUpDate(),
                Optional.of(new Note("Met at career fair")),
                original.getCircle());
        model.setPerson(original, withNote);

        NoteClearCommand command = new NoteClearCommand(Index.fromOneBased(1));

        Person expectedPerson = new Person(
                withNote.getName(), withNote.getPhone(), withNote.getEmail(),
                withNote.getAddress(), withNote.getTags(),
                withNote.getFollowUpDate(),
                Optional.empty(),
                withNote.getCircle());

        String expectedMessage = String.format(NoteClearCommand.MESSAGE_CLEAR_NOTE_SUCCESS,
                Messages.format(expectedPerson));

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.setPerson(withNote, expectedPerson);

        assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_validIndexPersonWithNoNote_success() {
        // Person already has no note — clearing should still succeed (idempotent)
        Person original = model.getFilteredPersonList().get(0);
        Person withNoNote = new Person(
                original.getName(), original.getPhone(), original.getEmail(),
                original.getAddress(), original.getTags(),
                original.getFollowUpDate(),
                Optional.empty(),
                original.getCircle());
        model.setPerson(original, withNoNote);

        NoteClearCommand command = new NoteClearCommand(Index.fromOneBased(1));

        Person expectedPerson = new Person(
                withNoNote.getName(), withNoNote.getPhone(), withNoNote.getEmail(),
                withNoNote.getAddress(), withNoNote.getTags(),
                withNoNote.getFollowUpDate(),
                Optional.empty(),
                withNoNote.getCircle());

        String expectedMessage = String.format("No note to clear for "
                + Messages.format(expectedPerson));

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.setPerson(withNoNote, expectedPerson);

        assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndex_throwsCommandException() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        NoteClearCommand command = new NoteClearCommand(outOfBoundIndex);
        assertCommandFailure(command, model, NoteClearCommand.MESSAGE_INVALID_PERSON);
    }

    @Test
    public void execute_noteIsEmptyAfterClear() throws Exception {
        // Seed a note, then clear it, then verify note is empty
        Person original = model.getFilteredPersonList().get(0);
        Person withNote = new Person(
                original.getName(), original.getPhone(), original.getEmail(),
                original.getAddress(), original.getTags(),
                original.getFollowUpDate(),
                Optional.of(new Note("Some important note")),
                original.getCircle());
        model.setPerson(original, withNote);

        NoteClearCommand command = new NoteClearCommand(Index.fromOneBased(1));
        command.execute(model);

        Person result = model.getFilteredPersonList().get(0);
        assertFalse(result.getNotes().isPresent());
    }

    @Test
    public void execute_preservesFollowUpDateAndCircle() throws Exception {
        Person original = model.getFilteredPersonList().get(0);
        Person withNote = new Person(
                original.getName(), original.getPhone(), original.getEmail(),
                original.getAddress(), original.getTags(),
                original.getFollowUpDate(),
                Optional.of(new Note("Some note")),
                original.getCircle());
        model.setPerson(original, withNote);

        NoteClearCommand command = new NoteClearCommand(Index.fromOneBased(1));
        command.execute(model);

        Person result = model.getFilteredPersonList().get(0);
        assertEquals(original.getFollowUpDate(), result.getFollowUpDate());
        assertEquals(original.getCircle(), result.getCircle());
    }

    @Test
    public void execute_preservesOtherFields() throws Exception {
        // Verify name, phone, email, address, tags are all untouched
        Person original = model.getFilteredPersonList().get(0);
        Person withNote = new Person(
                original.getName(), original.getPhone(), original.getEmail(),
                original.getAddress(), original.getTags(),
                original.getFollowUpDate(),
                Optional.of(new Note("Some note")),
                original.getCircle());
        model.setPerson(original, withNote);

        NoteClearCommand command = new NoteClearCommand(Index.fromOneBased(1));
        command.execute(model);

        Person result = model.getFilteredPersonList().get(0);
        assertEquals(original.getName(), result.getName());
        assertEquals(original.getPhone(), result.getPhone());
        assertEquals(original.getEmail(), result.getEmail());
        assertEquals(original.getAddress(), result.getAddress());
        assertEquals(original.getTags(), result.getTags());
    }

    @Test
    public void equals() {
        NoteClearCommand command1 = new NoteClearCommand(Index.fromOneBased(1));
        NoteClearCommand command2 = new NoteClearCommand(Index.fromOneBased(1));
        NoteClearCommand diffIndex = new NoteClearCommand(Index.fromOneBased(2));

        // same object
        assertTrue(command1.equals(command1));
        // same values
        assertTrue(command1.equals(command2));
        // different index
        assertFalse(command1.equals(diffIndex));
        // different type
        assertFalse(command1.equals(1));
        // null
        assertFalse(command1.equals(null));
    }

    @Test
    public void toStringMethod() {
        Index index = Index.fromOneBased(1);
        NoteClearCommand command = new NoteClearCommand(index);
        String expected = NoteClearCommand.class.getCanonicalName()
                + "{index=" + index + "}";
        assertEquals(expected, command.toString());
    }
}
