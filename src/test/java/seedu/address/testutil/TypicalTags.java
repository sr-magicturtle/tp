package seedu.address.testutil;

import seedu.address.model.tag.Tag;

/**
 * A utility class containing a list of Tag objects to be used in tests.
 */
public class TypicalTags {
    public static final Tag FRIEND = new Tag("friend");
    public static final Tag CLASSMATE = new Tag("classmate");
    public static final Tag COLLEAGUE = new Tag("colleague");
    public static final Tag HELLO_WORLD = new Tag("hello-world");
    public static final Tag UPPERCASE = new Tag("VALID");
    public static final Tag MIXED_CASE = new Tag("HelloWorld");

    private TypicalTags() {} // prevents instantiation
}

