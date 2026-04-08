package seedu.address.ui;

import java.util.Optional;

import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;

/**
 * Show confirmation dialog.
 * Implements {@code ConfirmationProvider}.
 */
public class ConfirmationDialog {

    /**
     * Shows confirmation dialog with message.
     * Returns true if the user clicked OK, false otherwise.
     * @param message The message to display in the dialog.
     * @return true if user confirmed, false if cancelled.
     */
    public static boolean showConfirmation(String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Action");
        alert.setHeaderText("Are you sure?");
        alert.setContentText(message);

        // Set CANCEL as the default button and remove default from OK
        alert.setOnShown(event -> {
            Button cancelButton = (Button) alert.getDialogPane().lookupButton(ButtonType.CANCEL);
            Button okButton = (Button) alert.getDialogPane().lookupButton(ButtonType.OK);

            if (cancelButton != null) {
                cancelButton.setDefaultButton(true);
                cancelButton.requestFocus();
            }
            if (okButton != null) {
                okButton.setDefaultButton(false);
            }
        });

        Optional<ButtonType> result = alert.showAndWait(); // returns Optional
        return result.isPresent() && result.get() == ButtonType.OK; // Optional must be present & ( result == OK )
    }
}
