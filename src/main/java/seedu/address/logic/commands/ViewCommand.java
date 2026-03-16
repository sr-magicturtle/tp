package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.function.Predicate;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;

/**
 * Displays only the contact identified by the specified index in the currently displayed person list.
 * <p>
 * The command filters the current list such that only the selected person remains visible in the UI.
 * The index refers to the index shown in the most recent person listing.
 */
public class ViewCommand extends Command {

    /** Command word used to trigger this command. */
    public static final String COMMAND_WORD = "view";

    /**
     * Usage instructions for the view command.
     * Indicates the required parameters and an example of how the command should be used.
     */
    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Shows the person identified by the index number used in the displayed person list.\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1";

    /** Message shown to the user when the view command executes successfully. */
    public static final String MESSAGE_SUCCESS = "Viewing Person: %1$s";

    /** The index of the person to be viewed in the filtered person list. */
    private final Index targetIndex;

    /**
     * Creates a {@code ViewCommand} to display the person at the specified index.
     *
     * @param targetIndex The index of the person in the currently displayed list.
     */
    public ViewCommand(Index targetIndex) {
        this.targetIndex = targetIndex;
    }

    /**
     * Executes the view command.
     * <p>
     * The person corresponding to the given index is retrieved from the currently filtered list.
     * The model is then updated to display only that person by applying a predicate that matches the selected person.
     *
     * @param model The model which contains the address book data.
     * @return A {@code CommandResult} containing the success message and formatted person details.
     * @throws CommandException If the provided index is invalid or out of bounds in the current list.
     */
    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();

        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Person personToView = lastShownList.get(targetIndex.getZeroBased());
        Predicate<Person> predicate = p -> p.equals(personToView);
        model.updateFilteredPersonList(predicate);

        return new CommandResult(String.format(MESSAGE_SUCCESS, Messages.format(personToView)));
    }
}