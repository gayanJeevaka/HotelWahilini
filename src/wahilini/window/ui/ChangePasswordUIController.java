package wahilini.window.ui;

import wahilini.window.dao.CommonDao;
import wahilini.window.entity.SysUser;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;

public class ChangePasswordUIController implements Initializable {

    @FXML
    private PasswordField txtOldPW;
    @FXML
    private PasswordField txtNewPW;
    @FXML
    private PasswordField txtConfirmPW;
    @FXML
    private Button btnChange;
    private SysUser user;
    private SysUser oldUser;
    @FXML
    private TextField txtUserName;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        resetForm();

    }

    private void resetForm() {
        this.user = LoginUIController.user;
        this.oldUser = new SysUser();
        oldUser.setPassword(user.getPassword());
        oldUser.setUserName(user.getUserName());
        txtConfirmPW.setText("");
        txtNewPW.setText("");
        txtOldPW.setText("");
        txtConfirmPW.setStyle(null);
        txtNewPW.setStyle(null);
        txtOldPW.setStyle(null);
        txtUserName.setStyle(CommonFeature.COL_VALID);
        txtUserName.setText(user.getUserName());
    }

    @FXML
    private void txtNewPWKR(KeyEvent event) {
        checkConfirmPassword();
        if (txtNewPW.getText().matches("[\\S]{6,20}")) {
            txtNewPW.setStyle(CommonFeature.COL_VALID);

        } else {
            txtNewPW.setStyle(null);
        }
    }

    @FXML
    private void txtConfirmPWKR(KeyEvent event) {
        checkConfirmPassword();
        if (txtConfirmPW.getText().isEmpty()) {
            txtConfirmPW.setStyle(null);
        }

    }

    @FXML
    private void btnClearAP(ActionEvent event) {
        resetForm();
    }

    @FXML
    private void btnChangeAP(ActionEvent event) {
        boolean isOldPW = oldUser.getPassword().matches(txtOldPW.getText());
        boolean isPWChanged = !user.getPassword().equals(oldUser.getPassword());
        boolean isEmpty = isOldPW && txtNewPW.getText().isEmpty() && txtConfirmPW.getText().isEmpty();
        boolean isUNChanged = txtUserName.getText().matches("[\\S]{4,20}") && !user.getUserName().equals(oldUser.getUserName());
        if ((isUNChanged && isEmpty) || (isPWChanged && isOldPW && checkConfirmPassword())) {
            Alert confirmAlert = CommonFeature.getConfirmAlert("Change Password");
            confirmAlert.setContentText("Do you want to Change Your username or password!");
            if (confirmAlert.showAndWait().get() == ButtonType.YES) {
                CommonDao.updateObject(user);
                resetForm();
                MainWindowUIController.tempStage.close();
            }

        } else {
            CommonFeature.ERROR_ALERT.setHeaderText("Change Password");
            CommonFeature.ERROR_ALERT.setContentText(isEmpty ? "Please enter old password or new user name !" : "Check old, new, confirm passwords!"
                    + "\n-New password length should be than 6 characters.");
            CommonFeature.ERROR_ALERT.showAndWait();
        }
    }

    // Check whether both passwords are similar
    private boolean checkConfirmPassword() {
        String newPassword = txtNewPW.getText();
        String confirmPassword = txtConfirmPW.getText();
        boolean chkPasswords = false;

        if (!newPassword.isEmpty() && !confirmPassword.isEmpty() && newPassword.matches("[\\S]{6,20}") && confirmPassword.matches("[\\S]{6,20}")) {
            if (newPassword.equals(confirmPassword)) {
                user.setPassword(newPassword);
                txtConfirmPW.setStyle(CommonFeature.COL_VALID);
                txtNewPW.setStyle(CommonFeature.COL_VALID);
                chkPasswords = true;
            } else {
                txtConfirmPW.setStyle(null);
                txtNewPW.setStyle(null);
                chkPasswords = false;
            }
        }
        return chkPasswords;
    }

    @FXML
    private void txtUserNameKR(KeyEvent event) {
        String userName = txtUserName.getText();

        if (userName.matches("[\\S]{4,20}")) {

            user.setUserName(userName);
            txtUserName.setStyle(CommonFeature.COL_VALID);

        } else {
            user.setUserName("");
            txtUserName.setStyle(null);
        }

    }

}
