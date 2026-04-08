package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.List;
import java.util.logging.Logger;

import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;
import seedu.address.model.tag.Tag;

/**
 * Adds a tag to a contact in the address book.
 * The contact is identified by its index in the filtered person list.
 * The tag must contain only letters, numbers, or hyphens,
 * be between 1 and 20 characters long, and not already exist for the contact.
 */
public class TagAddCommand extends Command {
    public static final String COMMAND_WORD = "tagadd";
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds 1 tag to a contact in"
        + "the address book at a time.\n"
        + "Parameters: INDEX " + PREFIX_TAG + "TAG\n"
        + "Example: " + COMMAND_WORD + " 1 " + PREFIX_TAG + "classmate";
    public static final int MAX_TAGS_PER_PERSON = 5;
    public static final String MESSAGE_TAG_LIMIT_REACHED = "A contact can have at most 5 tags.";
    public static final String MESSAGE_TAG_PERSON_SUCCESS = "Added tag '%1$s' to %2$s";
    public static final String MESSAGE_TAG_PERSON_FAILURE = "Invalid value: tag already exists for this contact.";
    public static final String MESSAGE_INVALID_PERSON = "The person does not exist in the address book.";

    private static final Logger logger = LogsCenter.getLogger(TagAddCommand.class);

    /** The index of the contact in the filtered person list. */
    private final Index index;

    /** The tag to be added to the contact. */
    private final Tag tag;

    /**
     * Constructs a TagAddCommand to add a tag to a contact.
     *
     * @param index the index of the contact in the filtered person list
     * @param tag the tag to be added
     */
    public TagAddCommand(Index index, Tag tag) {
        requireNonNull(index);
        requireNonNull(tag);
        this.index = index;
        this.tag = tag;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(MESSAGE_INVALID_PERSON);
        }

        Person personAtIndex = lastShownList.get(index.getZeroBased());

        if (personAtIndex.getTags().contains(tag)) {
            throw new CommandException(MESSAGE_TAG_PERSON_FAILURE);
        }

        if (personAtIndex.getTags().size() >= MAX_TAGS_PER_PERSON) {
            throw new CommandException(MESSAGE_TAG_LIMIT_REACHED);
        }

        Person editedPerson = personAtIndex.addTag(tag);
        model.setPerson(personAtIndex, editedPerson);
        logger.fine("Tag added: " + tag.tagName + " to person: " + editedPerson.getName());

        return new CommandResult(
                String.format(MESSAGE_TAG_PERSON_SUCCESS, tag.tagName, editedPerson.getName()));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof TagAddCommand)) {
            return false;
        }

        TagAddCommand otherCommand = (TagAddCommand) other;
        return index.equals(otherCommand.index) && tag.equals(otherCommand.tag);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
            .add("index", index)
            .add("tag", tag)
            .toString();
    }
}
