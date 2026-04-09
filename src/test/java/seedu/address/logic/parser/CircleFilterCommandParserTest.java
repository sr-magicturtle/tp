package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.model.circle.Circle.MESSAGE_CONSTRAINTS;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.CircleFilterCommand;

public class CircleFilterCommandParserTest {

    private final CircleFilterCommandParser parser = new CircleFilterCommandParser();

    @Test
    public void parse_validArgs_success() {
        // valid arguments with lowercase circle name
        CircleFilterCommand expectedCommand = new CircleFilterCommand("client");
        assertParseSuccess(parser, "client", expectedCommand);

        // valid arguments with uppercase circle name (should be normalized)
        CircleFilterCommand expectedCommandUpper = new CircleFilterCommand("client");
        assertParseSuccess(parser, "CLIENT", expectedCommandUpper);

        // valid arguments with mixed case
        CircleFilterCommand expectedCommandMixed = new CircleFilterCommand("prospect");
        assertParseSuccess(parser, "PrOsPeCt", expectedCommandMixed);

        // valid arguments with leading/trailing whitespace (should be trimmed)
        CircleFilterCommand expectedCommandSpace = new CircleFilterCommand("friend");
        assertParseSuccess(parser, "   friend   ", expectedCommandSpace);

        // all three valid circle names
        assertParseSuccess(parser, "client", new CircleFilterCommand("client"));
        assertParseSuccess(parser, "prospect", new CircleFilterCommand("prospect"));
        assertParseSuccess(parser, "friend", new CircleFilterCommand("friend"));
    }

    @Test
    public void parse_emptyArgs_failure() {
        // empty input
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, CircleFilterCommand.MESSAGE_USAGE);
        assertParseFailure(parser, "", expectedMessage);

        // whitespace only
        assertParseFailure(parser, "   ", expectedMessage);
        assertParseFailure(parser, "\t", expectedMessage);
        assertParseFailure(parser, "\n", expectedMessage);
    }

    @Test
    public void parse_invalidCircleName_failure() {
        // invalid circle name (not one of client, prospect, friend)
        assertParseFailure(parser, "invalid", MESSAGE_CONSTRAINTS);

        // misspelled circle names
        assertParseFailure(parser, "clients", MESSAGE_CONSTRAINTS);
        assertParseFailure(parser, "prospects", MESSAGE_CONSTRAINTS);
        assertParseFailure(parser, "friends", MESSAGE_CONSTRAINTS);

        // partial circle names
        assertParseFailure(parser, "cli", MESSAGE_CONSTRAINTS);
        assertParseFailure(parser, "pro", MESSAGE_CONSTRAINTS);
        assertParseFailure(parser, "fri", MESSAGE_CONSTRAINTS);

        // numeric values
        assertParseFailure(parser, "1", MESSAGE_CONSTRAINTS);
        assertParseFailure(parser, "123", MESSAGE_CONSTRAINTS);

        // special characters
        assertParseFailure(parser, "@client", MESSAGE_CONSTRAINTS);
        assertParseFailure(parser, "client!", MESSAGE_CONSTRAINTS);
        assertParseFailure(parser, "prospect-pro", MESSAGE_CONSTRAINTS);
        assertParseFailure(parser, "friend/buddy", MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_multipleArguments_failure() {
        // multiple circle names should fail
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, CircleFilterCommand.MESSAGE_USAGE);
        assertParseFailure(parser, "client prospect", expectedMessage);

        // three arguments
        assertParseFailure(parser, "client prospect friend", expectedMessage);

        // circle name with extra tokens
        assertParseFailure(parser, "client extra", expectedMessage);
    }

    @Test
    public void parse_circleNameWithWhitespace_failure() {
        // circle names with spaces inside (after trimming leading/trailing whitespace)
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, CircleFilterCommand.MESSAGE_USAGE);
        assertParseFailure(parser, "client prospect", expectedMessage);

        // tabs and newlines as separators
        assertParseFailure(parser, "client\tprospect", expectedMessage);
        assertParseFailure(parser, "client\nfriend", expectedMessage);
    }

    @Test
    public void parse_emptyStringAfterWhitespace_failure() {
        // trimmed empty string
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, CircleFilterCommand.MESSAGE_USAGE);
        assertParseFailure(parser, "   ", expectedMessage);
    }

    @Test
    public void parse_prefixLikeArguments_failure() {
        // arguments that look like prefixes are treated as invalid circle names
        assertParseFailure(parser, "c/client", MESSAGE_CONSTRAINTS);
        assertParseFailure(parser, "circle/client", MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_caseInsensitivity() {
        // lowercase
        assertParseSuccess(parser, "client", new CircleFilterCommand("client"));

        // uppercase (should be normalized to lowercase)
        assertParseSuccess(parser, "PROSPECT", new CircleFilterCommand("prospect"));

        // mixed case (should be normalized to lowercase)
        assertParseSuccess(parser, "FrIeNd", new CircleFilterCommand("friend"));
    }

    @Test
    public void parse_whitespaceHandling() {
        // leading whitespace (should be trimmed)
        assertParseSuccess(parser, "   client", new CircleFilterCommand("client"));

        // trailing whitespace (should be trimmed)
        assertParseSuccess(parser, "client   ", new CircleFilterCommand("client"));

        // both leading and trailing (should be trimmed)
        assertParseSuccess(parser, "   prospect   ", new CircleFilterCommand("prospect"));

        // tabs (should be trimmed)
        assertParseSuccess(parser, "\tclient\t", new CircleFilterCommand("client"));
    }

    @Test
    public void parse_invalidInputPatterns() {
        // circle name with numbers
        assertParseFailure(parser, "client1", MESSAGE_CONSTRAINTS);
        assertParseFailure(parser, "prospect2", MESSAGE_CONSTRAINTS);
        assertParseFailure(parser, "3friend", MESSAGE_CONSTRAINTS);

        // circle name with underscores
        assertParseFailure(parser, "client_new", MESSAGE_CONSTRAINTS);
        assertParseFailure(parser, "_friend", MESSAGE_CONSTRAINTS);

        // circle name with dots
        assertParseFailure(parser, "client.new", MESSAGE_CONSTRAINTS);
    }

}
