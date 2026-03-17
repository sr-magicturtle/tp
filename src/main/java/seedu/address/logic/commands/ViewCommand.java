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
 * Represents a command that displays the contact identified by the specified index
 * in the currently displayed person list.
 *
 * The command filters the current list such that only the selected person remains visible.
 * The index refers to the index shown in the most recent person listing.
 */
public class ViewCommand extends Command {

    /** Command word used to execute this command. */
    public static final String COMMAND_WORD = "view";

    /**
     * Usage instructions for the view command.
     *
     * Indicates the required parameters and an example of how the command should be used.
     */
    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Shows the person identified by the index number used in the displayed person list.\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1";

    /** Success message displayed when the command executes successfully. */
    public static final String MESSAGE_SUCCESS = "Viewing Person: %1$s";

    /** Index of the person to be viewed in the filtered person list. */
    private final Index targetIndex;

    /**
     * Creates a ViewCommand to display the person at the specified index.
     *
     * @param targetIndex Index of the person in the currently displayed list.
     */
    public ViewCommand(Index targetIndex) {
        this.targetIndex = targetIndex;
    }

    /**
     * Executes the command to display the selected person.
     *
     * Retrieves the person corresponding to the given index from the filtered list
     * and updates the model to display only that person.
     *
     * @param model Model containing the address book data.
     * @return CommandResult containing the success message and formatted person details.
     * @throws CommandException If the index is invalid or out of bounds.
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