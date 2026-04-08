package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.commands.NoteAddCommand.MESSAGE_INVALID_INDEX;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.NoteAddCommand;
import seedu.address.logic.commands.NoteClearCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new NoteClearCommand object.
 * Expected format: noteclear INDEX
 * Example: noteclear 1
 */
public class NoteClearCommandParser implements Parser<NoteClearCommand> {

    @Override
    public NoteClearCommand parse(String args) throws ParseException {
        requireNonNull(args);
        String argument = args.trim();

        // Ensure argument exists
        if (argument.isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    NoteClearCommand.MESSAGE_USAGE));
        }

        // Ensure exactly one argument (no extra tokens like a second index)
        String[] splitArgs = argument.split("\\s+");
        if (splitArgs.length != 1) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    NoteClearCommand.MESSAGE_USAGE));
        }

        // Check if argument is a valid integer
        int rawInt;
        try {
            rawInt = Integer.parseInt(argument);
        } catch (NumberFormatException e) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, NoteAddCommand.MESSAGE_USAGE));
        }

        // Check if index is positive
        if (rawInt <= 0) {
            throw new ParseException(MESSAGE_INVALID_INDEX); // index is present but out of range
        }

        Index index = Index.fromOneBased(rawInt);

        try {
            return new NoteClearCommand(index);
        } catch (IllegalArgumentException e) {
            throw new ParseException(e.getMessage());
        }
    }

}
