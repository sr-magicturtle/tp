package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.circle.Circle;
import seedu.address.model.person.Person;

public class CircleFilterCommandTest {

    @Test
    public void execute_filterByClient_success() throws CommandException {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

        // Add some persons with 'client' circle
        List<Person> originalList = model.getFilteredPersonList();
        if (originalList.size() > 0) {
            Person personAtIndex0 = originalList.get(0);
            Person personWithClientCircle = new Person(
                personAtIndex0.getName(),
                personAtIndex0.getPhone(),
                personAtIndex0.getEmail(),
                personAtIndex0.getAddress(),
                personAtIndex0.getTags(),
                personAtIndex0.getFollowUpDate(),
                personAtIndex0.getNotes(),
                Optional.of(new Circle("client"))
            );
            model.setPerson(personAtIndex0, personWithClientCircle);
        }

        CircleFilterCommand command = new CircleFilterCommand("client");
        CommandResult result = command.execute(model);

        assertTrue(result.getFeedbackToUser().contains("client"));
    }

    @Test
    public void execute_filterByProspect_success() throws CommandException {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

        // Add person with 'prospect' circle
        List<Person> originalList = model.getFilteredPersonList();
        if (originalList.size() > 1) {
            Person personAtIndex1 = originalList.get(1);
            Person personWithProspectCircle = new Person(
                personAtIndex1.getName(),
                personAtIndex1.getPhone(),
                personAtIndex1.getEmail(),
                personAtIndex1.getAddress(),
                personAtIndex1.getTags(),
                personAtIndex1.getFollowUpDate(),
                personAtIndex1.getNotes(),
                Optional.of(new Circle("prospect"))
            );
            model.setPerson(personAtIndex1, personWithProspectCircle);
        }

        CircleFilterCommand command = new CircleFilterCommand("prospect");
        CommandResult result = command.execute(model);

        assertTrue(result.getFeedbackToUser().contains("prospect"));
    }

    @Test
    public void execute_filterByFriend_success() throws CommandException {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

        // Add person with 'friend' circle
        List<Person> originalList = model.getFilteredPersonList();
        if (originalList.size() > 2) {
            Person personAtIndex2 = originalList.get(2);
            Person personWithFriendCircle = new Person(
                personAtIndex2.getName(),
                personAtIndex2.getPhone(),
                personAtIndex2.getEmail(),
                personAtIndex2.getAddress(),
                personAtIndex2.getTags(),
                personAtIndex2.getFollowUpDate(),
                personAtIndex2.getNotes(),
                Optional.of(new Circle("friend"))
            );
            model.setPerson(personAtIndex2, personWithFriendCircle);
        }

        CircleFilterCommand command = new CircleFilterCommand("friend");
        CommandResult result = command.execute(model);

        assertTrue(result.getFeedbackToUser().contains("friend"));
    }

    @Test
    public void execute_filterNoContactsFound_returnsNoContactsMessage() throws CommandException {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

        // Clear all circles from persons
        List<Person> originalList = model.getFilteredPersonList();
        for (Person person : originalList) {
            Person personWithoutCircle = new Person(
                person.getName(),
                person.getPhone(),
                person.getEmail(),
                person.getAddress(),
                person.getTags(),
                person.getFollowUpDate(),
                person.getNotes(),
                Optional.empty()
            );
            model.setPerson(person, personWithoutCircle);
        }

        CircleFilterCommand command = new CircleFilterCommand("client");
        CommandResult result = command.execute(model);

        assertTrue(result.getFeedbackToUser().contains("No contacts found"));
    }

