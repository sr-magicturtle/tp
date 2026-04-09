package seedu.address.logic.parser;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.FollowUpClearCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new FollowUpClearCommand object.
 */
public class FollowUpClearCommandParser implements Parser<FollowUpClearCommand> {

    @Override
    public FollowUpClearCommand parse(String args) throws ParseException {
        Index index = ParserUtil.parseIndex(args.trim(), FollowUpClearCommand.MESSAGE_USAGE);
        return new FollowUpClearCommand(index);
    }
}
