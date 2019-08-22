package wahilini.window.ui;

import wahilini.window.dao.DBManager;
import wahilini.window.dao.UserDao;
import wahilini.window.entity.Employee;
import wahilini.window.entity.Privilege;
import wahilini.window.entity.SysUser;
import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import static wahilini.window.ui.MainApplication.stages;

public class LoginUIController implements Initializable {

    @FXML
    private TextField txtUsername;
    @FXML
    private PasswordField txtPassword;
    @FXML
    private Button btnCancel;
    @FXML
    private Button btnLogin;
    @FXML
    private Label lblMessage;
    private int attempt;
    public static SysUser user;
    public static HashMap<String, Boolean> privileges;
    public static Employee employee;

    @FXML
    private AnchorPane paneLogin;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        attempt = 3;
        user = new SysUser(); // declare as  null for sign out purpose
        privileges = null;// declare as  null for sign out purpose    
        employee = new Employee();

    }

    @FXML
    private void btnCancelAP(ActionEvent event) {
        System.exit(1);
    }

    @FXML
    private void btnLoginAP(ActionEvent event) {
        if (attempt == 0) {
            System.exit(1);
        } else {
            String query = "SELECT u.user_name,u.password,u.id,e.id,e.name FROM sys_user u inner join employee e on u.employee_id = e.id "
                    + "WHERE u.user_name =? AND u.password =? AND e.nic IS NOT NULL";

            try {
                PreparedStatement statement = DBManager.JDBC_CONNECTION.prepareStatement(query);
                statement.setString(1, txtUsername.getText());
                statement.setString(2, txtPassword.getText());
                ResultSet results = statement.executeQuery();

                if (results.next()) {
                    int id = results.getInt("id");
                    user.setId(results.getInt("u.id"));
                    user.setUserName(results.getString("u.user_name"));
                    user.setPassword(results.getString("u.password"));
                    user.setEmployeeId(employee);
                    employee.setName(results.getString("e.name"));
                    employee.setId(results.getInt("e.id"));
                    privileges = new HashMap();
                    
                    ObservableList<Privilege> loadPrivileges = UserDao.getModulesByUserId(id);

                    loadPrivileges.stream().forEach((privilage) -> {
                        String moduleName = privilage.getModuleId().getName();
                        privileges.put(moduleName + "_select", (privilage.getPrivileges() >= 1));
                        privileges.put(moduleName + "_insert", (privilage.getPrivileges() >= 2));
                        privileges.put(moduleName + "_update", (privilage.getPrivileges() >= 3));
                        privileges.put(moduleName + "_delete", (privilage.getPrivileges() >= 4));
                    });                   
                    statement.close();

                } else {
                    lblMessage.setText("Login Faild. You have " + (attempt--) + " more attemts.");
                }
            } catch (SQLException  ex) {
                CommonFeature.printException("Exception from btnLoginAp() ");
                System.out.println("Exception from btnLoginAp()" + ex.getMessage());
            }
        }
        if (privileges != null) {
            try {
                Parent root = FXMLLoader.load(getClass().getResource("MainWindowUI.fxml"));
                Scene scene = new Scene(root);
                stages.close();
                stages = new Stage();
                stages.setScene(scene);
                stages.setTitle("D&B Lovers and Gift Centre");
                stages.initStyle(StageStyle.TRANSPARENT);
                stages.show();
                stages.setResizable(false);
            } catch (IOException ex) {
                CommonFeature.printException("Exception from btnLoginAp() ");
                System.out.println("Exception from btnLoginAp() \n" + ex.getMessage());
            }
        }
    }

    @FXML
    private void paneLoginKR(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            btnLogin.fire();
        }
    }
}
