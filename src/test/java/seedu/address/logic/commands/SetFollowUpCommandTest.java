package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.logic.Messages.MESSAGE_OOR_INDEX;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.FollowUpDate;
import seedu.address.model.person.Person;
import seedu.address.testutil.PersonBuilder;

public class SetFollowUpCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_validIndex_success() throws Exception {
        Person personToEdit = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        FollowUpDate followUpDate = new FollowUpDate("2026-12-31");
        SetFollowUpCommand setFollowUpCommand = new SetFollowUpCommand(INDEX_FIRST_PERSON, followUpDate);

        Person editedPerson = new PersonBuilder(personToEdit).withFollowUpDate("2026-12-31").build();

        String expectedMessage = String.format(SetFollowUpCommand.MESSAGE_SET_FOLLOW_UP_SUCCESS,
                seedu.address.logic.Messages.format(editedPerson));

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.setPerson(personToEdit, editedPerson);

        CommandResult commandResult = setFollowUpCommand.execute(model);

        assertEquals(expectedMessage, commandResult.getFeedbackToUser());
        assertEquals(expectedModel, model);
    }

    @Test
    public void execute_invalidIndex_throwsCommandException() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        SetFollowUpCommand setFollowUpCommand =
                new SetFollowUpCommand(outOfBoundIndex, new FollowUpDate("2099-12-31"));

        String expectedMessage = MESSAGE_OOR_INDEX;
        assertThrows(CommandException.class, expectedMessage, () -> {
            setFollowUpCommand.execute(model);
        });
    }

    @Test
    public void equals() {
        SetFollowUpCommand firstCommand =
                new SetFollowUpCommand(INDEX_FIRST_PERSON, new FollowUpDate("2099-12-31"));
        SetFollowUpCommand secondCommand =
                new SetFollowUpCommand(INDEX_SECOND_PERSON, new FollowUpDate("2099-12-30"));

        assertEquals(firstCommand, firstCommand);
        org.junit.jupiter.api.Assertions.assertNotEquals(firstCommand, secondCommand);
        org.junit.jupiter.api.Assertions.assertNotEquals(firstCommand, null);
        org.junit.jupiter.api.Assertions.assertNotEquals(firstCommand, new ClearCommand());
    }

    @Test
    public void execute_pastDate_successWithWarning() throws Exception {
        Person personToEdit = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        FollowUpDate followUpDate = new FollowUpDate("2020-01-01");
        SetFollowUpCommand setFollowUpCommand = new SetFollowUpCommand(INDEX_FIRST_PERSON, followUpDate);

        Person editedPerson = new PersonBuilder(personToEdit).withFollowUpDate("2020-01-01").build();

        String expectedMessage = String.format(SetFollowUpCommand.MESSAGE_SET_FOLLOW_UP_SUCCESS,
                seedu.address.logic.Messages.format(editedPerson))
                + "\n" + FollowUpDate.MESSAGE_PAST_DATE_WARNING;

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.setPerson(personToEdit, editedPerson);

        CommandResult commandResult = setFollowUpCommand.execute(model);

        assertEquals(expectedMessage, commandResult.getFeedbackToUser());
        assertEquals(expectedModel, model);
    }

    @Test
    public void execute_farFuture_successWithWarning() throws Exception {
        Person personToEdit = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        FollowUpDate followUpDate = new FollowUpDate("2099-12-31");
        SetFollowUpCommand setFollowUpCommand = new SetFollowUpCommand(INDEX_FIRST_PERSON, followUpDate);

        Person editedPerson = new PersonBuilder(personToEdit).withFollowUpDate("2099-12-31").build();

        String expectedMessage = String.format(SetFollowUpCommand.MESSAGE_SET_FOLLOW_UP_SUCCESS,
                seedu.address.logic.Messages.format(editedPerson))
                + "\n" + FollowUpDate.MESSAGE_FAR_FUTURE_WARNING;

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.setPerson(personToEdit, editedPerson);

        CommandResult commandResult = setFollowUpCommand.execute(model);

        assertEquals(expectedMessage, commandResult.getFeedbackToUser());
        assertEquals(expectedModel, model);
    }
}
