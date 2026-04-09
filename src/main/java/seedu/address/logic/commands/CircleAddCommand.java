package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CIRCLE;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.circle.Circle;
import seedu.address.model.person.Person;

/**
 * Adds a circle to a contact in the address book.
 * The contact is identified by its index in the filtered person list.
 * The circle must be valid (client, prospect, or friend) and not already exist for the contact.
 */
public class CircleAddCommand extends Command {

    public static final String COMMAND_WORD = "circleadd";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds a circle to a contact.\n"
        + "Parameters: INDEX " + PREFIX_CIRCLE + "CIRCLE_NAME\n"
        + "Example: " + COMMAND_WORD + " 1 " + PREFIX_CIRCLE + "client";

    public static final String MESSAGE_CIRCLE_PERSON_SUCCESS = "Added circle '%1$s' to %2$s";
    public static final String MESSAGE_CIRCLE_PERSON_FAILURE = "Add failed: contact already has a circle.";

    private static final Logger logger = LogsCenter.getLogger(CircleAddCommand.class);

    private final Circle circle;
    private final Index index;

    /**
     * Constructs a CircleAddCommand to add a circle to a contact.
     *
     * @param circle the circle to be added
     * @param index the index of the contact in the filtered person list
     */
    public CircleAddCommand(Index index, Circle circle) {
        requireNonNull(circle);
        requireNonNull(index);
        this.circle = circle;
        this.index = index;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_OOR_INDEX);
        }

        Person personAtIndex = lastShownList.get(index.getZeroBased());
        if (personAtIndex.getCircle().isPresent()) {
            throw new CommandException(MESSAGE_CIRCLE_PERSON_FAILURE);
        }

        Person editedPerson = new Person(
            personAtIndex.getName(),
            personAtIndex.getPhone(),
            personAtIndex.getEmail(),
            personAtIndex.getAddress(),
            personAtIndex.getTags(),
            personAtIndex.getFollowUpDate(),
            personAtIndex.getNotes(),
            Optional.of(circle)
        );

        model.setPerson(personAtIndex, editedPerson);
        logger.fine("Circle added: " + circle.getCircleName() + " to person: " + editedPerson.getName());

        return new CommandResult(String.format(MESSAGE_CIRCLE_PERSON_SUCCESS,
            circle.getCircleName(), editedPerson.getName()));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof CircleAddCommand)) {
            return false;
        }

        CircleAddCommand otherCircleAddCommand = (CircleAddCommand) other;
        return index.equals(otherCircleAddCommand.index) && circle.equals(otherCircleAddCommand.circle);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
            .add("index", index)
            .add("circle", circle)
            .toString();
    }
}
