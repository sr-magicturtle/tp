package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.logic.parser.TagRemoveCommandParser.MESSAGE_REMOVE_EXCESSIVE_TAGS;
import static seedu.address.logic.parser.ParserUtil.MESSAGE_INVALID_INDEX;
import static seedu.address.logic.parser.TagRemoveCommandParser.MESSAGE_REMOVE_EXCESSIVE_TAGS;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalTags.CLASSMATE;
import static seedu.address.testutil.TypicalTags.FRIEND;
import static seedu.address.testutil.TypicalTags.HELLO_WORLD;
import static seedu.address.testutil.TypicalTags.MIXED_CASE;
import static seedu.address.testutil.TypicalTags.UPPERCASE;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.TagRemoveCommand;
import seedu.address.model.tag.Tag;

/**
 * Contains unit tests for TagRemoveCommandParser.
 */
public class TagRemoveCommandParserTest {

    private static final String INVALID_TAG_WITH_SPECIAL_CHAR = "bro-ther!";
    private static final String INVALID_TAG_WITH_SPACE = "bro ther";

    private TagRemoveCommandParser parser = new TagRemoveCommandParser();

    @Test
    public void parse_validArgs_success() {
        assertParseSuccess(parser, "1 t/friend",
                new TagRemoveCommand(INDEX_FIRST_PERSON, FRIEND));
    }

    @Test
    public void parse_validArgsSecondPerson_success() {
        assertParseSuccess(parser, "2 t/classmate",
                new TagRemoveCommand(INDEX_SECOND_PERSON, CLASSMATE));
    }

    @Test
    public void parse_validTagUppercase_success() {
        assertParseSuccess(parser, "1 t/" + UPPERCASE.tagName,
                new TagRemoveCommand(INDEX_FIRST_PERSON, UPPERCASE));
    }

    @Test
    public void parse_validTagMixedCase_success() {
        assertParseSuccess(parser, "1 t/" + MIXED_CASE.tagName,
                new TagRemoveCommand(INDEX_FIRST_PERSON, MIXED_CASE));
    }

    @Test
    public void parse_validArgsWithHyphen_success() {
        assertParseSuccess(parser, "1 t/hello-world",
            new TagRemoveCommand(INDEX_FIRST_PERSON, HELLO_WORLD));
    }

    @Test
    public void parse_extraPreamble_failure() {
        assertParseFailure(parser, "1 extra t/friend", MESSAGE_INVALID_INDEX);
    }

    @Test
    public void parse_invalidTagEmpty_failure() {
        assertParseFailure(parser, "1 t/", Tag.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_missingTag_failure() {
        assertParseFailure(parser, "1",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, TagRemoveCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_missingIndex_failure() {
        assertParseFailure(parser, "t/friend",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, TagRemoveCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_emptyArgs_failure() {
        assertParseFailure(parser, "",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, TagRemoveCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_multipleTags_failure() {
        assertParseFailure(parser, "1 t/friend t/classmate",
                MESSAGE_REMOVE_EXCESSIVE_TAGS);
    }

    @Test
    public void parse_invalidIndex_failure() {
        assertParseFailure(parser, "0 t/friend", MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void parse_nonNumericalIndex_failure() {
        assertParseFailure(parser, "abc t/friend",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, TagRemoveCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_invalidIndexNegative_failure() {
        assertParseFailure(parser, "-1 t/friend", MESSAGE_INVALID_INDEX);
    }

    @Test
    public void parse_invalidIndexNonNumeric_failure() {
        assertParseFailure(parser, "abc t/friend", MESSAGE_INVALID_INDEX);
    }

    @Test
    public void parse_invalidTagWithSpecialChar_failure() {
        assertParseFailure(parser, "1 t/" + INVALID_TAG_WITH_SPECIAL_CHAR, Tag.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_invalidTagWithSpace_failure() {
        assertParseFailure(parser, "1 t/" + INVALID_TAG_WITH_SPACE, Tag.MESSAGE_CONSTRAINTS);
    }
}
