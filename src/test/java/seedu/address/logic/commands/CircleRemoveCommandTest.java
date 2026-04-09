package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalCircles.CLIENTS;
import static seedu.address.testutil.TypicalCircles.PROSPECTS;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.Optional;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.circle.Circle;
import seedu.address.model.person.Person;
import seedu.address.testutil.PersonBuilder;

public class CircleRemoveCommandTest {

    @Test
    public void execute_validIndexWithCircle_success() {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        Model expectedModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());

        Person personWithCircle = setCircleAtIndex(model, INDEX_FIRST_PERSON, Optional.of(CLIENTS));
        setCircleAtIndex(expectedModel, INDEX_FIRST_PERSON, Optional.of(CLIENTS));

        Person personWithoutCircle = buildPersonWithCircle(personWithCircle, Optional.empty());
        expectedModel.setPerson(personWithCircle, personWithoutCircle);

        CircleRemoveCommand command = new CircleRemoveCommand(INDEX_FIRST_PERSON);
        String expectedMessage = String.format(CircleRemoveCommand.MESSAGE_CIRCLE_PERSON_SUCCESS,
            personWithCircle.getName());
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndex_throwsCommandException() {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);

        CircleRemoveCommand command = new CircleRemoveCommand(outOfBoundIndex);

        assertThrows(CommandException.class,
                Messages.MESSAGE_OOR_INDEX, () -> command.execute(model));
    }

    @Test
    public void execute_personWithoutCircle_throwsCommandException() {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

        setCircleAtIndex(model, INDEX_FIRST_PERSON, Optional.empty());

        CircleRemoveCommand command = new CircleRemoveCommand(INDEX_FIRST_PERSON);

        assertThrows(CommandException.class,
            CircleRemoveCommand.MESSAGE_CIRCLE_PERSON_FAILURE, () -> command.execute(model));
    }

    @Test
    public void execute_lastPersonWithCircle_success() {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        Model expectedModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());

        Index lastIndex = Index.fromOneBased(model.getFilteredPersonList().size());

        Person lastPersonWithCircle = setCircleAtIndex(model, lastIndex, Optional.of(PROSPECTS));
        setCircleAtIndex(expectedModel, lastIndex, Optional.of(PROSPECTS));

        Person lastPersonWithoutCircle = buildPersonWithCircle(lastPersonWithCircle, Optional.empty());
        expectedModel.setPerson(lastPersonWithCircle, lastPersonWithoutCircle);

        CircleRemoveCommand command = new CircleRemoveCommand(lastIndex);
        String expectedMessage = String.format(CircleRemoveCommand.MESSAGE_CIRCLE_PERSON_SUCCESS,
            lastPersonWithCircle.getName());
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }

    @Test
    public void equals() {
        CircleRemoveCommand removeFirstCommand = new CircleRemoveCommand(INDEX_FIRST_PERSON);
        CircleRemoveCommand removeSecondCommand = new CircleRemoveCommand(INDEX_SECOND_PERSON);

        // same object -> returns true
        assertTrue(removeFirstCommand.equals(removeFirstCommand));

        // same values -> returns true
        CircleRemoveCommand removeFirstCommandCopy = new CircleRemoveCommand(INDEX_FIRST_PERSON);
        assertTrue(removeFirstCommand.equals(removeFirstCommandCopy));

        // different types -> returns false
        assertFalse(removeFirstCommand.equals(1));

        // null -> returns false
        assertFalse(removeFirstCommand.equals(null));

        // different index -> returns false
        assertFalse(removeFirstCommand.equals(removeSecondCommand));
    }

    @Test
    public void toString_correct() {
        CircleRemoveCommand command = new CircleRemoveCommand(INDEX_FIRST_PERSON);

        assertTrue(command.toString().contains("index"));
    }

    private Person setCircleAtIndex(Model model, Index index, Optional<Circle> circle) {
        Person person = model.getFilteredPersonList().get(index.getZeroBased());
        Person updatedPerson = circle.isPresent()
                ? new PersonBuilder(person).withCircle(circle.get().getCircleName()).build()
                : buildPersonWithCircle(person, Optional.empty());

        model.setPerson(person, updatedPerson);
        return updatedPerson;
    }

    private Person buildPersonWithCircle(Person base, Optional<Circle> circle) {
        return new Person(
                base.getName(),
                base.getPhone(),
                base.getEmail(),
                base.getAddress(),
                base.getTags(),
                base.getFollowUpDate(),
                base.getNotes(),
                circle
        );
    }
}
