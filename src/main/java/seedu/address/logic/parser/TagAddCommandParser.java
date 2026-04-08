package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.TagAddCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.tag.Tag;

/**
 * Parses input arguments and creates a new TagAddCommand object.
 */
public class TagAddCommandParser implements Parser<TagAddCommand> {

    public static final String MESSAGE_ADD_EXCESSIVE_TAGS = "Only 1 tag can be added at a time.";

    /**
     * Parses the given {@code String} of arguments in the context of the TagAddCommand
     * and returns a TagAddCommand object for execution.
     *
     * @throws ParseException if the user input does not conform to the expected format
     */
    public TagAddCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_TAG);

        // If preamble is empty, or t/ is not found,
        // throw error
        if (argMultimap.getPreamble().trim().isEmpty() || !argMultimap.getValue(PREFIX_TAG).isPresent()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, TagAddCommand.MESSAGE_USAGE));
        }

        // if more than 1 t/ is found,
        // throw error
        if (argMultimap.getAllValues(PREFIX_TAG).size() > 1) {
            throw new ParseException(MESSAGE_ADD_EXCESSIVE_TAGS);
        }

        // Check index is a numerical value
        // If not, throw error
        String preamble = argMultimap.getPreamble().trim();
        int tempIndex;
        try {
            tempIndex = Integer.parseInt(preamble);
        } catch (NumberFormatException e) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, TagAddCommand.MESSAGE_USAGE));
        }

        // index is present but out of range
        if (tempIndex <= 0) {
            throw new ParseException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Index index = Index.fromOneBased(tempIndex);
        Tag tag = ParserUtil.parseTag(argMultimap.getValue(PREFIX_TAG).get());

        return new TagAddCommand(tag, index);
    }
}
