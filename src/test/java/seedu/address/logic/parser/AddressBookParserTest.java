package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.Messages.MESSAGE_UNKNOWN_COMMAND;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalCircles.FRIENDS;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.AddCommand;
import seedu.address.logic.commands.CircleAddCommand;
import seedu.address.logic.commands.CircleFilterCommand;
import seedu.address.logic.commands.CircleRemoveCommand;
import seedu.address.logic.commands.ClearCommand;
import seedu.address.logic.commands.DeleteCommand;
import seedu.address.logic.commands.EditCommand;
import seedu.address.logic.commands.EditCommand.EditPersonDescriptor;
import seedu.address.logic.commands.ExitCommand;
import seedu.address.logic.commands.FindCommand;
import seedu.address.logic.commands.HelpCommand;
import seedu.address.logic.commands.ListCommand;
import seedu.address.logic.commands.RemindCommand;
import seedu.address.logic.commands.SetFollowUpCommand;
import seedu.address.logic.commands.TagAddCommand;
import seedu.address.logic.commands.TagRemoveCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.FollowUpDate;
import seedu.address.model.person.NameContainsKeywordsPredicate;
import seedu.address.model.person.Person;
import seedu.address.model.tag.Tag;
import seedu.address.testutil.EditPersonDescriptorBuilder;
import seedu.address.testutil.PersonBuilder;
import seedu.address.testutil.PersonUtil;
import seedu.address.ui.ConfirmationInterface;

public class AddressBookParserTest {

    private final ConfirmationInterface alwaysConfirm = prompt -> true;
    private final AddressBookParser parser = new AddressBookParser();

    @Test
    public void parseCommand_add() throws Exception {
        Person person = new PersonBuilder().build();
        AddCommand command = (AddCommand) parser.parseCommand(PersonUtil.getAddCommand(person));
        assertEquals(new AddCommand(person), command);
    }

    @Test
    public void parseCommand_clear() throws Exception {
        assertTrue(parser.parseCommand(ClearCommand.COMMAND_WORD) instanceof ClearCommand);
        assertTrue(parser.parseCommand(ClearCommand.COMMAND_WORD + " 3") instanceof ClearCommand);
    }

    @Test
    public void parseCommand_delete() throws Exception {
        DeleteCommand command = (DeleteCommand) parser.parseCommand(
            DeleteCommand.COMMAND_WORD + " " + INDEX_FIRST_PERSON.getOneBased());
        assertEquals(new DeleteCommand(INDEX_FIRST_PERSON, alwaysConfirm), command);
    }

    @Test
    public void parseCommand_edit() throws Exception {
        Person person = new PersonBuilder().build();
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder(person).build();
        EditCommand command = (EditCommand) parser.parseCommand(EditCommand.COMMAND_WORD + " "
            + INDEX_FIRST_PERSON.getOneBased() + " " + PersonUtil.getEditPersonDescriptorDetails(descriptor));
        assertEquals(new EditCommand(INDEX_FIRST_PERSON, descriptor), command);
    }

    @Test
    public void parseCommand_exit() throws Exception {
        assertTrue(parser.parseCommand(ExitCommand.COMMAND_WORD) instanceof ExitCommand);
        assertTrue(parser.parseCommand(ExitCommand.COMMAND_WORD + " 3") instanceof ExitCommand);
    }

    @Test
    public void parseCommand_find() throws Exception {
        List<String> keywords = Arrays.asList("foo", "bar", "baz");
        FindCommand command = (FindCommand) parser.parseCommand(
            FindCommand.COMMAND_WORD + " " + keywords.stream().collect(Collectors.joining(" ")));
        assertEquals(new FindCommand(new NameContainsKeywordsPredicate(keywords)), command);
    }

    @Test
    public void parseCommand_help() throws Exception {
        assertTrue(parser.parseCommand(HelpCommand.COMMAND_WORD) instanceof HelpCommand);
        assertTrue(parser.parseCommand(HelpCommand.COMMAND_WORD + " 3") instanceof HelpCommand);
    }

    @Test
    public void parseCommand_list() throws Exception {
        assertTrue(parser.parseCommand(ListCommand.COMMAND_WORD) instanceof ListCommand);
        assertTrue(parser.parseCommand(ListCommand.COMMAND_WORD + " 3") instanceof ListCommand);
    }

    @Test
    public void parseCommand_unrecognisedInput_throwsParseException() {
        assertThrows(ParseException.class, String.format(MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE), ()
            -> parser.parseCommand(""));
    }

    @Test
    public void parseCommand_unknownCommand_throwsParseException() {
        assertThrows(ParseException.class, MESSAGE_UNKNOWN_COMMAND, () -> parser.parseCommand("unknownCommand"));
    }

    @Test
    public void parseCommand_followup() throws Exception {
        SetFollowUpCommand command = (SetFollowUpCommand) parser.parseCommand(
            SetFollowUpCommand.COMMAND_WORD + " 1 d/2099-12-31");
        assertEquals(new SetFollowUpCommand(Index.fromOneBased(1), new FollowUpDate("2099-12-31")), command);
    }

    @Test
    public void parseCommand_remind() throws Exception {
        RemindCommand command = (RemindCommand) parser.parseCommand(
            RemindCommand.COMMAND_WORD + " 3");
        assertEquals(new RemindCommand(3), command);
    }

    @Test
    public void parseCommand_circleadd() throws Exception {
        CircleAddCommand command = (CircleAddCommand) parser.parseCommand(
            CircleAddCommand.COMMAND_WORD + " "
                + INDEX_FIRST_PERSON.getOneBased() + " c/" + FRIENDS.getCircleName());
        assertEquals(new CircleAddCommand(INDEX_FIRST_PERSON, FRIENDS), command);
    }

    @Test
    public void parseCommand_circlerm() throws Exception {
        CircleRemoveCommand command = (CircleRemoveCommand) parser.parseCommand(
            CircleRemoveCommand.COMMAND_WORD + " "
            + INDEX_FIRST_PERSON.getOneBased());
        assertEquals(new CircleRemoveCommand(INDEX_FIRST_PERSON), command);
    }

    @Test
    public void parseCommand_circlefilter() throws Exception {
        CircleFilterCommand command = (CircleFilterCommand) parser.parseCommand(
            CircleFilterCommand.COMMAND_WORD + " " + FRIENDS.getCircleName());
        assertEquals(new CircleFilterCommand(FRIENDS.getCircleName()), command);
    }

    @Test
    public void parseCommand_tagadd() throws Exception {
        TagAddCommand command = (TagAddCommand) parser.parseCommand(
            TagAddCommand.COMMAND_WORD + " "
            + INDEX_FIRST_PERSON.getOneBased() + " t/friend");
        assertEquals(new TagAddCommand(INDEX_FIRST_PERSON, new Tag("friend")), command);
    }

    @Test
    public void parseCommand_tagrm() throws Exception {
        TagRemoveCommand command = (TagRemoveCommand) parser.parseCommand(
            TagRemoveCommand.COMMAND_WORD + " "
            + INDEX_FIRST_PERSON.getOneBased() + " t/friend");
        assertEquals(new TagRemoveCommand(INDEX_FIRST_PERSON, new Tag("friend")), command);
    }
}
