package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.logic.parser.TagAddCommandParser.MESSAGE_ADD_EXCESSIVE_TAGS;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.TagAddCommand;
import seedu.address.model.tag.Tag;

public class TagAddCommandParserTest {

    private final TagAddCommandParser parser = new TagAddCommandParser();

    @Test
    public void parse_validArgs_success() {
        Tag expectedTag = new Tag("friend");
        Index expectedIndex = Index.fromOneBased(1);
        TagAddCommand expectedCommand = new TagAddCommand(expectedTag, expectedIndex);

        assertParseSuccess(parser, "1 t/friend", expectedCommand);
        assertParseSuccess(parser, "   1   t/friend   ", expectedCommand);

        Tag hyphenTag = new Tag("hello-world");
        TagAddCommand hyphenCommand = new TagAddCommand(hyphenTag, expectedIndex);
        assertParseSuccess(parser, "1 t/hello-world", hyphenCommand);
    }

    @Test
    public void parse_missingTag_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, TagAddCommand.MESSAGE_USAGE);
        assertParseFailure(parser, "1", expectedMessage);
    }

    @Test
    public void parse_missingIndex_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, TagAddCommand.MESSAGE_USAGE);
        assertParseFailure(parser, "t/friend", expectedMessage);
        assertParseFailure(parser, "", expectedMessage);
    }

    @Test
    public void parse_multipleTags_failure() {
        assertParseFailure(parser, "1 t/friend t/enemy", MESSAGE_ADD_EXCESSIVE_TAGS);
    }

    @Test
    public void parse_invalidIndex_failure() {
        assertParseFailure(parser, "0 t/friend", MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        assertParseFailure(parser, "-1 t/friend", MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void parse_nonNumericIndex_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, TagAddCommand.MESSAGE_USAGE);
        assertParseFailure(parser, "abc t/friend", expectedMessage);
        assertParseFailure(parser, "1 extra t/friend", expectedMessage);
    }

    @Test
    public void parse_invalidTag_failure() {
        assertParseFailure(parser, "1 t/@@@", Tag.MESSAGE_CONSTRAINTS);
        assertParseFailure(parser, "1 t/Hello", Tag.MESSAGE_CONSTRAINTS);
        assertParseFailure(parser, "1 t/bro ther", Tag.MESSAGE_CONSTRAINTS);
        assertParseFailure(parser, "1 t/", Tag.MESSAGE_CONSTRAINTS);
    }
}