    @Test
    public void execute_filterCaseInsensitive_success() throws CommandException {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

        // Add person with circle
        List<Person> originalList = model.getFilteredPersonList();
        if (originalList.size() > 0) {
            Person personAtIndex0 = originalList.get(0);
            Person personWithCircle = new Person(
                personAtIndex0.getName(),
                personAtIndex0.getPhone(),
                personAtIndex0.getEmail(),
                personAtIndex0.getAddress(),
                personAtIndex0.getTags(),
                personAtIndex0.getFollowUpDate(),
                personAtIndex0.getNotes(),
                Optional.of(new Circle("client"))
            );
            model.setPerson(personAtIndex0, personWithCircle);
        }

        // Filter with different cases
        String[] casesVariations = {"client", "CLIENT", "Client", "CLiEnT"};
        for (String caseVariation : casesVariations) {
            Model modelCopy = new ModelManager(getTypicalAddressBook(), new UserPrefs());
            List<Person> copyList = modelCopy.getFilteredPersonList();
            if (copyList.size() > 0) {
                Person person = copyList.get(0);
                Person personWithCircle = new Person(
                    person.getName(),
                    person.getPhone(),
                    person.getEmail(),
                    person.getAddress(),
                    person.getTags(),
                    person.getFollowUpDate(),
                    person.getNotes(),
                    Optional.of(new Circle("client"))
                );
                modelCopy.setPerson(person, personWithCircle);
            }

            CircleFilterCommand command = new CircleFilterCommand(caseVariation);
            CommandResult result = command.execute(modelCopy);

            // Verify the filter was applied
            assertTrue(result.getFeedbackToUser().toLowerCase().contains("filtered"));
        }
    }

    @Test
    public void execute_multiplePersonsWithSameCircle() throws CommandException {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

        // Add multiple persons with same circle
        List<Person> originalList = model.getFilteredPersonList();
        for (int i = 0; i < Math.min(2, originalList.size()); i++) {
            Person person = originalList.get(i);
            Person personWithCircle = new Person(
                person.getName(),
                person.getPhone(),
                person.getEmail(),
                person.getAddress(),
                person.getTags(),
                person.getFollowUpDate(),
                person.getNotes(),
                Optional.of(new Circle("prospect"))
            );
            model.setPerson(person, personWithCircle);
        }

        CircleFilterCommand command = new CircleFilterCommand("prospect");
        CommandResult result = command.execute(model);

        assertTrue(result.getFeedbackToUser().contains("prospect"));
        List<Person> filteredList = model.getFilteredPersonList();
        assertTrue(filteredList.size() >= 2);
    }

    @Test
    public void execute_mixedCircles_filtersByCorrectCircle() throws CommandException {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

        // Add persons with different circles
        List<Person> originalList = model.getFilteredPersonList();
        String[] circles = {"client", "prospect", "friend"};
        for (int i = 0; i < Math.min(3, originalList.size()); i++) {
            Person person = originalList.get(i);
            Person personWithCircle = new Person(
                person.getName(),
                person.getPhone(),
                person.getEmail(),
                person.getAddress(),
                person.getTags(),
                person.getFollowUpDate(),
                person.getNotes(),
                Optional.of(new Circle(circles[i]))
            );
            model.setPerson(person, personWithCircle);
        }

        // Filter for 'client' only
        CircleFilterCommand command = new CircleFilterCommand("client");
        CommandResult result = command.execute(model);

        List<Person> filteredList = model.getFilteredPersonList();
        // Verify only 'client' circle persons are in filtered list
        for (Person person : filteredList) {
            assertTrue(person.getCircle().isPresent());
            assertTrue(person.getCircle().get().getCircleName().equals("client"));
        }
    }

    @Test
    public void equals() {
        CircleFilterCommand filterClientFirst = new CircleFilterCommand("client");
        CircleFilterCommand filterClientSecond = new CircleFilterCommand("client");
        CircleFilterCommand filterProspect = new CircleFilterCommand("prospect");

        // same object -> returns true
        assertTrue(filterClientFirst.equals(filterClientFirst));

        // same values -> returns true
        assertTrue(filterClientFirst.equals(filterClientSecond));

        // different types -> returns false
        assertFalse(filterClientFirst.equals(1));

        // null -> returns false
        assertFalse(filterClientFirst.equals(null));

        // different circle name -> returns false
        assertFalse(filterClientFirst.equals(filterProspect));
    }

    @Test
    public void equals_caseInsensitive() {
        // Different case should still be equal after normalization
        CircleFilterCommand filterClientLower = new CircleFilterCommand("client");
        CircleFilterCommand filterClientUpper = new CircleFilterCommand("CLIENT");

        // Both normalized to lowercase, so should be equal
        assertTrue(filterClientLower.equals(filterClientUpper));
    }

    @Test
    public void toString_correct() {
        CircleFilterCommand command = new CircleFilterCommand("client");

        String expectedToString = "seedu.address.logic.commands.CircleFilterCommand{"
            + "circleName=client"
            + "}";

        assertTrue(command.toString().contains("circleName") && command.toString().contains("client"));
    }

}
