package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_DUPLICATE_FIELDS;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CIRCLE;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.CircleAddCommand;
import seedu.address.model.circle.Circle;

public class CircleAddCommandParserTest {

    private final CircleAddCommandParser parser = new CircleAddCommandParser();

    @Test
    public void parse_validArgs_success() {
        // valid arguments with lowercase circle name
        Circle expectedCircle = new Circle("client");
        Index expectedIndex = Index.fromOneBased(1);
        CircleAddCommand expectedCommand = new CircleAddCommand(expectedIndex, expectedCircle);

        assertParseSuccess(parser, "1 c/client", expectedCommand);

        // valid arguments with uppercase circle name (should be normalized)
        Circle expectedCircleUpper = new Circle("CLIENT");
        CircleAddCommand expectedCommandUpper = new CircleAddCommand(expectedIndex, expectedCircleUpper);
        assertParseSuccess(parser, "1 c/CLIENT", expectedCommandUpper);

        // valid arguments with mixed case
        Circle expectedCircleMixed = new Circle("ProSpect");
        Index expectedIndex2 = Index.fromOneBased(2);
        CircleAddCommand expectedCommandMixed = new CircleAddCommand(expectedIndex2, expectedCircleMixed);
        assertParseSuccess(parser, "2 c/ProSpect", expectedCommandMixed);

        // valid arguments with leading/trailing whitespace
        Circle expectedCircleFriend = new Circle("friend");
        CircleAddCommand expectedCommandFriend = new CircleAddCommand(expectedIndex, expectedCircleFriend);
        assertParseSuccess(parser, "   1   c/friend   ", expectedCommandFriend);
    }

    @Test
    public void parse_missingIndex_failure() {
        // no index provided
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, CircleAddCommand.MESSAGE_USAGE);
        assertParseFailure(parser, "c/client", expectedMessage);

        // empty input
        assertParseFailure(parser, "", expectedMessage);
    }

    @Test
    public void parse_missingCirclePrefix_failure() {
        // no c/ provided at all
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, CircleAddCommand.MESSAGE_USAGE);
        assertParseFailure(parser, "1", expectedMessage);

        // index only with whitespace
        assertParseFailure(parser, "1    ", expectedMessage);
    }

    @Test
    public void parse_multipleCirclePrefixes_failure() {
        // more than one c/ should fail with duplicate prefix error
        String expectedMessage = MESSAGE_DUPLICATE_FIELDS + PREFIX_CIRCLE;
        assertParseFailure(parser, "1 c/client c/prospect", expectedMessage);
    }

    @Test
    public void parse_indexOutOfRange_failure() {
        // zero index
        assertParseFailure(parser, "0 c/client", Messages.MESSAGE_OOR_INDEX);

        // negative index
        assertParseFailure(parser, "-1 c/client", Messages.MESSAGE_OOR_INDEX);
    }

    @Test
    public void parse_nonNumericalIndex_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, CircleAddCommand.MESSAGE_USAGE);

        // non-integer index
        assertParseFailure(parser, "abc c/client", expectedMessage);

        // decimal index
        assertParseFailure(parser, "1.5 c/client", expectedMessage);

        // very large index (beyond integer range)
        assertParseFailure(parser, "99999999999999999999 c/client", expectedMessage);
    }

    @Test
    public void parse_invalidCircleName_failure() {
        // invalid circle name (not one of client, prospect, friend)
        assertParseFailure(parser, "1 c/invalid", Circle.MESSAGE_CONSTRAINTS);

        // misspelled circle name
        assertParseFailure(parser, "1 c/clients", Circle.MESSAGE_CONSTRAINTS);
        assertParseFailure(parser, "1 c/prospects", Circle.MESSAGE_CONSTRAINTS);
        assertParseFailure(parser, "1 c/friends", Circle.MESSAGE_CONSTRAINTS);

        // partial circle name
        assertParseFailure(parser, "1 c/cli", Circle.MESSAGE_CONSTRAINTS);
        assertParseFailure(parser, "1 c/pro", Circle.MESSAGE_CONSTRAINTS);

        // numeric value
        assertParseFailure(parser, "1 c/123", Circle.MESSAGE_CONSTRAINTS);

        // special characters
        assertParseFailure(parser, "1 c/@client", Circle.MESSAGE_CONSTRAINTS);
        assertParseFailure(parser, "1 c/client!", Circle.MESSAGE_CONSTRAINTS);
        assertParseFailure(parser, "1 c/cli-ent", Circle.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_emptyCircleValue_failure() {
        // prefix is present but empty value - fails circle validation
        assertParseFailure(parser, "1 c/", Circle.MESSAGE_CONSTRAINTS);

        // whitespace only after prefix - also fails circle validation
        assertParseFailure(parser, "1 c/   ", Circle.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_unknownPrefix_failure() {
        // unknown prefix in arguments
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, CircleAddCommand.MESSAGE_USAGE);
        assertParseFailure(parser, "1 x/client", expectedMessage);

        // unknown prefix before circle prefix
        assertParseFailure(parser, "1 t/tag c/client", expectedMessage);
    }

    @Test
    public void parse_multipleIndexes_failure() {
        // multiple indexes before circle prefix (treated as preamble issue)
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, CircleAddCommand.MESSAGE_USAGE);
        assertParseFailure(parser, "1 2 c/client", expectedMessage);
    }

    @Test
    public void parse_indexWithAlphabeticCharacters_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, CircleAddCommand.MESSAGE_USAGE);
        // index with letters mixed in
        assertParseFailure(parser, "1a c/client", expectedMessage);
        assertParseFailure(parser, "a1 c/client", expectedMessage);
    }

    @Test
    public void parse_extraTokensAfterCircle_failure() {
        // extra text after circle name
        assertParseFailure(parser, "1 c/client extra", Circle.MESSAGE_CONSTRAINTS);

        assertParseFailure(parser, "1 c/client 34extra", Circle.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_validEdgeCases() {
        // index at boundary
        Circle expectedCircle = new Circle("friend");
        Index expectedIndex = Index.fromOneBased(Integer.MAX_VALUE - 1);
        CircleAddCommand expectedCommand = new CircleAddCommand(expectedIndex, expectedCircle);

        // This should successfully parse even though the index is very large
        // (whether it's valid in the model is a separate concern)
        assertParseSuccess(parser, String.valueOf(Integer.MAX_VALUE - 1) + " c/friend", expectedCommand);
    }

}
