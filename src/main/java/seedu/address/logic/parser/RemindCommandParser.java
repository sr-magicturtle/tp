package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import seedu.address.commons.core.index.Index;
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
        Index daysIndex = ParserUtil.parseIndex(trimmedArgs);
        return new RemindCommand(daysIndex.getOneBased());
    }
}
