package seedu.address.ui;

import java.time.LocalDate;
import java.util.Comparator;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import seedu.address.model.person.Person;

/**
 * An UI component that displays information of a {@code Person}.
 */
public class PersonCard extends UiPart<Region> {

    private static final String FXML = "PersonListCard.fxml";

    public final Person person;

    @FXML
    private HBox cardPane;
    @FXML
    private Label name;
    @FXML
    private Label id;
    @FXML
    private Label phone;
    @FXML
    private Label address;
    @FXML
    private Label email;
    @FXML
    private Label followUpDate;
    @FXML
    private FlowPane tags;

    /**
     * Creates a {@code PersonCard} with the given {@code Person} and index to display.
     */
    public PersonCard(Person person, int displayedIndex) {
        super(FXML);
        this.person = person;
        id.setText(displayedIndex + ".");
        name.setText(person.getName().fullName);

        String addressValue = person.getAddress().value;
        String emailValue = person.getEmail().value;

        phone.setText("Phone: " + person.getPhone().value);
        address.setText(addressValue.equals("MISSING_ADDRESS") ? "-" : addressValue);
        email.setText(emailValue.equals("missing@email.empty") ? "-" : emailValue);

        followUpDate.getStyleClass().add("follow-up-date-value");

        if (person.getFollowUpDate().isPresent()) {
            LocalDate date = person.getFollowUpDate().get().value;
            followUpDate.setText(date.toString());

            LocalDate today = LocalDate.now();
            LocalDate soonThreshold = today.plusDays(3);
            if (!date.isBefore(today) && !date.isAfter(soonThreshold)) {
                followUpDate.getStyleClass().add("follow-up-soon");
            }
        } else {
            followUpDate.setText("-");
        }

        person.getTags().stream()
                .sorted(Comparator.comparing(tag -> tag.tagName))
                .forEach(tag -> tags.getChildren().add(new Label(tag.tagName)));
    }
}
