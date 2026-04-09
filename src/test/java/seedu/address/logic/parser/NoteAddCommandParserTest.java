package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.model.person.Note.MAX_CHAR_COUNT;
import static seedu.address.model.person.Note.MESSAGE_CHAR_LIMIT_EXCEEDED;
import static seedu.address.model.person.Note.MESSAGE_CONSTRAINTS;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import org.junit.jupiter.api.Test;

import seedu.address.logic.Messages;
import seedu.address.logic.commands.NoteAddCommand;
import seedu.address.model.person.Note;

public class NoteAddCommandParserTest {
    private static final String INVALID_FORMAT_MESSAGE =
            String.format(MESSAGE_INVALID_COMMAND_FORMAT, NoteAddCommand.MESSAGE_USAGE);
    private final NoteAddCommandParser parser = new NoteAddCommandParser();

    @Test
    public void parse_validArgs_success() {
        assertParseSuccess(parser, "1 note/Met at career fair",
                new NoteAddCommand(INDEX_FIRST_PERSON, new Note("Met at career fair")));
    }

    @Test
    public void parse_leadingTrailingWhitespace_success() {
        assertParseSuccess(parser, "  1 note/Met at career fair  ",
                new NoteAddCommand(INDEX_FIRST_PERSON, new Note("Met at career fair")));
    }

    @Test
    public void parse_noteTextWithColons_success() {
        assertParseSuccess(parser, "1 note/Note with: extra: colons",
                new NoteAddCommand(INDEX_FIRST_PERSON, new Note("Note with: extra: colons")));
    }

    @Test
    public void parse_exactlyMaxWords_success() {
        String noteText = "w".repeat(MAX_CHAR_COUNT).trim();
        assertParseSuccess(parser, "1 note/" + noteText,
                new NoteAddCommand(INDEX_FIRST_PERSON, new Note(noteText)));
    }

    @Test
    public void parse_missingColon_throwsParseException() {
        // No ": " separator
        assertParseFailure(parser, "1 Met at career fair", INVALID_FORMAT_MESSAGE);
    }

    @Test
    public void parse_emptyNoteText_throwsParseException() {
        // Separator present but nothing after it
        assertParseFailure(parser, "1 note/", MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_emptyArgs_throwsParseException() {
        assertParseFailure(parser, "", INVALID_FORMAT_MESSAGE);
    }

    @Test
    public void parse_nonPositiveIndex_throwsParseException() {
        assertParseFailure(parser, "0 note/Some note", Messages.MESSAGE_OOR_INDEX);
        assertParseFailure(parser, "-1 note/Some note", Messages.MESSAGE_OOR_INDEX);
    }

    @Test
    public void parse_nonNumericIndex_throwsParseException() {
        assertParseFailure(parser, "abc note/Some note", INVALID_FORMAT_MESSAGE);
    }

    @Test
    public void parse_wordLimitExceeded_throwsParseException() {
        String tooLong = "w".repeat(MAX_CHAR_COUNT + 1).trim();
        assertParseFailure(parser, "1 note/" + tooLong,
                MESSAGE_CHAR_LIMIT_EXCEEDED);
    }

    @Test
    public void parse_excessivelyLargePositiveIndex_throwsParseException() {
        assertParseFailure(parser, "99999999999999999999 note/Some note", INVALID_FORMAT_MESSAGE);
    }

    @Test
    public void parse_excessivelyLargeNegativeIndex_throwsParseException() {
        assertParseFailure(parser, "-99999999999999999999 note/Some note", INVALID_FORMAT_MESSAGE);
    }

    @Test
    public void parse_integerOverflowIndex_throwsParseException() {
        assertParseFailure(parser, "2147483648 note/Some note", Messages.MESSAGE_OOR_INDEX);
    }
}
