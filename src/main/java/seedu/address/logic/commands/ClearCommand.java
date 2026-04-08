package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.ui.ConfirmationDialog;
import seedu.address.ui.ConfirmationInterface;

/**
 * Clears the address book.
 */
public class ClearCommand extends Command {

    public static final String COMMAND_WORD = "clear";

    public static final String MESSAGE_CLEAR_SUCCESS = "Address book has been cleared!";
    public static final String MESSAGE_CLEAR_CANCELLED = "Clear cancelled.";

    private final ConfirmationInterface confirmationInterface;

    /** Production constructor: uses real popup. */
    public ClearCommand() {
        this(ConfirmationDialog::showConfirmation);
    }

    /** Test constructor: inject stub confirmation (msg -> true/false). */
    public ClearCommand(ConfirmationInterface confirmationInterface) {
        requireNonNull(confirmationInterface);
        this.confirmationInterface = confirmationInterface;
    }

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);

        if (!confirmationInterface.confirm(
                "ARE YOU SURE YOU WANT TO CLEAR ALL CONTACTS ?")) {
            return new CommandResult(MESSAGE_CLEAR_CANCELLED);
        }

        model.setAddressBook(new AddressBook());
        return new CommandResult(MESSAGE_CLEAR_SUCCESS, false, false, false, true);
    }
}
