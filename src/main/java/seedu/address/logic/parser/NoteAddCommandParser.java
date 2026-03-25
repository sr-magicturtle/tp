package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.NoteAddCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new NoteAddCommand object.
 * Expected format: INDEX: NOTE_TEXT
 * e.g. note/1: Met at career fair
 */
public class NoteAddCommandParser implements Parser<NoteAddCommand> {

    public static final int MAX_WORD_COUNT = 200;
    public static final String MESSAGE_WORD_LIMIT_EXCEEDED =
            "Note exceeds the maximum word count of " + MAX_WORD_COUNT + " words.";

    @Override
    public NoteAddCommand parse(String args) throws ParseException {
        if (args == null) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, NoteAddCommand.MESSAGE_USAGE));
        }
        String trimmed = args.trim();

        int separatorIndex = trimmed.indexOf(": ");
        if (separatorIndex <= 0) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, NoteAddCommand.MESSAGE_USAGE));
        }

        String indexPart = trimmed.substring(0, separatorIndex).trim();
        String noteText = trimmed.substring(separatorIndex + 2).trim();

        if (noteText.isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, NoteAddCommand.MESSAGE_USAGE));
        }

        String[] words = noteText.split("\\s+");
        if (words.length == 0 || (words.length == 1 && words[0].isEmpty())) {
            throw new ParseException("Note text cannot be empty.");
        }
        if (words.length > MAX_WORD_COUNT) {
            throw new ParseException(MESSAGE_WORD_LIMIT_EXCEEDED);
        }

        Index index;
        try {
            index = ParserUtil.parseIndex(indexPart);
        } catch (ParseException pe) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, NoteAddCommand.MESSAGE_USAGE), pe);
        }

        return new NoteAddCommand(index, noteText);
    }
}
