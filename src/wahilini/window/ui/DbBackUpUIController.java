package wahilini.window.ui;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.scene.control.ButtonType;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javax.swing.JFileChooser;

public class DbBackUpUIController implements Initializable {

    @FXML
    private TextArea txtFileLocation;
    @FXML
    private Button btnBackup;
    @FXML
    private Button btnLocation;
    private String path;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        path = "";
        txtFileLocation.setText("");
        btnBackup.setDisable(true);
    }

    @FXML
    private void btnBackupAP(ActionEvent event) {
        String dbUserName = "root";
        String dbPassword = "root";
        String dbName = "budget_battery";
        try {
            Alert confirmAlert = CommonFeature.getConfirmAlert("Database Backup");
            confirmAlert.setContentText("Do you want create to backup?");
            if (confirmAlert.showAndWait().get() == ButtonType.YES) {
                /* Process */
                Runtime runtime = Runtime.getRuntime();
                Process process = runtime.exec("C:/Program Files/MySQL/MySQL Server 5.5/bin/mysqldump.exe -u" + dbUserName + " -p" + dbPassword
                        + " --database " + dbName + " -r" + path);

                Alert information = new Alert(Alert.AlertType.INFORMATION);
                information.setTitle("Information Message");
                if (process.waitFor() == 0) {
                    information.setContentText("Backup is successfuly created!");
                    information.showAndWait();
                } else {
                    information.setContentText("Backup cannot be created!");
                    information.showAndWait();
                }
                txtFileLocation.setText("");
                 btnBackup.setDisable(true);
            }
        } catch (NullPointerException | InterruptedException | IOException ex) {

        }
    }

    @FXML
    private void btnLocationAP(ActionEvent event) {

        try {
            String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

            JFileChooser jFileChooser = new JFileChooser();
            jFileChooser.showOpenDialog(jFileChooser);
            File file = jFileChooser.getSelectedFile();
            path = file.getAbsolutePath().replace('\\', '/');
            path += "_" + date + ".sql";
            txtFileLocation.setText(path);
            btnBackup.setDisable(false);

        } catch (NullPointerException ex) {
            System.out.println(ex.getCause());
        }
    }

}
