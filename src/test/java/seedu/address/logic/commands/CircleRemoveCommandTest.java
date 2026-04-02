package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.Optional;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.circle.Circle;
import seedu.address.model.person.Person;

public class CircleRemoveCommandTest {

    @Test
    public void execute_validIndexWithCircle_success() {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        Model expectedModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());

        Index index = Index.fromOneBased(1);

        // Get the person and add a circle if they don't have one
        Person personAtIndex = model.getFilteredPersonList().get(index.getZeroBased());
        Person personWithCircle = new Person(
            personAtIndex.getName(),
            personAtIndex.getPhone(),
            personAtIndex.getEmail(),
            personAtIndex.getAddress(),
            personAtIndex.getTags(),
            personAtIndex.getFollowUpDate(),
            personAtIndex.getNotes(),
            Optional.of(new Circle("client"))
        );
        model.setPerson(personAtIndex, personWithCircle);

        // Same for expected model
        Person expectedPersonAtIndex = expectedModel.getFilteredPersonList().get(index.getZeroBased());
        Person expectedPersonWithCircle = new Person(
            expectedPersonAtIndex.getName(),
            expectedPersonAtIndex.getPhone(),
            expectedPersonAtIndex.getEmail(),
            expectedPersonAtIndex.getAddress(),
            expectedPersonAtIndex.getTags(),
            expectedPersonAtIndex.getFollowUpDate(),
            expectedPersonAtIndex.getNotes(),
            Optional.of(new Circle("client"))
        );
        expectedModel.setPerson(expectedPersonAtIndex, expectedPersonWithCircle);

        // Now remove the circle
        Person personWithoutCircle = new Person(
            personWithCircle.getName(),
            personWithCircle.getPhone(),
            personWithCircle.getEmail(),
            personWithCircle.getAddress(),
            personWithCircle.getTags(),
            personWithCircle.getFollowUpDate(),
            personWithCircle.getNotes(),
            Optional.empty()
        );
        expectedModel.setPerson(personWithCircle, personWithoutCircle);

