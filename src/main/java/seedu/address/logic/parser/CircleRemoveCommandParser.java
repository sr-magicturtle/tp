package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.CircleRemoveCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new CircleRemoveCommand object.
 */
public class CircleRemoveCommandParser implements Parser<CircleRemoveCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the CircleRemoveCommand
     * and returns a CircleRemoveCommand object for execution.
     *
     * @throws ParseException if the user input does not conform to the expected format
     */
    public CircleRemoveCommand parse(String args) throws ParseException {
        requireNonNull(args);
        String trimmedArgs = args.trim();

        if (trimmedArgs.isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                CircleRemoveCommand.MESSAGE_USAGE));
        }

        // Ensure exactly one argument (no extra tokens like a second index)
        String[] splitArgs = trimmedArgs.split("\\s+");
        if (splitArgs.length != 1) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                CircleRemoveCommand.MESSAGE_USAGE));
        }

        Index index = ParserUtil.parseIndex(trimmedArgs, CircleRemoveCommand.MESSAGE_USAGE);

        return new CircleRemoveCommand(index);
    }
}
