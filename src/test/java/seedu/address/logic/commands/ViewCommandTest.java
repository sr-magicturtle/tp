package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccessWithViewMode;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.Collections;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;

/**
 * Contains integration tests (interaction with the Model) for ViewCommand.
 */
public class ViewCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    private Model expectedModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void equals() {
        ViewCommand viewFirstCommand = new ViewCommand(Index.fromOneBased(1));
        ViewCommand viewSecondCommand = new ViewCommand(Index.fromOneBased(2));

        assertTrue(viewFirstCommand.equals(viewFirstCommand));

        ViewCommand viewFirstCommandCopy = new ViewCommand(Index.fromOneBased(1));
        assertTrue(viewFirstCommand.equals(viewFirstCommandCopy));

        assertFalse(viewFirstCommand.equals(1));
        assertFalse(viewFirstCommand.equals(null));
        assertFalse(viewFirstCommand.equals(viewSecondCommand));
    }

    @Test
    public void execute_validIndex_success() {
        Index index = Index.fromOneBased(1);
        Person personToView = model.getFilteredPersonList().get(index.getZeroBased());

        ViewCommand command = new ViewCommand(index);

        String expectedMessage = String.format(
                ViewCommand.MESSAGE_SUCCESS,
                personToView.getName()
        );

        expectedModel.updateFilteredPersonList(p -> p.isSamePerson(personToView));

        assertCommandSuccessWithViewMode(command, model, expectedMessage, expectedModel);

        assertEquals(Collections.singletonList(model.getFilteredPersonList().get(0)),
                model.getFilteredPersonList());
    }

    @Test
    public void execute_invalidIndex_throwsCommandException() {
        Index outOfBoundIndex = Index.fromOneBased(
                model.getFilteredPersonList().size() + 1);

        ViewCommand command = new ViewCommand(outOfBoundIndex);

        CommandException thrown = assertThrows(CommandException.class, () -> command.execute(model));

        assertEquals(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX,
                thrown.getMessage());
    }
}
