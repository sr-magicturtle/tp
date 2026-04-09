package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_FOLLOW_UP_DATE;

import java.util.stream.Stream;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.SetFollowUpCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.FollowUpDate;

/**
 * Parses input arguments and creates a new SetFollowUpCommand object.
 */
public class SetFollowUpCommandParser implements Parser<SetFollowUpCommand> {

    @Override
    public SetFollowUpCommand parse(String args) throws ParseException {
        requireNonNull(args);

        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_FOLLOW_UP_DATE);

        if (!arePrefixesPresent(argMultimap, PREFIX_FOLLOW_UP_DATE)
                || argMultimap.getPreamble().trim().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    SetFollowUpCommand.MESSAGE_USAGE));
        }

        // Reuse shared duplicate-prefix validation logic
        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_FOLLOW_UP_DATE);
        Index index = ParserUtil.parseIndex(argMultimap.getPreamble().trim(), SetFollowUpCommand.MESSAGE_USAGE);

        FollowUpDate followUpDate = ParserUtil.parseFollowUpDate(
                argMultimap.getValue(PREFIX_FOLLOW_UP_DATE).get());

        return new SetFollowUpCommand(index, followUpDate);
    }

    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }
}
