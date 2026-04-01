package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.Messages.MESSAGE_UNKNOWN_COMMAND;

import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import seedu.address.commons.core.LogsCenter;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.AddCommand;
import seedu.address.logic.commands.CircleAddCommand;
import seedu.address.logic.commands.CircleFilterCommand;
import seedu.address.logic.commands.CircleRemoveCommand;
import seedu.address.logic.commands.ClearCommand;
import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.DeleteCommand;
import seedu.address.logic.commands.EditCommand;
import seedu.address.logic.commands.ExitCommand;
import seedu.address.logic.commands.FindCommand;
import seedu.address.logic.commands.FollowUpClearCommand;
import seedu.address.logic.commands.HelpCommand;
import seedu.address.logic.commands.ListCommand;
import seedu.address.logic.commands.NoteAddCommand;
import seedu.address.logic.commands.NoteClearCommand;
import seedu.address.logic.commands.RemindCommand;
import seedu.address.logic.commands.SetFollowUpCommand;
import seedu.address.logic.commands.TagAddCommand;
import seedu.address.logic.commands.TagRemoveCommand;
import seedu.address.logic.commands.ViewCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses user input.
 */
public class AddressBookParser {

    /**
     * Used for initial separation of command word and args.
     */
    private static final Pattern BASIC_COMMAND_FORMAT = Pattern.compile("(?<commandWord>\\S+)(?<arguments>.*)");
    private static final Logger logger = LogsCenter.getLogger(AddressBookParser.class);

    /**
     * Parses user input into command for execution.
     *
     * @param userInput full user input string
     * @return the command based on the user input
     * @throws ParseException if the user input does not conform the expected format
     */
    public Command parseCommand(String userInput) throws ParseException {
        final Matcher matcher = BASIC_COMMAND_FORMAT.matcher(userInput.trim());
        if (!matcher.matches()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE));
        }

        final String commandWord = matcher.group("commandWord");
        final String arguments = matcher.group("arguments");

        // Note to developers: Change the log level in config.json to enable lower level (i.e., FINE, FINER and lower)
        // log messages such as the one below.
        // Lower level log messages are used sparingly to minimize noise in the code.
        logger.fine("Command word: " + commandWord + "; Arguments: " + arguments);

        switch (commandWord) {

        case AddCommand.COMMAND_WORD:
            return new AddCommandParser().parse(arguments);

        case EditCommand.COMMAND_WORD:
            return new EditCommandParser().parse(arguments);

        case DeleteCommand.COMMAND_WORD:
            return new DeleteCommandParser().parse(arguments);

        case ClearCommand.COMMAND_WORD:
            return new ClearCommand();

        case FindCommand.COMMAND_WORD:
            return new FindCommandParser().parse(arguments);

        case ListCommand.COMMAND_WORD:
            return new ListCommand();

        case ExitCommand.COMMAND_WORD:
            return new ExitCommand();

        case HelpCommand.COMMAND_WORD:
            return new HelpCommand();

        case TagAddCommand.COMMAND_WORD:
            return new TagAddCommandParser().parse(arguments);

        case TagRemoveCommand.COMMAND_WORD:
            return new TagRemoveCommandParser().parse(arguments);

        case ViewCommand.COMMAND_WORD:
            return new ViewCommandParser().parse(arguments);

        case SetFollowUpCommand.COMMAND_WORD:
            return new SetFollowUpCommandParser().parse(arguments);

        case RemindCommand.COMMAND_WORD:
            return new RemindCommandParser().parse(arguments);

        case NoteAddCommand.COMMAND_WORD:
            return new NoteAddCommandParser().parse(arguments);

        case NoteClearCommand.COMMAND_WORD:
            try {
                return new NoteClearCommand(ParserUtil.parseIndex(arguments.trim()));
            } catch (ParseException pe) {
                throw new ParseException(
                        String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT,
                                NoteClearCommand.MESSAGE_USAGE), pe);
            }

        case CircleAddCommand.COMMAND_WORD:
            return new CircleAddCommandParser().parse(arguments);

        case CircleRemoveCommand.COMMAND_WORD:
            return new CircleRemoveCommandParser().parse(arguments);

        case CircleFilterCommand.COMMAND_WORD:
            return new CircleFilterCommandParser().parse(arguments);

        case FollowUpClearCommand.COMMAND_WORD:
            return new FollowUpClearCommandParser().parse(arguments);

        default:
            logger.finer("This user input caused a ParseException: " + userInput);
            throw new ParseException(MESSAGE_UNKNOWN_COMMAND);
        }
    }

}
