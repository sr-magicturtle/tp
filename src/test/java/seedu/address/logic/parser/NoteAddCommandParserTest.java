package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.NoteAddCommand;

public class NoteAddCommandParserTest {
    private static final String INVALID_FORMAT_MESSAGE =
            String.format(MESSAGE_INVALID_COMMAND_FORMAT, NoteAddCommand.MESSAGE_USAGE);
    private final NoteAddCommandParser parser = new NoteAddCommandParser();

    @Test
    public void parse_validArgs_success() {
        assertParseSuccess(parser, "1: Met at career fair",
                new NoteAddCommand(INDEX_FIRST_PERSON, "Met at career fair"));
    }

    @Test
    public void parse_leadingTrailingWhitespace_success() {
        assertParseSuccess(parser, "  1: Met at career fair  ",
                new NoteAddCommand(INDEX_FIRST_PERSON, "Met at career fair"));
    }

    @Test
    public void parse_noteTextWithColons_success() {
        // Colons after the first ": " should be treated as note content
        assertParseSuccess(parser, "1: Note with: extra: colons",
                new NoteAddCommand(INDEX_FIRST_PERSON, "Note with: extra: colons"));
    }

    @Test
    public void parse_exactlyMaxWords_success() {
        String note = "word ".repeat(NoteAddCommandParser.MAX_WORD_COUNT).trim();
        assertParseSuccess(parser, "1: " + note,
                new NoteAddCommand(INDEX_FIRST_PERSON, note));
    }

    @Test
    public void parse_missingColon_throwsParseException() {
        // No ": " separator
        assertParseFailure(parser, "1 Met at career fair", INVALID_FORMAT_MESSAGE);
    }

    @Test
    public void parse_emptyNoteText_throwsParseException() {
        // Separator present but nothing after it
        assertParseFailure(parser, "1: ", INVALID_FORMAT_MESSAGE);
    }

    @Test
    public void parse_emptyArgs_throwsParseException() {
        assertParseFailure(parser, "", INVALID_FORMAT_MESSAGE);
    }

    @Test
    public void parse_invalidIndex_throwsParseException() {
        assertParseFailure(parser, "0: Some note", INVALID_FORMAT_MESSAGE);
        assertParseFailure(parser, "-1: Some note", INVALID_FORMAT_MESSAGE);
        assertParseFailure(parser, "abc: Some note", INVALID_FORMAT_MESSAGE);
    }

    @Test
    public void parse_wordLimitExceeded_throwsParseException() {
        String tooLong = "word ".repeat(NoteAddCommandParser.MAX_WORD_COUNT + 1).trim();
        assertParseFailure(parser, "1: " + tooLong,
                NoteAddCommandParser.MESSAGE_WORD_LIMIT_EXCEEDED);
    }
}
