package seedu.address.ui;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 * Utility class to give tag its UI design.
 */
public class TagUtil {

    private static final double HEIGHT = 40;
    private static final double NOTCH = 8;
    private static final double HORIZONTAL_PADDING = 15;
    private static final double MIN_WIDTH = 50;

    private TagUtil() {
    }

    /**
     * Creates a tag-shaped node displaying the given tag name.
     *
     * @param tagName the text to display inside the tag
     * @return a Node styled as a price tag
     */
    public static Node createTagNode(String tagName) {

        Label label = new Label(tagName);
        label.setStyle(
                "-fx-text-fill: white;"
                        + "-fx-font-family: 'Segoe UI Semibold';"
                        + "-fx-font-size: 12px;"
        );

        // Find size of text
        Text measurer = new Text(tagName);
        measurer.setFont(Font.font("Segoe UI Semibold", 12));
        double textWidth = measurer.getLayoutBounds().getWidth();
        double widthOfTag = Math.max(MIN_WIDTH, textWidth + HORIZONTAL_PADDING + NOTCH);

        Polygon tagShape = new Polygon(
                NOTCH, 0,
                widthOfTag, 0,
                widthOfTag, HEIGHT,
                NOTCH, HEIGHT,
                0, HEIGHT / 2.0
        );
        tagShape.setFill(javafx.scene.paint.Color.web("#3e7b91"));

        // Make tag a fixed size
        Pane shapePane = new Pane(tagShape);
        shapePane.setMinSize(widthOfTag, HEIGHT);
        shapePane.setMaxSize(widthOfTag, HEIGHT);

        // Ensures the contents are added on top in order
        StackPane stack = new StackPane();
        stack.getChildren().addAll(shapePane, label);
        StackPane.setAlignment(label, javafx.geometry.Pos.CENTER);
        label.setTranslateX(2);

        return stack;
    }
}
