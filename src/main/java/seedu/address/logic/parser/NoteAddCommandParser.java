package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.commands.NoteAddCommand.MESSAGE_INVALID_INDEX;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NOTE;
import static seedu.address.model.person.Note.MESSAGE_CONSTRAINTS;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.NoteAddCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Note;

/**
 * Parses input arguments and creates a new NoteAddCommand object.
 * Expected format: INDEX note/NOTE_TEXT
 * Example: 1 note/Looking for family coverage
 */
public class NoteAddCommandParser implements Parser<NoteAddCommand> {

    @Override
    public NoteAddCommand parse(String args) throws ParseException {
        requireNonNull(args);

        int notePrefixPosition = args.indexOf(PREFIX_NOTE.toString());

        // Verify the presence of "note/" first
        if (notePrefixPosition == -1) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, NoteAddCommand.MESSAGE_USAGE));
        }

        // preamble is everything before "note/"
        // noteText is everything after "note/"
        String preamble = args.substring(0, notePrefixPosition).trim();
        String noteText = args.substring(notePrefixPosition + PREFIX_NOTE.toString().length()).trim();

        // Then validate the index
        int rawInt;
        try {
            rawInt = Integer.parseInt(preamble);
        } catch (NumberFormatException e) {
            throw new ParseException(
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, NoteAddCommand.MESSAGE_USAGE));
        }

        // if index is present but out of range
        if (rawInt <= 0) {
            throw new ParseException(MESSAGE_INVALID_INDEX);
        }

        // Validate note is not empty
        if (noteText.isEmpty()) {
            throw new ParseException(MESSAGE_CONSTRAINTS);
        }

        Index index = Index.fromOneBased(rawInt);

        // Validate the word count of the new input is <= MAX_CHAR_COUNT
        // Validating the combined total word count is inside NoteAddCommand.execute()
        try {
            return new NoteAddCommand(index, new Note(noteText));
        } catch (IllegalArgumentException e) {
            throw new ParseException(e.getMessage());
        }
    }
}
