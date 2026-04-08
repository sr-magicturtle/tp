package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.TagAddCommand;
import seedu.address.logic.commands.TagRemoveCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.tag.Tag;

/**
 * Parses input arguments and creates a new TagRemoveCommand object.
 */
public class TagRemoveCommandParser implements Parser<TagRemoveCommand> {

    public static final String MESSAGE_REMOVE_EXCESSIVE_TAGS = "Only 1 tag can be removed at a time.";

    /**
     * Parses the given {@code String} of arguments in the context of the TagRemoveCommand
     * and returns a TagRemoveCommand object for execution.
     *
     * @throws ParseException if the user input does not conform to the expected format
     */
    public TagRemoveCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_TAG);

        if (argMultimap.getPreamble().trim().isEmpty() || !argMultimap.getValue(PREFIX_TAG).isPresent()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, TagRemoveCommand.MESSAGE_USAGE));
        }

        if (argMultimap.getAllValues(PREFIX_TAG).size() > 1) {
            throw new ParseException(MESSAGE_REMOVE_EXCESSIVE_TAGS);
        }

        // Check index is a numerical value
        // If not, throw error
        String preamble = argMultimap.getPreamble().trim();
        int tempIndex;
        try {
            tempIndex = Integer.parseInt(preamble);
        } catch (NumberFormatException e) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, TagRemoveCommand.MESSAGE_USAGE));
        }

        // index is present but out of range
        if (tempIndex <= 0) {
            throw new ParseException(MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Index index = Index.fromOneBased(tempIndex);
        Tag tag = ParserUtil.parseTag(argMultimap.getValue(PREFIX_TAG).get());

        return new TagRemoveCommand(tag, index);
    }
}
