package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;
import static seedu.address.testutil.TypicalTags.CLASSMATE;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;
import seedu.address.model.tag.Tag;
import seedu.address.testutil.PersonBuilder;

/**
 * Contains integration tests (interaction with the Model) and unit tests for TagAddCommand.
 */
public class TagAddCommandTest {

    private static final String VALID_TAG_FOR_LIMIT_TEST_1 = "tag1";
    private static final String VALID_TAG_FOR_LIMIT_TEST_2 = "tag2";
    private static final String VALID_TAG_FOR_LIMIT_TEST_3 = "tag3";
    private static final String VALID_TAG_FOR_LIMIT_TEST_4 = "tag4";
    private static final String VALID_TAG_FOR_LIMIT_TEST_5 = "tag5";
    private static final String VALID_TAG_FOR_LIMIT_TEST_6 = "tag6";

    @Test
    public void execute_validIndexAndNewTag_success() {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        Model expectedModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());

        Person personToTag = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person taggedPerson = personToTag.addTag(CLASSMATE);
        expectedModel.setPerson(personToTag, taggedPerson);

        TagAddCommand command = new TagAddCommand(INDEX_FIRST_PERSON, CLASSMATE);
        String expectedMessage = String.format(
                TagAddCommand.MESSAGE_TAG_PERSON_SUCCESS, CLASSMATE.tagName, taggedPerson.getName());
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndex_throwsCommandException() {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);

        TagAddCommand command = new TagAddCommand(outOfBoundIndex, CLASSMATE);

        assertThrows(CommandException.class,
                TagAddCommand.MESSAGE_INVALID_PERSON, () -> command.execute(model));
    }

    @Test
    public void execute_duplicateTag_throwsCommandException() {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

        // Get an existing tag from the first person
        Person firstPerson = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Tag existingTag = firstPerson.getTags().iterator().next();

        TagAddCommand command = new TagAddCommand(INDEX_FIRST_PERSON, existingTag);

        assertThrows(CommandException.class,
                TagAddCommand.MESSAGE_TAG_PERSON_FAILURE, () -> command.execute(model));
    }

    @Test
    public void execute_lastPersonValidTag_success() {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        Model expectedModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());

        Index lastIndex = Index.fromOneBased(model.getFilteredPersonList().size());

        Person personToTag = model.getFilteredPersonList().get(lastIndex.getZeroBased());
        Person taggedPerson = personToTag.addTag(CLASSMATE);
        expectedModel.setPerson(personToTag, taggedPerson);

        TagAddCommand command = new TagAddCommand(lastIndex, CLASSMATE);
        String expectedMessage = String.format(
                TagAddCommand.MESSAGE_TAG_PERSON_SUCCESS, CLASSMATE.tagName, taggedPerson.getName());

        assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_tagLimitReached_throwsCommandException() {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

        Person originalPerson = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person personWithFiveTags = new PersonBuilder(originalPerson)
                .withTags(VALID_TAG_FOR_LIMIT_TEST_1, VALID_TAG_FOR_LIMIT_TEST_2,
                        VALID_TAG_FOR_LIMIT_TEST_3, VALID_TAG_FOR_LIMIT_TEST_4,
                        VALID_TAG_FOR_LIMIT_TEST_5)
                .build();
        model.setPerson(originalPerson, personWithFiveTags);

        TagAddCommand command = new TagAddCommand(INDEX_FIRST_PERSON, new Tag(VALID_TAG_FOR_LIMIT_TEST_6));

        assertThrows(CommandException.class,
                TagAddCommand.MESSAGE_TAG_LIMIT_REACHED, () -> command.execute(model));
    }

    @Test
    public void equals() {
        TagAddCommand tagAddFirstCommand = new TagAddCommand(INDEX_FIRST_PERSON, CLASSMATE);
        TagAddCommand tagAddSecondCommand = new TagAddCommand(INDEX_SECOND_PERSON, CLASSMATE);

        // same object -> returns true
        assertTrue(tagAddFirstCommand.equals(tagAddFirstCommand));

        // same values -> returns true
        TagAddCommand tagAddFirstCommandCopy = new TagAddCommand(INDEX_FIRST_PERSON, CLASSMATE);
        assertTrue(tagAddFirstCommand.equals(tagAddFirstCommandCopy));

        // different types -> returns false
        assertFalse(tagAddFirstCommand.equals(1));

        // null -> returns false
        assertFalse(tagAddFirstCommand.equals(null));

        // different index -> returns false
        assertFalse(tagAddFirstCommand.equals(tagAddSecondCommand));

        // different tag -> returns false
        Tag differentTag = new Tag("friend");
        TagAddCommand tagAddDifferentTag = new TagAddCommand(INDEX_FIRST_PERSON, differentTag);
        assertFalse(tagAddFirstCommand.equals(tagAddDifferentTag));
    }
}
