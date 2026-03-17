package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.TagAddCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.tag.Tag;

/**
 * Parses input arguments and creates a new TagAddCommand object.
 *
 * This parser is responsible for parsing user input to extract an index and a tag,
 * which are then used to create a TagAddCommand. The parser expects the input
 * to contain an index (in the preamble) and exactly one tag specified with the
 * PREFIX_TAG prefix.
 *
 * <p>Expected input format: INDEX t/TAG
 * where INDEX is a positive integer representing the position of the person in the address book,
 * and TAG is the name of the tag to be added.
 *
 * <p>Validation:
 * <ul>
 *   <li>Ensures exactly one tag is provided (no more, no less)</li>
 *   <li>Validates that the index is a valid positive integer</li>
 *   <li>Validates that the tag is valid according to Tag constraints</li>
 * </ul>
 *
 * @throws ParseException if the input format is invalid, if no tag is provided,
 *                        if more than one tag is provided, or if the index is invalid
 */
public class TagAddCommandParser implements Parser<TagAddCommand> {

    /**
     * Parses the given arguments string and returns a TagAddCommand.
     *
     * <p>This method tokenizes the input arguments to separate the index from the tag prefix.
     * It validates that exactly one tag is provided and that the index is valid.
     *
     * @param args the input arguments string containing an index and a tag with PREFIX_TAG prefix
     * @return a TagAddCommand object containing the parsed tag and index
     * @throws ParseException if the arguments format is invalid, if no tag is provided,
     *                        if multiple tags are provided, or if the index cannot be parsed
     */
    public TagAddCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
            ArgumentTokenizer.tokenize(args, PREFIX_TAG);

        String preamble = argMultimap.getPreamble().trim();

        String[] preambleParts = preamble.split("\\s+");
        for (String part : preambleParts) {
            if (part.matches("\\S+/\\S*")) { // token contains a prefix-like pattern e.g. l/123 or l/
                throw new ParseException("Invalid prefix detected: " + part);
            }
        }

        if (preambleParts.length == 0) {
            throw new ParseException("Missing index.");
        }

        Index index;
        try {
            index = ParserUtil.parseIndex(preambleParts[0]);
        } catch (ParseException pe) {
            throw new ParseException("Index is not a non-zero unsigned integer.");
        }

        List<String> allTags = argMultimap.getAllValues(PREFIX_TAG);
        if (allTags.isEmpty()) {
            throw new ParseException("Missing tag value. Use prefix t/TAG.");
        }
        if (allTags.size() > 1) {
            throw new ParseException(
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, TagAddCommand.MESSAGE_USAGE));
        }

        Tag tag;
        try {
            tag = ParserUtil.parseTag(allTags.get(0));
        } catch (ParseException pe) {
            throw new ParseException("Invalid tag format. Tags must be 1–20 chars (a-z, 0-9, -).");
        }

        return new TagAddCommand(tag, index);
    }
}
