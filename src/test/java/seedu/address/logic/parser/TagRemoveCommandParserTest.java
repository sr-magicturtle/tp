package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.logic.parser.TagRemoveCommandParser.MESSAGE_REMOVE_EXCESSIVE_TAGS;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.TagRemoveCommand;
import seedu.address.model.tag.Tag;

public class TagRemoveCommandParserTest {

    @Test
    public void parse_validArgs_success() {
        TagRemoveCommandParser parser = new TagRemoveCommandParser();
        assertParseSuccess(parser, "1 t/friend",
                new TagRemoveCommand(new Tag("friend"), Index.fromOneBased(1)));
    }

    @Test
    public void parse_missingTag_failure() {
        TagRemoveCommandParser parser = new TagRemoveCommandParser();
        assertParseFailure(parser, "1",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, TagRemoveCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_missingIndex_failure() {
        TagRemoveCommandParser parser = new TagRemoveCommandParser();
        assertParseFailure(parser, "t/friend",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, TagRemoveCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_multipleTags_failure() {
        TagRemoveCommandParser parser = new TagRemoveCommandParser();
        assertParseFailure(parser, "1 t/friend t/classmate",
                MESSAGE_REMOVE_EXCESSIVE_TAGS);
    }

    @Test
    public void parse_invalidIndex_failure() {
        TagRemoveCommandParser parser = new TagRemoveCommandParser();
        assertParseFailure(parser, "0 t/friend", MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void parse_nonNumericalIndex_failure() {
        TagRemoveCommandParser parser = new TagRemoveCommandParser();
        assertParseFailure(parser, "abc t/friend",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, TagRemoveCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_invalidTag_failure() {
        TagRemoveCommandParser parser = new TagRemoveCommandParser();
        assertParseFailure(parser, "1 t/INVALID!", Tag.MESSAGE_CONSTRAINTS);
        assertParseFailure(parser, "1 t/Hello", Tag.MESSAGE_CONSTRAINTS);
        assertParseFailure(parser, "1 t/bro ther", Tag.MESSAGE_CONSTRAINTS);
    }
}
