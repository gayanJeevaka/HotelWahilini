package wahilini.window.ui;

import wahilini.window.dao.CommonDao;
import wahilini.window.dao.EmployeeDao;
import wahilini.window.entity.Employee;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import org.hibernate.HibernateException;
import static wahilini.window.ui.LoginUIController.privileges;

public class EmployeeUIController implements Initializable {

    @FXML
    private TextField txtName;
    @FXML
    private TextField txtTellNo2;
    @FXML
    private TextField txtTellNo1;
    @FXML
    private TextField txtNIC;
    @FXML
    private TextArea txtAddress;
    @FXML
    private TableColumn colName;
    @FXML
    private TableColumn colTelNo1;
    @FXML
    private TableColumn colNIC;
    @FXML
    private TableColumn colAddress;
    @FXML
    private Button btnSave;
    @FXML
    private Button btnDelete;
    @FXML
    private Button btnUpdate;
    @FXML
    private TextField txtSerName;
    @FXML
    private TableView<Employee> tbEmployee;
    @FXML
    private RadioButton rbtMale;
    @FXML
    private RadioButton rbtFemale;
    @FXML
    private ToggleGroup tgGender;
    @FXML
    private TableColumn<?, ?> colGender;
    private Employee employee;
    private Employee oldEmployee;
    private ObservableList<Employee> employees;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        resetForm();
    }

    private void resetForm() {
        employees = CommonDao.getObjectObservableList("Employee.findAll");
        oldEmployee = new Employee();
        employee = new Employee();
        employee.setGender("Male");
        tgGender.selectToggle(null);
        employee.setTelNo2("");
        txtAddress.setText("");
        txtNIC.setText("");
        txtName.setText("");
        txtTellNo1.setText("");
        txtTellNo2.setText("");
        tbEmployee.getItems().clear();
        fillTable(employees);
        txtAddress.setStyle(null);
        txtNIC.setStyle(CommonFeature.COL_EMPTY);
        txtName.setStyle(CommonFeature.COL_EMPTY);
        txtTellNo1.setStyle(CommonFeature.COL_EMPTY);
        txtTellNo2.setStyle(CommonFeature.COL_EMPTY);
        enableButtons(false, true, true);
        rbtMale.setSelected(true);
    }

    private void enableButtons(boolean save, boolean update, boolean delete) {
        btnSave.setDisable(save || !privileges.get("Employee_Reg_Form_insert"));
        btnUpdate.setDisable(update || !privileges.get("Employee_Reg_Form_update"));
        btnDelete.setDisable(delete || !privileges.get("Employee_Reg_Form_delete"));

    }

    @FXML
    private void btnSaveAP(ActionEvent event) {

        try {
            if (getErrors().isEmpty()) {
                String message = "Do you need to save this employee?\n"
                        + "---------------------------------------------------------- \n"
                        + "Name :- " + employee.getName() + "\n"
                        + "Gender :- " + employee.getGender() + "\n"
                        + "NIC :- " + employee.getNic() + "\n"
                        + "Telephone 1 :- " + employee.getTelNo1() + "\n"
                        + "Telephone 2 :- " + employee.getTelNo2() + "\n"
                        + "Address :- " + employee.getAddress() + "\n"
                        + "---------------------------------------------------------- \n";
                Alert confirmAlert = CommonFeature.getConfirmAlert("Employee Form-Save");
                confirmAlert.setContentText(message);
                if (confirmAlert.showAndWait().get() == ButtonType.YES) {
                    CommonDao.insertObject(employee);
                    resetForm();
                }
            } else {

                CommonFeature.ERROR_ALERT.setHeaderText("Employee-Save");
                CommonFeature.ERROR_ALERT.setContentText(getErrors());
                CommonFeature.ERROR_ALERT.showAndWait();
            }
        } catch (NullPointerException | HibernateException ex) {
            CommonFeature.printException("Exception from Employee-Save " + ex.getMessage());
            CommonFeature.ERROR_ALERT.setHeaderText("Employee-Save");
            CommonFeature.ERROR_ALERT.setContentText("Employee couldn't save please try again..");
            CommonFeature.ERROR_ALERT.showAndWait();
        }

    }

    @FXML
    private void btnDeleteAP(ActionEvent event) {
        Alert confirmAlert = CommonFeature.getConfirmAlert("Employee Form-Delete");
        confirmAlert.setContentText("Do you want to delete this employee? :- " + employee.getName());
        if (confirmAlert.showAndWait().get() == ButtonType.YES) {
            employee.setNic(null);
            CommonDao.updateObject(employee);
            resetForm();
        }
    }

    @FXML
    private void btnUpdateAP(ActionEvent event) {
        if (!getUpdates().isEmpty() && getErrors().isEmpty()) {
            Alert confirmAlert = CommonFeature.getConfirmAlert("Employee Form-Update");
            confirmAlert.setContentText(getUpdates());
            if (confirmAlert.showAndWait().get() == ButtonType.YES) {
                CommonDao.updateObject(employee);
                resetForm();
            }
        } else {
            String msg = !getErrors().isEmpty() ? getErrors() : "Nothing changed";
            CommonFeature.ERROR_ALERT.setHeaderText("Employee Form-Update");
            CommonFeature.ERROR_ALERT.setContentText(msg);
            CommonFeature.ERROR_ALERT.showAndWait();
        }
    }

    @FXML

    private void btnClearAP(ActionEvent event) throws IOException {
        Alert confirmAlert = CommonFeature.getConfirmAlert("Employee Form-Clear");
        confirmAlert.setContentText("Do you want to clear this form?");
        if (confirmAlert.showAndWait().get() == ButtonType.YES) {
            resetForm();
        }
    }

    private String getUpdates() {
        String msg = "";
        msg += !oldEmployee.getName().equals(employee.getName()) ? "Name :- " + oldEmployee.getName() + " changed to " + employee.getName() + "\n" : "";
        msg += !oldEmployee.getNic().equals(employee.getNic()) ? "NIC :- " + oldEmployee.getNic() + " changed to " + employee.getNic() + "\n" : "";
        msg += !oldEmployee.getGender().equals(employee.getGender()) ? "Gender :- " + oldEmployee.getGender() + " changed to " + employee.getGender() + "\n" : "";
        msg += !oldEmployee.getTelNo1().equals(employee.getTelNo1()) ? "Tel No1 :- " + oldEmployee.getTelNo1() + " changed to " + employee.getTelNo1() + "\n" : "";
        msg += !oldEmployee.getTelNo2().equals(employee.getTelNo2()) ? "TelNo2 :- " + oldEmployee.getTelNo2() + " changed to " + employee.getTelNo2() + "\n" : "";
        msg += !oldEmployee.getAddress().equals(employee.getAddress()) ? "Address :- " + oldEmployee.getAddress() + " changed to " + employee.getAddress() + "\n" : "";
        return msg;
    }

    private void fillTable(ObservableList<Employee> employees) {
        colAddress.setCellValueFactory(new PropertyValueFactory("address"));
        colGender.setCellValueFactory(new PropertyValueFactory("gender"));
        colNIC.setCellValueFactory(new PropertyValueFactory("nic"));
        colName.setCellValueFactory(new PropertyValueFactory("name"));
        colTelNo1.setCellValueFactory(new PropertyValueFactory("telNo1"));

        tbEmployee.setItems(employees);

    }

    private void fillForm(Employee employee) {

        txtAddress.setText(employee.getAddress());
        txtNIC.setText(employee.getNic());
        tgGender.selectToggle(employee.getGender().matches("Male") ? rbtMale : rbtFemale);
        txtName.setText(employee.getName());
        txtTellNo1.setText(employee.getTelNo1());
        txtTellNo2.setText(employee.getTelNo2());
        txtAddress.setStyle(null);
        txtNIC.setStyle(CommonFeature.COL_EMPTY);
        txtName.setStyle(CommonFeature.COL_EMPTY);
        txtTellNo1.setStyle(CommonFeature.COL_EMPTY);
        txtTellNo2.setStyle(CommonFeature.COL_EMPTY);
        enableButtons(true, false, false);
    }

    @FXML
    private void txtNameKR(KeyEvent event) {
        String name = txtName.getText();
        if (name.matches("[a-zA-Z\\s.]{3,50}")) {
            employee.setName(name);
            txtName.setStyle(CommonFeature.COL_VALID);
        } else {
            employee.setName("");
            txtName.setStyle(name.isEmpty() ? CommonFeature.COL_EMPTY : CommonFeature.COL_INVALID);

        }
    }

    @FXML
    private void txtTellNo1KR(KeyEvent event) {
        String tele = txtTellNo1.getText();
        if (tele.matches("0[1-9]{2}\\d{7}")) {
            txtTellNo1.setStyle(CommonFeature.COL_VALID);
            employee.setTelNo1(tele);
        } else {
            employee.setTelNo1("");
            txtTellNo1.setStyle(tele.isEmpty() ? CommonFeature.COL_EMPTY : CommonFeature.COL_INVALID);

        }
    }

    @FXML
    private void txtTellNo2KR(KeyEvent event) {
        String tele = txtTellNo2.getText();
        if (tele.matches("0[1-9]{2}\\d{7}")) {
            txtTellNo2.setStyle(CommonFeature.COL_VALID);
            employee.setTelNo2(tele);
        } else {
            employee.setTelNo2("");
            txtTellNo2.setStyle(tele.isEmpty() ? CommonFeature.COL_EMPTY : CommonFeature.COL_INVALID);

        }

    }

    @FXML
    private void txtNICKR(KeyEvent event) {
        String nic = txtNIC.getText();
        if (nic.matches("^[1-9][\\d\\W]{10}$|^[5-9][\\d\\W]{8}[v|V]$")) {
            employee.setNic(nic);
            txtNIC.setStyle(CommonFeature.COL_VALID);
        } else {
            employee.setNic("");
            txtNIC.setStyle(nic.isEmpty() ? CommonFeature.COL_EMPTY : CommonFeature.COL_INVALID);

        }

    }

    @FXML
    private void txtAddressKR(KeyEvent event) {
        String address = txtAddress.getText().trim();
        if (address.matches("[\\w\\W]{3,}")) {
            txtAddress.setStyle(CommonFeature.COL_VALID);
            employee.setAddress(address);
        } else {
            employee.setAddress("");
            txtAddress.setStyle(address.isEmpty() ? CommonFeature.COL_EMPTY : CommonFeature.COL_INVALID);

        }

    }

    private String getErrors() {
        String errors = "";
        //Check invalid data
        errors += !txtName.getText().isEmpty() && employee.getName().equals("") ? "Invalid Name \n" : "";
        errors += !txtTellNo1.getText().isEmpty() && employee.getTelNo1().equals("") ? "Invalid Telephone 1 \n" : "";
        errors += !txtTellNo2.getText().isEmpty() && employee.getTelNo2().equals("") ? "Invalid Telephone 2\n" : "";
        errors += !txtNIC.getText().isEmpty() && employee.getNic().equals("") ? "Invalid NIC \n" : "";
        errors += !txtAddress.getText().isEmpty() && employee.getAddress().equals("") ? "Invalid Address \n" : "";
        //Check emptiness data
        errors += txtName.getText().isEmpty() ? "Enter Name \n" : "";
        errors += employee.getGender() == null ? "Select Gender \n" : "";
        errors += txtNIC.getText().isEmpty() ? "Enter NIC number \n" : "";
        errors += txtTellNo1.getText().isEmpty() ? "Enter Telephone 1\n" : "";
        errors += txtAddress.getText().isEmpty() ? "Enter Address \n" : "";

        return errors;
    }

    @FXML
    private void txtSerNameKR(KeyEvent event) {
        String value = txtSerName.getText();
        if (value.isEmpty()) {
            tbEmployee.setItems(employees);
        } else {
            tbEmployee.setItems(EmployeeDao.getEmployeeByName(value));
        }

    }

    @FXML
    private void tbEmployeeMC(MouseEvent event) {
        try {

            if (event.getClickCount() == 2) {
                employee = tbEmployee.getSelectionModel().getSelectedItem();
                if (employee != null) {
                    Alert confirmAlert = CommonFeature.getConfirmAlert("Employee Form-Select");
                    confirmAlert.setContentText("Do you want to select this employee? :- " + employee.getName());
                    if (confirmAlert.showAndWait().get() == ButtonType.YES) {
                        oldEmployee.setAddress(employee.getAddress());
                        oldEmployee.setGender(employee.getGender());
                        oldEmployee.setName(employee.getName());
                        oldEmployee.setNic(employee.getNic());
                        oldEmployee.setTelNo1(employee.getTelNo1());
                        oldEmployee.setTelNo2(employee.getTelNo2());
                        fillForm(employee);
                    }

                }
            }

        } catch (Exception ex) {
            CommonFeature.printException("Exception from tbEmployee M.Clicked ");
            System.out.println("Error from tbEmployee M.Clicked \n" + ex.getMessage());
        }

    }

    @FXML
    private void rbtGender(ActionEvent event) {
        employee.setGender(rbtFemale.isSelected() ? "Female" : "Male");
    }

}
