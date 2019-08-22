package wahilini.window.ui;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;

public final class CommonFeature {

    public static final DecimalFormat TO_DECI_FORMAT = new DecimalFormat("#.##");
    public static final Alert ERROR_ALERT = new Alert(Alert.AlertType.ERROR);
    public static final String COL_VALID = "-fx-background-color:#66ff66";
    public static final String COL_INVALID = "-fx-background-color:pink";
    public static final String COL_EMPTY = "-fx-background-color:white";
    private static final Alert information = new Alert(Alert.AlertType.WARNING);

    static {
        ERROR_ALERT.setTitle("Error Message");
    }

    public static void printException(String message) {

        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("ExceptionLog.txt", true))) {
            bw.write(message + dateFormat.format(date) + "\n");
        } catch (IOException ex1) {
            System.out.println("Exception from printException method \n" + ex1.getMessage());
        }
    }

    public static Alert getConfirmAlert(String headerText) {
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION, "", ButtonType.YES, ButtonType.NO);
        confirmAlert.setTitle("Confirmation Message");
        Button yesButton = (Button) confirmAlert.getDialogPane().lookupButton(ButtonType.YES);
        yesButton.setDefaultButton(true);
        yesButton.setText("Yes (Enter)");
        Button noButton = (Button) confirmAlert.getDialogPane().lookupButton(ButtonType.NO);
        noButton.setText("Cancel (ESC)");
        noButton.setDisable(true);
        confirmAlert.setHeaderText(headerText);
        return confirmAlert;
    }

    public static void getAlertInformation(String message) {
        information.setTitle("Warning Message");
        information.setContentText(message);
        information.showAndWait();
    }
}
