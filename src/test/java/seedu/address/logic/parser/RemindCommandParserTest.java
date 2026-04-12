package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.RemindCommand;

public class RemindCommandParserTest {

    private final RemindCommandParser parser = new RemindCommandParser();

    @Test
    public void parse_validArgs_returnsRemindCommand() {
        assertParseSuccess(parser, "3", new RemindCommand(3));
    }

    @Test
    public void parse_emptyArgs_failure() {
        assertParseFailure(parser, "",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, RemindCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_nonNumericArgs_failure() {
        assertParseFailure(parser, "abc", RemindCommand.MESSAGE_INVALID_DAYS);
    }

    @Test
    public void parse_zero_failure() {
        assertParseFailure(parser, "0", RemindCommand.MESSAGE_INVALID_DAYS);
    }

    @Test
    public void parse_negative_failure() {
        assertParseFailure(parser, "-1", RemindCommand.MESSAGE_INVALID_DAYS);
    }

    @Test
    public void parse_excessivelyLargePositive_failure() {
        assertParseFailure(parser, "99999999999999999999", RemindCommand.MESSAGE_INVALID_DAYS);
    }

    @Test
    public void parse_integerOverflow_failure() {
        assertParseFailure(parser, "2147483648", RemindCommand.MESSAGE_INVALID_DAYS);
    }
}
