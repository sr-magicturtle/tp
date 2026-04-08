package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.TagAddCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.tag.Tag;

/**
 * Parses input arguments and creates a new TagAddCommand object.
 */
public class TagAddCommandParser implements Parser<TagAddCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the TagAddCommand
     * and returns a TagAddCommand object for execution.
     *
     * @throws ParseException if the user input does not conform to the expected format
     */
    public TagAddCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_TAG);

        if (argMultimap.getPreamble().trim().isEmpty() || !argMultimap.getValue(PREFIX_TAG).isPresent()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, TagAddCommand.MESSAGE_USAGE));
        }

        if (argMultimap.getAllValues(PREFIX_TAG).size() > 1) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, TagAddCommand.MESSAGE_USAGE));
        }

        Index index = ParserUtil.parseIndex(argMultimap.getPreamble().trim());
        Tag tag = ParserUtil.parseTag(argMultimap.getValue(PREFIX_TAG).get());

        return new TagAddCommand(index, tag);
    }
}
