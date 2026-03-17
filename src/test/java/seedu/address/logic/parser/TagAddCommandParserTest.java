package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

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

        // normal
        assertParseSuccess(parser, "1 t/friend", expectedCommand);

        // leading/trailing whitespace
        assertParseSuccess(parser, "   1   t/friend   ", expectedCommand);
    }

    @Test
    public void parse_missingTagPrefix_failure() {
        // no t/ provided at all
        assertParseFailure(parser, "1", "Missing tag value. Use prefix t/TAG.");
    }

    @Test
    public void parse_multipleTags_failure() {
        // more than one t/ should fail with invalid command format using MESSAGE_USAGE
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, TagAddCommand.MESSAGE_USAGE);
        assertParseFailure(parser, "1 t/friend t/enemy", expectedMessage);
    }

    @Test
    public void parse_invalidIndex_failure() {
        // zero index
        assertParseFailure(parser, "0 t/friend", "Index is not a non-zero unsigned integer.");

        // non-integer index
        assertParseFailure(parser, "abc t/friend", "Index is not a non-zero unsigned integer.");

        // missing index (empty input) — current parser behavior
        assertParseFailure(parser, "", "Index is not a non-zero unsigned integer.");
    }

    @Test
    public void parse_unknownPrefixInPreamble_failure() {
        // unknown prefix-like token in preamble should be rejected
        assertParseFailure(parser, "1 l/123 t/friend", "Invalid prefix detected: l/123");
    }

    @Test
    public void parse_invalidTag_failure() {
        // tag violates Tag constraints -> parser wraps with its own message
        assertParseFailure(parser, "1 t/@@@", "Invalid tag format. Tags must be 1–20 chars (a-z, 0-9, -).");
    }

    @Test
    public void parse_emptyTagValue_failure() {
        // prefix is present but empty value -> treated as invalid tag format in your parser
        assertParseFailure(parser, "1 t/", "Invalid tag format. Tags must be 1–20 chars (a-z, 0-9, -).");
    }
}