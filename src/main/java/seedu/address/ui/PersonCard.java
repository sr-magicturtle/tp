package seedu.address.ui;

import static seedu.address.ui.TagUtil.createTagNode;

import java.time.LocalDate;
import java.util.Comparator;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import seedu.address.model.person.Person;

/**
 * A UI component that displays information of a {@code Person}.
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
    @FXML
    private HBox notesRow;
    @FXML
    private Label notes;
    @FXML
    private Label circleBadge;

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
        address.setText("Address: " + (person.isDefaultAddress() ? "-" : addressValue));
        email.setText(person.isDefaultEmail() ? "-" : emailValue);

        email.getStyleClass().removeAll("person-email-link", "person-field-empty");

        boolean hasEmail = !emailValue.equals("missing@email.empty");

        if (hasEmail) {
            email.setText(emailValue);
            email.getStyleClass().add("person-email-link"); // underline only when real email
        } else {
            email.setText("-");
            email.getStyleClass().add("person-field-empty"); // grey, not underlined
        }

        followUpDate.getStyleClass().add("follow-up-date-value");
        followUpDate.getStyleClass().removeAll("follow-up-soon", "follow-up-overdue");

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

        circleBadge.getStyleClass().removeAll("circle-client", "circle-prospect", "circle-friend");

        person.getCircle().ifPresentOrElse(c -> {
            String circle = c.getCircleName();
            circleBadge.setText(circle);
            setShown(circleBadge, true);

            switch (circle) {
            case "client":
                circleBadge.getStyleClass().add("circle-client");
                break;
            case "prospect":
                circleBadge.getStyleClass().add("circle-prospect");
                break;
            case "friend":
                circleBadge.getStyleClass().add("circle-friend");
                break;
            default:
                break;
            }
        }, () -> setShown(circleBadge, false));

        person.getTags().stream()
                .sorted(Comparator.comparing(tag -> tag.tagName))
                .limit(5)
                .forEach(tag -> tags.getChildren().add(createTagNode(tag.tagName)));
    }

    public static void setShown(Node node, boolean shown) {
        node.setVisible(shown);
        node.setManaged(shown);
    }

}
