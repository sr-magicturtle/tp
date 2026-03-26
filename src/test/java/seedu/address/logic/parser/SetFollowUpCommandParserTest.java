package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.SetFollowUpCommand;
import seedu.address.model.person.FollowUpDate;

public class SetFollowUpCommandParserTest {

    private final SetFollowUpCommandParser parser = new SetFollowUpCommandParser();

    @Test
    public void parse_validArgs_returnsSetFollowUpCommand() {
        assertParseSuccess(parser, "1 d/2099-12-31",
                new SetFollowUpCommand(Index.fromOneBased(1), new FollowUpDate("2099-12-31")));
    }

    @Test
    public void parse_missingDate_failure() {
        assertParseFailure(parser, "1",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, SetFollowUpCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_missingIndex_failure() {
        assertParseFailure(parser, "d/2099-12-31",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, SetFollowUpCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_invalidDate_failure() {
        assertParseFailure(parser, "1 d/abc",
                FollowUpDate.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_pastDate_failure() {
        assertParseFailure(parser, "1 d/2020-01-01",
                "Follow up date cannot be before today.");
    }
}
