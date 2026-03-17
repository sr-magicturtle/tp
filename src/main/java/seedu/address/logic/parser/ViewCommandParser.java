package seedu.address.logic.parser;

import static seedu.address.logic.parser.ParserUtil.parseIndex;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.ViewCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Represents a parser that parses input arguments to create a ViewCommand.
 *
 * Extracts the index from the user input and converts it into an Index
 * corresponding to the person in the currently displayed list.
 */
public class ViewCommandParser {

    /**
     * Parses the given arguments and returns a ViewCommand.
     *
     * @param args User input containing the index of the person to view.
     * @return ViewCommand containing the parsed Index.
     * @throws ParseException If the input does not contain a valid index.
     */
    public ViewCommand parse(String args) throws ParseException {
        Index index = parseIndex(args.trim());
        return new ViewCommand(index);
    }
}