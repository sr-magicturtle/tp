package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NOTE;
import static seedu.address.model.person.Note.MAX_CHAR_COUNT;
import static seedu.address.model.person.Note.MESSAGE_CHAR_LIMIT_EXCEEDED;
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
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_NOTE);

        // Validate index
        Index index;
        try {
            index = ParserUtil.parseIndex(argMultimap.getPreamble());
        } catch (ParseException pe) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, NoteAddCommand.MESSAGE_USAGE), pe);
        }

        // Validate note/ prefix is present
        if (argMultimap.getValue(PREFIX_NOTE).isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, NoteAddCommand.MESSAGE_USAGE));
        }

        String noteText = argMultimap.getValue(PREFIX_NOTE).get().trim();

        // Validate note is not empty
        if (noteText.isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_CONSTRAINTS, NoteAddCommand.MESSAGE_USAGE));
        }

        // Validate the word count of the new input is <= MAX_CHAR_COUNT
        // Validating the combined total word count is inside NoteAddCommand.execute()
        if (noteText.length() > MAX_CHAR_COUNT) {
            throw new ParseException(MESSAGE_CHAR_LIMIT_EXCEEDED);
        }

        return new NoteAddCommand(index, new Note(noteText));
    }
}
