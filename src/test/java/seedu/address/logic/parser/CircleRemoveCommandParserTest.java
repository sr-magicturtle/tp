package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.logic.parser.ParserUtil.MESSAGE_INVALID_INDEX;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.CircleRemoveCommand;

public class CircleRemoveCommandParserTest {

    private final CircleRemoveCommandParser parser = new CircleRemoveCommandParser();

    @Test
    public void parse_validArgs_success() {
        Index expectedIndex = Index.fromOneBased(1);
        CircleRemoveCommand expectedCommand = new CircleRemoveCommand(expectedIndex);
        assertParseSuccess(parser, "1", expectedCommand);
    }

    @Test
    public void parse_validArgsWithWhitespace_success() {
        Index expectedIndex = Index.fromOneBased(1);
        CircleRemoveCommand expectedCommand = new CircleRemoveCommand(expectedIndex);
        assertParseSuccess(parser, "   1   ", expectedCommand);
    }

    @Test
    public void parse_validArgsLargerIndex_success() {
        Index expectedIndex = Index.fromOneBased(10);
        CircleRemoveCommand expectedCommand = new CircleRemoveCommand(expectedIndex);
        assertParseSuccess(parser, "10", expectedCommand);
    }

    @Test
    public void parse_validArgsVeryLargeIndex_success() {
        Index expectedIndex = Index.fromOneBased(999);
        CircleRemoveCommand expectedCommand = new CircleRemoveCommand(expectedIndex);
        assertParseSuccess(parser, "999", expectedCommand);
    }

    @Test
    public void parse_emptyArgs_failure() {
        // empty input
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, CircleRemoveCommand.MESSAGE_USAGE);
        assertParseFailure(parser, "", expectedMessage);

        // whitespace only
        assertParseFailure(parser, "   ", expectedMessage);
        assertParseFailure(parser, "\t", expectedMessage);
    }

    @Test
    public void parse_invalidIndex_failure() {
        // zero index (not allowed)
        assertParseFailure(parser, "0", MESSAGE_INVALID_INDEX);

        // negative index
        assertParseFailure(parser, "-1", MESSAGE_INVALID_INDEX);

        // non-integer index
        assertParseFailure(parser, "abc", MESSAGE_INVALID_INDEX);

        // decimal index
        assertParseFailure(parser, "1.5", MESSAGE_INVALID_INDEX);

        // index with letters mixed in
        assertParseFailure(parser, "1a", MESSAGE_INVALID_INDEX);
        assertParseFailure(parser, "a1", MESSAGE_INVALID_INDEX);
        assertParseFailure(parser, "1a2", MESSAGE_INVALID_INDEX);

        // very large index (beyond integer range)
        assertParseFailure(parser, "99999999999999999999", MESSAGE_INVALID_INDEX);

        // scientific notation
        assertParseFailure(parser, "1e10", MESSAGE_INVALID_INDEX);

        // leading plus sign
        assertParseFailure(parser, "+1", MESSAGE_INVALID_INDEX);
    }

    @Test
    public void parse_multipleIndexes_failure() {
        // multiple indexes should fail
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, CircleRemoveCommand.MESSAGE_USAGE);
        assertParseFailure(parser, "1 2", expectedMessage);

        // three indexes
        assertParseFailure(parser, "1 2 3", expectedMessage);

        // index with extra tokens
        assertParseFailure(parser, "1 extra", expectedMessage);
    }

    @Test
    public void parse_extraArguments_failure() {
        // index followed by text
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, CircleRemoveCommand.MESSAGE_USAGE);
        assertParseFailure(parser, "1 client", expectedMessage);

        // index followed by circle prefix
        assertParseFailure(parser, "1 c/client", expectedMessage);

        // index followed by other prefixes
        assertParseFailure(parser, "1 t/tag", expectedMessage);
    }

    @Test
    public void parse_specialCharacters_failure() {
        // special characters as index
        assertParseFailure(parser, "@", MESSAGE_INVALID_INDEX);
        assertParseFailure(parser, "#", MESSAGE_INVALID_INDEX);
        assertParseFailure(parser, "$", MESSAGE_INVALID_INDEX);
        assertParseFailure(parser, "%", MESSAGE_INVALID_INDEX);
        assertParseFailure(parser, "!", MESSAGE_INVALID_INDEX);
    }

    @Test
    public void parse_indexWithWhitespace_failure() {
        // index with internal whitespace (spaces)
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, CircleRemoveCommand.MESSAGE_USAGE);
        assertParseFailure(parser, "1 0", expectedMessage);

        // index with tabs and numbers
        assertParseFailure(parser, "1\t2", expectedMessage);
    }

    @Test
    public void parse_whitespaceHandling() {
        // tabs as whitespace
        Index expectedIndex = Index.fromOneBased(1);
        CircleRemoveCommand expectedCommand = new CircleRemoveCommand(expectedIndex);
        assertParseSuccess(parser, "\t1\t", expectedCommand);

        // multiple spaces
        assertParseSuccess(parser, "   1   ", expectedCommand);

        // newlines with index
        assertParseSuccess(parser, "\n1\n", expectedCommand);
    }

}
