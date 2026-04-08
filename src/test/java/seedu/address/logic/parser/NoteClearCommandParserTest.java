package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.NoteClearCommand;
import seedu.address.logic.parser.exceptions.ParseException;

public class NoteClearCommandParserTest {

    private final NoteClearCommandParser parser = new NoteClearCommandParser();

    @Test
    public void parse_validIndex_returnsNoteClearCommand() throws Exception {
        NoteClearCommand command = parser.parse("1");
        assertEquals(new NoteClearCommand(Index.fromOneBased(1)), command);
    }

    @Test
    public void parse_validIndexWithWhitespace_returnsNoteClearCommand() throws Exception {
        NoteClearCommand command = parser.parse("  2  ");
        assertEquals(new NoteClearCommand(Index.fromOneBased(2)), command);
    }

    @Test
    public void parse_largeValidIndex_returnsNoteClearCommand() throws Exception {
        NoteClearCommand command = parser.parse("99");
        assertEquals(new NoteClearCommand(Index.fromOneBased(99)), command);
    }

    @Test
    public void parse_emptyArgs_throwsParseException() {
        assertThrows(ParseException.class, () -> parser.parse(""),
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, NoteClearCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_whitespaceOnly_throwsParseException() {
        assertThrows(ParseException.class, () -> parser.parse("   "),
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, NoteClearCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_nonIntegerArg_throwsParseException() {
        assertThrows(ParseException.class, () -> parser.parse("abc"),
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, NoteClearCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_zeroIndex_throwsParseException() {
        // Index 0 is not valid (must be positive)
        assertThrows(ParseException.class, () -> parser.parse("0"));
    }

    @Test
    public void parse_negativeIndex_throwsParseException() {
        assertThrows(ParseException.class, () -> parser.parse("-1"));
    }

    @Test
    public void parse_multipleArgs_throwsParseException() {
        // Extra tokens after the index are not allowed
        assertThrows(ParseException.class, () -> parser.parse("1 2"),
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, NoteClearCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_indexWithExtraText_throwsParseException() {
        assertThrows(ParseException.class, () -> parser.parse("1 abc"),
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, NoteClearCommand.MESSAGE_USAGE));
    }
}
