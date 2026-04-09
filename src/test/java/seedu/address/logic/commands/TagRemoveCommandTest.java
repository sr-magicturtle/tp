package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;
import static seedu.address.testutil.TypicalTags.COLLEAGUE;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;
import seedu.address.model.tag.Tag;

/**
 * Contains integration tests (interaction with the Model) and unit tests for TagRemoveCommand.
 */
public class TagRemoveCommandTest {

    @Test
    public void execute_validIndexAndExistingTag_success() {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        Model expectedModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());

        Person personToRemoveTagFrom = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Tag existingTag = personToRemoveTagFrom.getTags().iterator().next();

        Person editedPerson = personToRemoveTagFrom.removeTag(existingTag);
        expectedModel.setPerson(personToRemoveTagFrom, editedPerson);

        TagRemoveCommand command = new TagRemoveCommand(INDEX_FIRST_PERSON, existingTag);
        String expectedMessage = String.format(
                TagRemoveCommand.MESSAGE_REMOVE_TAG_SUCCESS, existingTag.tagName, editedPerson.getName());

        assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndex_throwsCommandException() {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);

        TagRemoveCommand command = new TagRemoveCommand(outOfBoundIndex, COLLEAGUE);

        assertThrows(CommandException.class,
                Messages.MESSAGE_OOR_INDEX, () -> command.execute(model));
    }

    @Test
    public void execute_tagNotPresentOnPerson_throwsCommandException() {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        Tag tagNotPresent = new Tag("nonexistent-tag");

        TagRemoveCommand command = new TagRemoveCommand(INDEX_FIRST_PERSON, tagNotPresent);

        assertThrows(CommandException.class,
                TagRemoveCommand.MESSAGE_REMOVE_TAG_FAILURE, () -> command.execute(model));
    }

    @Test
    public void execute_secondPersonValidTag_success() {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        Model expectedModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());

        Person personToRemoveTagFrom = model.getFilteredPersonList().get(INDEX_SECOND_PERSON.getZeroBased());
        Tag existingTag = personToRemoveTagFrom.getTags().iterator().next();

        Person editedPerson = personToRemoveTagFrom.removeTag(existingTag);
        expectedModel.setPerson(personToRemoveTagFrom, editedPerson);

        TagRemoveCommand command = new TagRemoveCommand(INDEX_SECOND_PERSON, existingTag);
        String expectedMessage = String.format(
                TagRemoveCommand.MESSAGE_REMOVE_TAG_SUCCESS, existingTag.tagName, editedPerson.getName());

        assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }

    @Test
    public void equals() {
        TagRemoveCommand tagRemoveFirstCommand = new TagRemoveCommand(INDEX_FIRST_PERSON, COLLEAGUE);
        TagRemoveCommand tagRemoveSecondCommand = new TagRemoveCommand(INDEX_SECOND_PERSON, COLLEAGUE);

        // same object -> returns true
        assertTrue(tagRemoveFirstCommand.equals(tagRemoveFirstCommand));

        // same values -> returns true
        TagRemoveCommand tagRemoveFirstCommandCopy = new TagRemoveCommand(INDEX_FIRST_PERSON, COLLEAGUE);
        assertTrue(tagRemoveFirstCommand.equals(tagRemoveFirstCommandCopy));

        // different types -> returns false
        assertFalse(tagRemoveFirstCommand.equals(1));

        // null -> returns false
        assertFalse(tagRemoveFirstCommand.equals(null));

        // different index -> returns false
        assertFalse(tagRemoveFirstCommand.equals(tagRemoveSecondCommand));

        // different tag -> returns false
        Tag differentTag = new Tag("friend");
        TagRemoveCommand tagRemoveDifferentTag = new TagRemoveCommand(INDEX_FIRST_PERSON, differentTag);
        assertFalse(tagRemoveFirstCommand.equals(tagRemoveDifferentTag));
    }
}
