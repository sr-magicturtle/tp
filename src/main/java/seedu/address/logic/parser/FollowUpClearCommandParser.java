package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.FollowUpClearCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new FollowUpClearCommand object.
 */
public class FollowUpClearCommandParser implements Parser<FollowUpClearCommand> {

    @Override
    public FollowUpClearCommand parse(String args) throws ParseException {
        try {
            Index index = ParserUtil.parseIndex(args.trim());
            return new FollowUpClearCommand(index);
        } catch (ParseException pe) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, FollowUpClearCommand.MESSAGE_USAGE), pe);
        }
    }
}
