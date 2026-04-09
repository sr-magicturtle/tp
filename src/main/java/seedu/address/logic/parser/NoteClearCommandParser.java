package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import seedu.address.commons.core.index.Index;
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

        Index index = ParserUtil.parseIndex(argument, NoteClearCommand.MESSAGE_USAGE);

        try {
            return new NoteClearCommand(index);
        } catch (IllegalArgumentException e) {
            throw new ParseException(e.getMessage());
        }
    }

}
