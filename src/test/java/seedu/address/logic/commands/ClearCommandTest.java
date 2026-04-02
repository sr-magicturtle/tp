package seedu.address.logic.commands;

import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccessWithExitView;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.Test;

import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;

public class ClearCommandTest {

    @Test
    public void execute_emptyAddressBookConfirmed_success() {
        Model model = new ModelManager();
        Model expectedModel = new ModelManager();

        ClearCommand command = new ClearCommand(msg -> true);

        assertCommandSuccessWithExitView(command, model, ClearCommand.MESSAGE_CLEAR_SUCCESS, expectedModel);
    }

    @Test
    public void execute_emptyAddressBookCancelled_success() {
        Model model = new ModelManager();
        Model expectedModel = new ModelManager();

        ClearCommand command = new ClearCommand(msg -> false);

        assertCommandSuccess(command, model, ClearCommand.MESSAGE_CLEAR_CANCELLED, expectedModel);
    }

    @Test
    public void execute_nonEmptyAddressBookConfirmed_success() {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        Model expectedModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        expectedModel.setAddressBook(new AddressBook());

        ClearCommand command = new ClearCommand(msg -> true);

        assertCommandSuccessWithExitView(command, model, ClearCommand.MESSAGE_CLEAR_SUCCESS, expectedModel);
    }

    @Test
    public void execute_nonEmptyAddressBookCancelled_success() {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        Model expectedModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());

        ClearCommand command = new ClearCommand(msg -> false);

        assertCommandSuccess(command, model, ClearCommand.MESSAGE_CLEAR_CANCELLED, expectedModel);
    }
}