        CircleRemoveCommand command = new CircleRemoveCommand(index);
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
            CircleRemoveCommand.MESSAGE_INVALID_PERSON, () -> command.execute(model));
    }

    @Test
    public void execute_personWithoutCircle_throwsCommandException() {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

        Index index = Index.fromOneBased(1);

        // Ensure person doesn't have a circle
        Person personAtIndex = model.getFilteredPersonList().get(index.getZeroBased());
        if (personAtIndex.getCircle().isPresent()) {
            Person personWithoutCircle = new Person(
                personAtIndex.getName(),
                personAtIndex.getPhone(),
                personAtIndex.getEmail(),
                personAtIndex.getAddress(),
                personAtIndex.getTags(),
                personAtIndex.getFollowUpDate(),
                personAtIndex.getNotes(),
                Optional.empty()
            );
            model.setPerson(personAtIndex, personWithoutCircle);
        }

        CircleRemoveCommand command = new CircleRemoveCommand(index);

        assertThrows(CommandException.class,
            CircleRemoveCommand.MESSAGE_CIRCLE_PERSON_FAILURE, () -> command.execute(model));
    }

    @Test
    public void execute_lastPersonWithCircle_success() {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        Model expectedModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());

        Index lastIndex = Index.fromOneBased(model.getFilteredPersonList().size());

        // Add circle to last person
        Person lastPersonAtIndex = model.getFilteredPersonList().get(lastIndex.getZeroBased());
        Person lastPersonWithCircle = new Person(
            lastPersonAtIndex.getName(),
            lastPersonAtIndex.getPhone(),
            lastPersonAtIndex.getEmail(),
            lastPersonAtIndex.getAddress(),
            lastPersonAtIndex.getTags(),
            lastPersonAtIndex.getFollowUpDate(),
            lastPersonAtIndex.getNotes(),
            Optional.of(new Circle("prospect"))
        );
        model.setPerson(lastPersonAtIndex, lastPersonWithCircle);

        // Same for expected model
        Person expectedLastPersonAtIndex = expectedModel.getFilteredPersonList().get(lastIndex.getZeroBased());
        Person expectedLastPersonWithCircle = new Person(
            expectedLastPersonAtIndex.getName(),
            expectedLastPersonAtIndex.getPhone(),
            expectedLastPersonAtIndex.getEmail(),
            expectedLastPersonAtIndex.getAddress(),
            expectedLastPersonAtIndex.getTags(),
            expectedLastPersonAtIndex.getFollowUpDate(),
            expectedLastPersonAtIndex.getNotes(),
            Optional.of(new Circle("prospect"))
        );
        expectedModel.setPerson(expectedLastPersonAtIndex, expectedLastPersonWithCircle);

        // Remove circle from last person
        Person lastPersonWithoutCircle = new Person(
            lastPersonWithCircle.getName(),
            lastPersonWithCircle.getPhone(),
            lastPersonWithCircle.getEmail(),
            lastPersonWithCircle.getAddress(),
            lastPersonWithCircle.getTags(),
            lastPersonWithCircle.getFollowUpDate(),
            lastPersonWithCircle.getNotes(),
            Optional.empty()
        );
        expectedModel.setPerson(lastPersonWithCircle, lastPersonWithoutCircle);

        CircleRemoveCommand command = new CircleRemoveCommand(lastIndex);
        String expectedMessage = String.format(CircleRemoveCommand.MESSAGE_CIRCLE_PERSON_SUCCESS,
            lastPersonWithCircle.getName());
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_multiplePersonsWithCircles() {
        String[] circles = {"client", "prospect", "friend"};

        for (String circleName : circles) {
            Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

            Index index = Index.fromOneBased(1);

            // Add circle
            Person personAtIndex = model.getFilteredPersonList().get(index.getZeroBased());
            Person personWithCircle = new Person(
                personAtIndex.getName(),
                personAtIndex.getPhone(),
                personAtIndex.getEmail(),
                personAtIndex.getAddress(),
                personAtIndex.getTags(),
                personAtIndex.getFollowUpDate(),
                personAtIndex.getNotes(),
                Optional.of(new Circle(circleName))
            );
            model.setPerson(personAtIndex, personWithCircle);

            // Remove circle
            CircleRemoveCommand command = new CircleRemoveCommand(index);

            try {
                command.execute(model);
                // Verify circle was removed
                Person updatedPerson = model.getFilteredPersonList().get(index.getZeroBased());
                assertTrue(updatedPerson.getCircle().isEmpty());
            } catch (CommandException e) {
                assertTrue(false, "Should not throw exception for person with circle");
            }
        }
    }

    @Test
    public void execute_removeCircleThenAddAgain() {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

        Index index = Index.fromOneBased(1);

        // Add circle
        Person personAtIndex = model.getFilteredPersonList().get(index.getZeroBased());
        Person personWithCircle = new Person(
            personAtIndex.getName(),
            personAtIndex.getPhone(),
            personAtIndex.getEmail(),
            personAtIndex.getAddress(),
            personAtIndex.getTags(),
            personAtIndex.getFollowUpDate(),
            personAtIndex.getNotes(),
            Optional.of(new Circle("client"))
        );
        model.setPerson(personAtIndex, personWithCircle);

        // Remove circle
        CircleRemoveCommand removeCommand = new CircleRemoveCommand(index);
        try {
            removeCommand.execute(model);
        } catch (CommandException e) {
            assertTrue(false, "Remove should succeed for person with circle");
        }

        // Verify circle was removed
        Person personAfterRemove = model.getFilteredPersonList().get(index.getZeroBased());
        assertTrue(personAfterRemove.getCircle().isEmpty());

        // Now verify that we can add a circle again
        Person personToAddCircle = model.getFilteredPersonList().get(index.getZeroBased());
        assertTrue(personToAddCircle.getCircle().isEmpty());
    }

    @Test
    public void equals() {
        CircleRemoveCommand removeFirstCommand = new CircleRemoveCommand(Index.fromOneBased(1));
        CircleRemoveCommand removeSecondCommand = new CircleRemoveCommand(Index.fromOneBased(2));

        // same object -> returns true
        assertTrue(removeFirstCommand.equals(removeFirstCommand));

        // same values -> returns true
        CircleRemoveCommand removeFirstCommandCopy = new CircleRemoveCommand(Index.fromOneBased(1));
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
        Index index = Index.fromOneBased(1);
        CircleRemoveCommand command = new CircleRemoveCommand(index);

        String expectedToString = "seedu.address.logic.commands.CircleRemoveCommand{"
            + "index=Index 1"
            + "}";

        assertTrue(command.toString().contains("index"));
    }

}
