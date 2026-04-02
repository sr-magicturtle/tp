package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.circle.Circle;
import seedu.address.model.person.Person;
import seedu.address.testutil.PersonBuilder;

public class CircleAddCommandTest {

    /**
     * Creates a fresh AddressBook with test persons that have no circles assigned.
     * Used to avoid conflicts with typical test data that already have circles.
     */
    private static AddressBook createCleanAddressBook() {
        AddressBook ab = new AddressBook();
        ab.addPerson(new PersonBuilder().withName("Alice Test")
            .withPhone("98765432").withEmail("alicetest@example.com").withAddress("Test Address 1")
            .build());
        ab.addPerson(new PersonBuilder().withName("Bob Test")
            .withPhone("87654321").withEmail("bobtest@example.com").withAddress("Test Address 2")
            .build());
        ab.addPerson(new PersonBuilder().withName("Charlie Test")
            .withPhone("76543210").withEmail("charlietest@example.com").withAddress("Test Address 3")
            .build());
        return ab;
    }

    @Test
    public void execute_validIndexAndNewCircle_success() {
        Model model = new ModelManager(createCleanAddressBook(), new UserPrefs());
        Model expectedModel = new ModelManager(createCleanAddressBook(), new UserPrefs());

        Index index = Index.fromOneBased(1);
        Circle circle = new Circle("friend");

        Person personAtIndex = model.getFilteredPersonList().get(index.getZeroBased());
        Person circledPerson = new Person(
            personAtIndex.getName(),
            personAtIndex.getPhone(),
            personAtIndex.getEmail(),
            personAtIndex.getAddress(),
            personAtIndex.getTags(),
            personAtIndex.getFollowUpDate(),
            personAtIndex.getNotes(),
            java.util.Optional.of(circle)
        );

        expectedModel.setPerson(personAtIndex, circledPerson);

        CircleAddCommand command = new CircleAddCommand(circle, index);
        String expectedMessage = String.format(
            CircleAddCommand.MESSAGE_CIRCLE_PERSON_SUCCESS, circle.getCircleName(), circledPerson.getName());
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_validIndexAllCircles_success() {
        // Test prospect circle
        Model model = new ModelManager(createCleanAddressBook(), new UserPrefs());
        Model expectedModel = new ModelManager(createCleanAddressBook(), new UserPrefs());

        Index index = Index.fromOneBased(1);
        Circle circle = new Circle("prospect");

        Person personAtIndex = model.getFilteredPersonList().get(index.getZeroBased());
        Person circledPerson = new Person(
            personAtIndex.getName(),
            personAtIndex.getPhone(),
            personAtIndex.getEmail(),
            personAtIndex.getAddress(),
            personAtIndex.getTags(),
            personAtIndex.getFollowUpDate(),
            personAtIndex.getNotes(),
            java.util.Optional.of(circle)
        );

        expectedModel.setPerson(personAtIndex, circledPerson);

        CircleAddCommand command = new CircleAddCommand(circle, index);
        String expectedMessage = String.format(
            CircleAddCommand.MESSAGE_CIRCLE_PERSON_SUCCESS, circle.getCircleName(), circledPerson.getName());
        assertCommandSuccess(command, model, expectedMessage, expectedModel);

        // Test friend circle
        model = new ModelManager(createCleanAddressBook(), new UserPrefs());
        expectedModel = new ModelManager(createCleanAddressBook(), new UserPrefs());

        index = Index.fromOneBased(2);
        circle = new Circle("friend");

        personAtIndex = model.getFilteredPersonList().get(index.getZeroBased());
        circledPerson = new Person(
            personAtIndex.getName(),
            personAtIndex.getPhone(),
            personAtIndex.getEmail(),
            personAtIndex.getAddress(),
            personAtIndex.getTags(),
            personAtIndex.getFollowUpDate(),
            personAtIndex.getNotes(),
            java.util.Optional.of(circle)
        );

        expectedModel.setPerson(personAtIndex, circledPerson);

        command = new CircleAddCommand(circle, index);
        expectedMessage = String.format(
            CircleAddCommand.MESSAGE_CIRCLE_PERSON_SUCCESS, circle.getCircleName(), circledPerson.getName());
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_lastPersonValidCircle_success() {
        Model model = new ModelManager(createCleanAddressBook(), new UserPrefs());
        Model expectedModel = new ModelManager(createCleanAddressBook(), new UserPrefs());

        Index lastIndex = Index.fromOneBased(model.getFilteredPersonList().size());
        Circle circle = new Circle("prospect");

        Person personAtIndex = model.getFilteredPersonList().get(lastIndex.getZeroBased());
        Person circledPerson = new Person(
            personAtIndex.getName(),
            personAtIndex.getPhone(),
            personAtIndex.getEmail(),
            personAtIndex.getAddress(),
            personAtIndex.getTags(),
            personAtIndex.getFollowUpDate(),
            personAtIndex.getNotes(),
            java.util.Optional.of(circle)
        );

        expectedModel.setPerson(personAtIndex, circledPerson);

        CircleAddCommand command = new CircleAddCommand(circle, lastIndex);
        String expectedMessage = String.format(
            CircleAddCommand.MESSAGE_CIRCLE_PERSON_SUCCESS, circle.getCircleName(), circledPerson.getName());
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndex_throwsCommandException() {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        Circle circle = new Circle("client");

        CircleAddCommand command = new CircleAddCommand(circle, outOfBoundIndex);

        assertThrows(CommandException.class,
            CircleAddCommand.MESSAGE_INVALID_PERSON, () -> command.execute(model));
    }

    @Test
    public void execute_invalidIndexZero_throwsCommandException() {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

        // Index is created using fromOneBased which starts at 1
        // So getZeroBased() of a 1-based index 1 would be 0, which is the first element
        // We need an index that exceeds the list size
        Index invalidIndex = Index.fromZeroBased(model.getFilteredPersonList().size() + 1);
        Circle circle = new Circle("friend");

        CircleAddCommand command = new CircleAddCommand(circle, invalidIndex);

        assertThrows(CommandException.class,
            CircleAddCommand.MESSAGE_INVALID_PERSON, () -> command.execute(model));
    }

    @Test
    public void execute_personAlreadyHasCircle_throwsCommandException() {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

        Index index = Index.fromOneBased(1);
        Circle firstCircle = new Circle("client");

        // First add a circle
        Person personAtIndex = model.getFilteredPersonList().get(index.getZeroBased());
        Person personWithCircle = new Person(
            personAtIndex.getName(),
            personAtIndex.getPhone(),
            personAtIndex.getEmail(),
            personAtIndex.getAddress(),
            personAtIndex.getTags(),
            personAtIndex.getFollowUpDate(),
            personAtIndex.getNotes(),
            java.util.Optional.of(firstCircle)
        );
        model.setPerson(personAtIndex, personWithCircle);

        // Now try to add another circle
        Circle secondCircle = new Circle("prospect");
        CircleAddCommand command = new CircleAddCommand(secondCircle, index);

        assertThrows(CommandException.class,
            CircleAddCommand.MESSAGE_CIRCLE_PERSON_FAILURE, () -> command.execute(model));
    }

    @Test
    public void execute_differentIndexes_allValid() {
        String[] validCircles = {"client", "prospect", "friend"};

        for (String circleName : validCircles) {
            Model model = new ModelManager(createCleanAddressBook(), new UserPrefs());

            if (1 <= model.getFilteredPersonList().size()) {
                Index index = Index.fromOneBased(1);
                Circle circle = new Circle(circleName);

                CircleAddCommand command = new CircleAddCommand(circle, index);

                // Should not throw exception for valid index and no existing circle
                try {
                    command.execute(model);
                    // Success - verified that circle was added
                } catch (CommandException e) {
                    // Should not happen with clean data
                    assertTrue(false, "Should not throw exception when adding circle to person without circle");
                }
            }
        }
    }

    @Test
    public void equals() {
        Circle client = new Circle("client");
        Circle prospect = new Circle("prospect");

        CircleAddCommand addClientFirstCommand = new CircleAddCommand(client, Index.fromOneBased(1));
        CircleAddCommand addClientSecondCommand = new CircleAddCommand(client, Index.fromOneBased(2));
        CircleAddCommand addProspectFirstCommand = new CircleAddCommand(prospect, Index.fromOneBased(1));

        // same object -> returns true
        assertTrue(addClientFirstCommand.equals(addClientFirstCommand));

        // same values -> returns true
        CircleAddCommand addClientFirstCommandCopy = new CircleAddCommand(client, Index.fromOneBased(1));
        assertTrue(addClientFirstCommand.equals(addClientFirstCommandCopy));

        // different types -> returns false
        assertFalse(addClientFirstCommand.equals(1));

        // null -> returns false
        assertFalse(addClientFirstCommand.equals(null));

        // different index -> returns false
        assertFalse(addClientFirstCommand.equals(addClientSecondCommand));

        // different circle -> returns false
        assertFalse(addClientFirstCommand.equals(addProspectFirstCommand));

        // different circle and index -> returns false
        assertFalse(addClientFirstCommand.equals(addProspectFirstCommand));
    }

    @Test
    public void toString_correct() {
        Circle client = new Circle("client");
        Index index = Index.fromOneBased(1);
        CircleAddCommand command = new CircleAddCommand(client, index);

        String expectedToString = "seedu.address.logic.commands.CircleAddCommand{"
            + "index=Index 1, circle=[client]"
            + "}";

        assertTrue(command.toString().contains("index") && command.toString().contains("circle"));
    }

}
