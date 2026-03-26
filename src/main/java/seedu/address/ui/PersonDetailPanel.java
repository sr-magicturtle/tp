package seedu.address.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import seedu.address.model.person.Person;

/**
 * An UI component that displays information of a {@code Person} in View mode.
 */
public class PersonDetailPanel extends UiPart<Region> {

    private static final String FXML = "PersonDetailPanel.fxml";

    @FXML private Label name;
    @FXML private Label phone;
    @FXML private Label email;
    @FXML private Label address;

    @FXML private Label notes;
    @FXML private Label followUpDate;
    @FXML private Label circle;

    @FXML private HBox followUpRow;
    @FXML private HBox notesRow;
    @FXML private HBox circleRow;

    /**
     * Creates a {@code PersonDetailPanel} with the given {@code Person} to display.
     */
    public PersonDetailPanel(Person person) {
        super(FXML);

        name.setText(person.getName().fullName);
        phone.setText(person.getPhone().value);
        email.setText(person.getEmail().value);
        address.setText(person.getAddress().value);

        notes.setText(person.getNotes().orElse("-"));

        followUpDate.setText(
                person.getFollowUpDate().map(d -> d.value.toString()).orElse("-")
        );

        circle.setText(person.getCircle().orElse("-"));
    }
}
