package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;
import seedu.address.model.tag.Tag;

public class TagAddCommandTest {

    @Test
    public void execute_validIndexAndNewTag_success() {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        Model expectedModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());

        Index index = Index.fromOneBased(1);
        Tag tag = new Tag("classmate");

        Person personToTag = model.getFilteredPersonList().get(index.getZeroBased());
        Person taggedPerson = personToTag.addTag(tag);
        expectedModel.setPerson(personToTag, taggedPerson);

        TagAddCommand command = new TagAddCommand(tag, index);
        String expectedMessage = String.format(
                TagAddCommand.MESSAGE_TAG_PERSON_SUCCESS, tag.tagName, taggedPerson.getName());
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndex_throwsCommandException() {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        Tag tag = new Tag("classmate");

        TagAddCommand command = new TagAddCommand(tag, outOfBoundIndex);

        assertThrows(CommandException.class,
                Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX, () -> command.execute(model));
    }

    @Test
    public void execute_duplicateTag_throwsCommandException() {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        Index index = Index.fromOneBased(1);

        // Get an existing tag from the first person
        Person firstPerson = model.getFilteredPersonList().get(index.getZeroBased());
        Tag existingTag = firstPerson.getTags().iterator().next();

        TagAddCommand command = new TagAddCommand(existingTag, index);

        assertThrows(CommandException.class,
                TagAddCommand.MESSAGE_TAG_PERSON_FAILURE, () -> command.execute(model));
    }

    @Test
    public void execute_lastPersonValidTag_success() throws Exception {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        Model expectedModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());

        Index lastIndex = Index.fromOneBased(model.getFilteredPersonList().size());
        Tag newTag = new Tag("newtag");

        Person personToTag = model.getFilteredPersonList().get(lastIndex.getZeroBased());
        Person taggedPerson = personToTag.addTag(newTag);
        expectedModel.setPerson(personToTag, taggedPerson);

        TagAddCommand command = new TagAddCommand(newTag, lastIndex);
        String expectedMessage = String.format(
                TagAddCommand.MESSAGE_TAG_PERSON_SUCCESS, newTag.tagName, taggedPerson.getName());

        assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }

    @Test
    public void equals() {
        Tag tag = new Tag("classmate");
        TagAddCommand tagAddFirstCommand = new TagAddCommand(tag, Index.fromOneBased(1));
        TagAddCommand tagAddSecondCommand = new TagAddCommand(tag, Index.fromOneBased(2));

        // same object -> returns true
        assertTrue(tagAddFirstCommand.equals(tagAddFirstCommand));

        // same values -> returns true
        TagAddCommand tagAddFirstCommandCopy = new TagAddCommand(tag, Index.fromOneBased(1));
        assertTrue(tagAddFirstCommand.equals(tagAddFirstCommandCopy));

        // different types -> returns false
        assertFalse(tagAddFirstCommand.equals(1));

        // null -> returns false
        assertFalse(tagAddFirstCommand.equals(null));

        // different index -> returns false
        assertFalse(tagAddFirstCommand.equals(tagAddSecondCommand));

        // different tag -> returns false
        TagAddCommand tagAddDifferentTag = new TagAddCommand(new Tag("friend"), Index.fromOneBased(1));
        assertFalse(tagAddFirstCommand.equals(tagAddDifferentTag));
    }

    @Test
    public void execute_tagLimitReached_throwsCommandException() {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

        Index index = Index.fromOneBased(1);
        Person originalPerson = model.getFilteredPersonList().get(index.getZeroBased());

        Person personWithFiveTags = new seedu.address.testutil.PersonBuilder(originalPerson)
                .withTags("tag1", "tag2", "tag3", "tag4", "tag5")
                .build();
        model.setPerson(originalPerson, personWithFiveTags);

        TagAddCommand command = new TagAddCommand(new Tag("tag6"), index);

        assertThrows(CommandException.class,
                TagAddCommand.MESSAGE_TAG_LIMIT_REACHED, () -> command.execute(model));
    }
}
