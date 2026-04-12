package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.logging.Logger;

import seedu.address.commons.core.LogsCenter;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.UpcomingFollowUpPredicate;

/**
 * Filters and lists all persons with follow up dates within the next specified number of days.
 */
public class RemindCommand extends Command {

    public static final String COMMAND_WORD = "remind";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Lists all persons with follow up dates within the next specified number of days.\n"
            + "Parameters: DAYS\n"
            + "Example: " + COMMAND_WORD + " 3";

    public static final String MESSAGE_SUCCESS =
            "Listed all persons with follow up dates within the next %1$d day(s).";

    public static final String MESSAGE_INVALID_DAYS =
            "DAYS must be a positive integer.\n" + MESSAGE_USAGE;

    private static final Logger logger = LogsCenter.getLogger(RemindCommand.class);

    private final int days;
    private final UpcomingFollowUpPredicate predicate;

    /**
     * Creates a RemindCommand to filter persons by follow up dates within the next specified number of days.
     */
    public RemindCommand(int days) {
        this.days = days;
        this.predicate = new UpcomingFollowUpPredicate(days);
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        assert days > 0;

        logger.fine("Filtering persons with follow up dates in the next " + days + " day(s)");
        model.updateFilteredPersonList(predicate);

        return new CommandResult(String.format(MESSAGE_SUCCESS, days), false, false, false, true);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof RemindCommand)) {
            return false;
        }

        RemindCommand otherCommand = (RemindCommand) other;
        return days == otherCommand.days;
    }

    @Override
    public String toString() {
        return RemindCommand.class.getCanonicalName() + "{days=" + days + "}";
    }
}
