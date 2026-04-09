package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CIRCLE;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.CircleAddCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.circle.Circle;

/**
 * Parses input arguments and creates a new CircleAddCommand object.
 */
public class CircleAddCommandParser implements Parser<CircleAddCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the CircleAddCommand
     * and returns a CircleAddCommand object for execution.
     *
     * @throws ParseException if the user input does not conform to the expected format
     */
    public CircleAddCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_CIRCLE);

        // Ensure the circle prefix is present
        if (!argMultimap.getValue(PREFIX_CIRCLE).isPresent()
            || argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                CircleAddCommand.MESSAGE_USAGE));
        }

        // Ensure no duplicate prefixes
        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_CIRCLE);

        Index index = ParserUtil.parseIndex(
                argMultimap.getPreamble(), CircleAddCommand.MESSAGE_USAGE);

        // Parse and validate circle name
        String circleName = argMultimap.getValue(PREFIX_CIRCLE).get().trim();
        if (!Circle.isValidCircleName(circleName)) {
            throw new ParseException(Circle.MESSAGE_CONSTRAINTS);
        }

        Circle circle = new Circle(circleName);
        return new CircleAddCommand(index, circle);
    }
}
