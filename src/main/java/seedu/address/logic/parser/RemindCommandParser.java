package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import seedu.address.logic.commands.RemindCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new RemindCommand object.
 */
public class RemindCommandParser implements Parser<RemindCommand> {

    @Override
    public RemindCommand parse(String args) throws ParseException {
        requireNonNull(args);
        String trimmedArgs = args.trim();

        if (trimmedArgs.isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    RemindCommand.MESSAGE_USAGE));
        }

        try {
            int days = Integer.parseInt(trimmedArgs);
            if (days <= 0) {
                throw new ParseException(RemindCommand.MESSAGE_INVALID_DAYS);
            }
            return new RemindCommand(days);
        } catch (NumberFormatException e) {
            throw new ParseException(RemindCommand.MESSAGE_INVALID_DAYS);
        }
    }
}
