package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.Messages.MESSAGE_OOR_INDEX;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.logic.parser.TagAddCommandParser.MESSAGE_ADD_EXCESSIVE_TAGS;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalTags.CLASSMATE;
import static seedu.address.testutil.TypicalTags.FRIEND;
import static seedu.address.testutil.TypicalTags.HELLO_WORLD;
import static seedu.address.testutil.TypicalTags.MIXED_CASE;
import static seedu.address.testutil.TypicalTags.UPPERCASE;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.TagAddCommand;
import seedu.address.model.tag.Tag;

/**
 * Contains unit tests for TagAddCommandParser.
 */
public class TagAddCommandParserTest {

    private static final String INVALID_TAG_WITH_SPECIAL_CHAR = "@@@";
    private static final String INVALID_TAG_WITH_SPACE = "bro ther";

    private final TagAddCommandParser parser = new TagAddCommandParser();

    @Test
    public void parse_validArgs_success() {
        TagAddCommand expectedCommand = new TagAddCommand(INDEX_FIRST_PERSON, FRIEND);

        assertParseSuccess(parser, "1 t/friend", expectedCommand);
        assertParseSuccess(parser, "   1   t/friend   ", expectedCommand);
    }

    @Test
    public void parse_validArgsWithHyphen_success() {
        TagAddCommand hyphenCommand = new TagAddCommand(INDEX_FIRST_PERSON, HELLO_WORLD);
        assertParseSuccess(parser, "1 t/hello-world", hyphenCommand);
    }

    @Test
    public void parse_validArgsSecondPerson_success() {
        TagAddCommand expectedCommand = new TagAddCommand(INDEX_SECOND_PERSON, CLASSMATE);

        assertParseSuccess(parser, "2 t/classmate", expectedCommand);
    }

    @Test
    public void parse_validTagUppercase_success() {
        TagAddCommand expectedCommand = new TagAddCommand(INDEX_FIRST_PERSON, UPPERCASE);
        assertParseSuccess(parser, "1 t/" + UPPERCASE.tagName, expectedCommand);
    }

    @Test
    public void parse_validTagMixedCase_success() {
        TagAddCommand expectedCommand = new TagAddCommand(INDEX_FIRST_PERSON, MIXED_CASE);
        assertParseSuccess(parser, "1 t/" + MIXED_CASE.tagName, expectedCommand);
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
    }

    @Test
    public void parse_emptyArgs_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, TagAddCommand.MESSAGE_USAGE);
        assertParseFailure(parser, "", expectedMessage);
    }

    @Test
    public void parse_multipleTags_failure() {
        assertParseFailure(parser, "1 t/friend t/enemy", MESSAGE_ADD_EXCESSIVE_TAGS);
    }

    @Test
    public void parse_invalidIndex_failure() {
        assertParseFailure(parser, "0 t/friend", MESSAGE_OOR_INDEX);
        assertParseFailure(parser, "-1 t/friend", MESSAGE_OOR_INDEX);
    }

    @Test
    public void parse_nonNumericIndex_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, TagAddCommand.MESSAGE_USAGE);
        assertParseFailure(parser, "abc t/friend", expectedMessage);
        assertParseFailure(parser, "1 extra t/friend", expectedMessage);
    }

    @Test
    public void parse_invalidTagWithSpecialChar_failure() {
        assertParseFailure(parser, "1 t/" + INVALID_TAG_WITH_SPECIAL_CHAR, Tag.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_invalidTagWithSpace_failure() {
        assertParseFailure(parser, "1 t/" + INVALID_TAG_WITH_SPACE, Tag.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_invalidTagEmpty_failure() {
        assertParseFailure(parser, "1 t/", Tag.MESSAGE_CONSTRAINTS);
    }
}
